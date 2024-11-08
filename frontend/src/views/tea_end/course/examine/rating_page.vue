<!-- <template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <back_header/>
      </el-header>
      <el-main>
        <div class="grading-container"> 
      
          <div class="custom-table">
            <el-button round type="primary" style="margin-bottom: 10px; margin-right: 15px; padding: 7px;" @click="previousStudent">上一个</el-button>
            <el-button round style="margin-right: 25px; padding: 7px;" @click="nextStudent">下一个</el-button>
          </div>

          <el-row :gutter="20" class="content" style="margin-top: -30px;">
            <el-col :span="16">
              <el-card>
                <vue-office-pdf 
                  :src="pdf"
                  style="height: 100vh"
                  @rendered="renderedHandler"
                  @error="errorHandler"
                />
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <el-form :model="gradingForm">
                  <el-form-item label="成绩" required>
                    <el-input-number v-model="gradingForm.grade" :min="0" :max="10"></el-input-number>
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

<script setup>
import { ref } from 'vue';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import back_header from '@/components/back_header.vue';
import { useRoute } from 'vue-router';
import { getReviewList, submitReview } from '@/api/assignments.js';
import VueOfficePdf from '@vue-office/pdf';

const route = useRoute();
// 使用 github 主题
VMdEditor.use(githubTheme);

const tableData = ref();
const gradingForm = ref({
  submissionId: 0, // Ensure this is set to the correct ID dynamically
  grade: 0,
  comment: '',
  // gradedAt: new Date().toISOString() // Automatically set the current timestamp
  gradedAt: new Date().toISOString().slice(0, 19).replace('T', ' ') // Format as 'YYYY-MM-DD HH:MM:SS'
});

const pdf = ref('http://static.shanhuxueyuan.com/test.pdf'); // 设置 PDF 地址

const init = async () => {
  tableData.value = await getReviewList({ id: route.query.id });
  if (tableData.value.length > 0) {
    gradingForm.value.submissionId = tableData.value[currentStudentIndex.value].id; // 获取第一个学生的 submissionId
  }
};
init();

const currentStudentIndex = ref(0);

const submitGrading = async () => {
  try {
    const response = await submitReview(gradingForm.value);
    console.log('提交评分成功:', response);
    alert('评分提交成功');
  } catch (error) {
    console.error('提交评分失败:', error);
    alert('评分提交失败，请重试');
  }
};

const previousStudent = () => {
  if (currentStudentIndex.value > 0) {
    currentStudentIndex.value--;
    gradingForm.value.submissionId = tableData.value[currentStudentIndex.value].id; // 更新 submissionId
  }
};

const nextStudent = () => {
  if (currentStudentIndex.value < tableData.value.length - 1) {
    currentStudentIndex.value++;
    gradingForm.value.submissionId = tableData.value[currentStudentIndex.value].id; // 更新 submissionId
  }
};

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
</style> -->



<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <back_header />
      </el-header>
      <el-main>
        <div class="grading-container">
          <!-- Table -->
          <div class="custom-table">
            <el-button round type="primary" style="margin-bottom: 10px; margin-right: 15px; padding: 7px;" @click="previousStudent">上一个</el-button>
            <el-button round style="margin-right: 25px; padding: 7px;" @click="nextStudent">下一个</el-button>
          </div>

          <!-- 评分区域 -->
          <el-row :gutter="20" class="content" style="margin-top: -30px;">
            <el-col :span="16">
              <el-card>
                <vue-office-pdf :src="pdf" style="height: 100vh" @rendered="renderedHandler" @error="errorHandler" />
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <el-form :model="gradingForm">
                  <el-form-item label="成绩" required>
                    <el-input-number v-model="gradingForm.grade" :min="0" :max="10"></el-input-number>
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

<script setup>
import { ref } from 'vue';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import back_header from '@/components/back_header.vue';
import { useRoute } from 'vue-router';
import { getReviewList, submitReview } from '@/api/assignments.js';
import VueOfficePdf from '@vue-office/pdf';

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

const pdf = ref('http://static.shanhuxueyuan.com/test.pdf'); // 设置 PDF 地址
const currentStudentIndex = ref(0);

const init = async () => {
  try {
    const data = await getReviewList({ id: route.query.id });
    console.log("当前索引:11", data);
    tableData.value = data || [];
    if (tableData.value.length > 0) {
      updateSubmissionId();
    }
  } catch (error) {
    console.error("加载数据失败:", error);
  }
};
init();

const updateSubmissionId = () => {
  console.log("当前索引11:", currentStudentIndex.value,tableData.value);
  if (tableData.value.length > currentStudentIndex.value) {
    gradingForm.value.submissionId = tableData.value[currentStudentIndex.value].id; // 更新 submissionId
  }
};

const submitGrading = async () => {
  if (!gradingForm.value.submissionId) {
    alert('无法提交评分：未找到有效的 submissionId');
    return;
  }

  try {
    const response = await submitReview({...gradingForm.value,feedback:gradingForm.value.comment});
    console.log('提交评分成功:', response);
    alert('评分提交成功');
  } catch (error) {
    console.error('提交评分失败:', error);
    alert('评分提交失败，请重试');
  }
};

const previousStudent = () => {
  if (currentStudentIndex.value > 0) {
    currentStudentIndex.value--;
    updateSubmissionId(); // 更新 submissionId
  }
};

const nextStudent = () => {
  if (currentStudentIndex.value < tableData.value.length - 1) {
    currentStudentIndex.value++;
    updateSubmissionId(); // 更新 submissionId
  }
};

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
