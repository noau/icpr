<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <back_header />
      </el-header>
      <el-main>
        <div class="grading-container">
          <!-- 切换不同学生的作业 -->
          <div class="custom-table">
            <el-button round type="primary" style="margin-bottom: 10px; margin-right: 15px; padding: 7px;" @click="previousStudent">上一个</el-button>
            <el-button round style="margin-right: 25px; padding: 7px;" @click="nextStudent">下一个</el-button>
          </div>
          <!-- <div class="custom-table">
            <el-row>
              <el-col :span="23" style="margin-left: -40px; margin-right: -15px; margin-top: -40px;">
                <el-table :data="transformedData" border style="width: 100%" :show-header="false">
                  <el-table-column prop="label" width="100">
                    <template #default="scope">
                      <strong>{{ scope.row.label }}</strong>
                    </template>
                  </el-table-column>
                  <el-table-column
                    v-for="(item, index) in tableData"
                    :key="index"
                    :prop="`value${index}`"
                    width="90"
                  >
                    <template #default="scope">
                      <div :class="{'highlight': currentStudentIndex === index}">
                        {{ scope.row.label === '得分' && currentStudentIndex === index ? '正在批阅...' : scope.row[`value${index}`] }}
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </el-col>
              <el-col :span="1" class="button-col" style="margin-left: 40px;  margin-top: -40px;">
                <el-button round type="primary" style="margin-bottom: 10px; margin-right: 15px; padding: 7px;" @click="previousStudent">上一个</el-button>
                <el-button round style="margin-right: 25px; padding: 7px;" @click="nextStudent">下一个</el-button>
              </el-col>
            </el-row>
          </div> -->

          <!-- 评分区域 -->
          <el-row :gutter="20" class="content" style="margin-top: -30px;">
            <el-col :span="16">
              <el-card>
                <vue-office-pdf v-if="getFileTypeFromUrl(pdf) == 1" :src="pdf" style="height: 100vh" @rendered="renderedHandler" @error="errorHandler" />
                <vue-office-docx v-else-if="getFileTypeFromUrl(pdf) == 3" :src="pdf" style="height: 100vh" @rendered="renderedHandler" @error="errorHandler" />
                <img v-else-if="getFileTypeFromUrl(pdf) == 2" :src="attachmentUrl" alt="Attachment Image" style="max-width: 100%; height: auto;"/>
                <p v-else>暂无</p>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <el-form :model="gradingForm">
                  <!-- <el-form-item label="成绩" required>
                    <el-input-number v-model="gradingForm.grade" :min="0" :max="10"></el-input-number>
                  </el-form-item> -->
                  <el-form-item label="成绩" required>
                    <el-input-number v-model="gradingForm.grade" :min="0" :max="fullGrade"></el-input-number>
                  </el-form-item>
                  <el-form-item label="评语" label-position="top">
                    <v-md-editor v-model="gradingForm.comment" height="200px"></v-md-editor>
                  </el-form-item>
                  <el-form-item style="text-align: right;">
                    <el-button round type="primary" @click="submitGrading">确定</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<!-- <script setup>
import { ref } from 'vue';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import back_header from '@/components/back_header.vue';
import { useRoute } from 'vue-router';
import { getReviewList, submitReview } from '@/api/assignments.js';
import VueOfficePdf from '@vue-office/pdf';
//引入VueOfficeDocx组件
import VueOfficeDocx from '@vue-office/docx';
//引入相关样式
import '@vue-office/docx/lib/index.css'
import { getFileTypeFromUrl } from '@/utils/base.js';
import { getAttachmentUrl } from '@/api/common.js';


const route = useRoute();
// 使用 github 主题
VMdEditor.use(githubTheme);

const tableData = ref([]);
const gradingForm = ref({
  submissionId: null, // 初始为空
  grade: 0,
  comment: '',
  gradedAt: new Date().toISOString().slice(0, 19).replace('T', ' ') // 格式为 'YYYY-MM-DD HH:MM:SS'
});

const pdf = ref(''); // 设置 PDF 地址 http://static.shanhuxueyuan.com/test.pdf
const currentStudentIndex = ref(0);

const init = async () => {
  try {
    const data = await getReviewList({ id: route.query.submissionId });
    tableData.value = data || [];
    if (tableData.value.length > 0) {
      // 设置进入的初始索引
      currentStudentIndex.value = tableData.value.findIndex(item => item.id == route.query.id);
      updateSubmissionId();
      if(tableData.value[currentStudentIndex.value].attachments.length > 0)loadFileUrl(tableData.value[currentStudentIndex.value].attachments[0])
    }
  } catch (error) {
    console.error("加载数据失败:", error);
  }
};
init();


// 获取附件的 URL 并设置为 pdfUrl
const loadFileUrl = async (attachmentId) => {
  try {
    const response = await getAttachmentUrl(attachmentId);
    if (response && response.url) {
      pdf.value = response.url;
      // console.log(pdf.value + ":  " + getFileTypeFromUrl(response.url));
    } else {
      console.log('未获取到文件 URL');
    }
  } catch (error) {
    console.error('获取文件 URL 失败:', error);
  }
};

const updateSubmissionId = () => {
  console.log("当前索引11:", currentStudentIndex.value,tableData.value);
  if (tableData.value.length > currentStudentIndex.value) {
    gradingForm.value.submissionId = tableData.value[currentStudentIndex.value].id; // 更新 submissionId
  }
};

// const submitGrading = async () => {
//   if (!gradingForm.value.submissionId) {
//     alert('无法提交评分：未找到有效的 submissionId');
//     return;
//   }

//   try {
//     const response = await submitReview(gradingForm.value);
//     console.log('提交评分成功:', response);
//     alert('评分提交成功');
//   } catch (error) {
//     console.error('提交评分失败:', error);
//     alert('评分提交失败，请重试');
//   }
// };

const submitGrading = async () => {
  if (!gradingForm.value.submissionId) {
    alert('无法提交评分：未找到有效的 submissionId');
    return;
  }

  try {
    const response = await submitReview({...gradingForm.value,feedback:gradingForm.value.comment});
    console.log('提交评分成功:', response);
    alert('评分提交成功');

    // 提交成功后自动切换到下一个学生
    nextStudent();
  } catch (error) {
    console.error('提交评分失败:', error);
    alert('评分提交失败，请重试');
  }
};

const previousStudent = () => {
  if (currentStudentIndex.value > 0) {
    currentStudentIndex.value--;
    updateSubmissionId(); // 更新 submissionId
    if(tableData.value[currentStudentIndex.value].attachments.length > 0)loadFileUrl(tableData.value[currentStudentIndex.value].attachments[0])
  }
};

const nextStudent = () => {
  if (currentStudentIndex.value < tableData.value.length - 1) {
    currentStudentIndex.value++;
    updateSubmissionId(); // 更新 submissionId
    if(tableData.value[currentStudentIndex.value].attachments.length > 0)loadFileUrl(tableData.value[currentStudentIndex.value].attachments[0])
  }
};



const renderedHandler = () => {
  console.log("渲染完成");
};

const errorHandler = () => {
  console.log("渲染失败");
};
</script> -->

<script setup>
// Existing imports and setup
import { ref, onMounted } from 'vue';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import back_header from '@/components/back_header.vue';
import { useRoute } from 'vue-router';
import { getReviewList, submitReview, getAssignmentsInfo } from '@/api/assignments.js'; // Import getAssignmentsInfo API function
import VueOfficePdf from '@vue-office/pdf';
import VueOfficeDocx from '@vue-office/docx';
import '@vue-office/docx/lib/index.css'
import { getFileTypeFromUrl } from '@/utils/base.js';
import { getAttachmentUrl } from '@/api/common.js';

const route = useRoute();
VMdEditor.use(githubTheme);

const tableData = ref([]);
const gradingForm = ref({
  submissionId: null, 
  grade: 0,
  comment: '',
  gradedAt: new Date().toISOString().slice(0, 19).replace('T', ' ')
});

const pdf = ref('');
const currentStudentIndex = ref(0);
const fullGrade = ref(10); // Default max grade

// Initialize data
const init = async () => {
  try {
    // Fetch fullGrade from getAssignmentsInfo API
    const assignmentData = await getAssignmentsInfo({ id: route.query.id });
    fullGrade.value = assignmentData.fullGrade || 10; // Set fullGrade to API value, default to 10 if undefined

    const data = await getReviewList({ id: route.query.submissionId });
    tableData.value = data || [];
    if (tableData.value.length > 0) {
      currentStudentIndex.value = tableData.value.findIndex(item => item.id == route.query.id);
      updateSubmissionId();
      if(tableData.value[currentStudentIndex.value].attachments.length > 0) loadFileUrl(tableData.value[currentStudentIndex.value].attachments[0])
    }
  } catch (error) {
    console.error("加载数据失败:", error);
  }
};
onMounted(init);

// Load file URL function (unchanged)
const loadFileUrl = async (attachmentId) => {
  try {
    const response = await getAttachmentUrl(attachmentId);
    if (response && response.url) {
      pdf.value = response.url;
    } else {
      console.log('未获取到文件 URL');
    }
  } catch (error) {
    console.error('获取文件 URL 失败:', error);
  }
};

// Update submission ID function (unchanged)
const updateSubmissionId = () => {
  if (tableData.value.length > currentStudentIndex.value) {
    gradingForm.value.submissionId = tableData.value[currentStudentIndex.value].id;
  }
};

// Grading submission function (unchanged)
const submitGrading = async () => {
  if (!gradingForm.value.submissionId) {
    alert('无法提交评分：未找到有效的 submissionId');
    return;
  }
  try {
    const response = await submitReview(gradingForm.value);
    alert('评分提交成功');
    nextStudent();
  } catch (error) {
    alert('评分提交失败，请重试');
  }
};

// Student navigation functions (unchanged)
const previousStudent = () => {
  if (currentStudentIndex.value > 0) {
    currentStudentIndex.value--;
    updateSubmissionId();
    if(tableData.value[currentStudentIndex.value].attachments.length > 0) loadFileUrl(tableData.value[currentStudentIndex.value].attachments[0]);
  }
};

const nextStudent = () => {
  if (currentStudentIndex.value < tableData.value.length - 1) {
    currentStudentIndex.value++;
    updateSubmissionId();
    if(tableData.value[currentStudentIndex.value].attachments.length > 0) loadFileUrl(tableData.value[currentStudentIndex.value].attachments[0]);
  }
};

// Rendered and error handlers (unchanged)
const renderedHandler = () => {
  console.log("渲染完成");
};

const errorHandler = () => {
  console.log("渲染失败");
};
</script>


<style scoped>
.grading-container {
  padding: 20px;
}

.custom-table {
  margin-top: -20px;
  margin-bottom: 30px;
  display: flex;
}

.button-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.header {
  margin-bottom: 20px;
}

.content {
  margin-bottom: 20px;
}

.footer {
  display: flex;
  justify-content: space-between;
}

.right-align {
  text-align: right;
}
</style>
