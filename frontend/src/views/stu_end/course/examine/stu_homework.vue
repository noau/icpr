<template>
  <div>
    <el-row :gutter="20" class="header" style="margin-top: -25px;">
      <el-col :span="8">
        <el-input v-model="searchQuery" placeholder="输入作业名称进行搜索" class="search-input" />
      </el-col>
      <el-col :span="4">
        <el-button round style="margin-right: 100px; margin-left: -80px; padding: 10px;" type="primary"
          @click="handleSearch">搜索</el-button>
      </el-col>
    </el-row>
    <div class="box-card">
      <el-card>
        <el-table :data="paginatedData" style="width: 100%">
          <!-- 作业标题，点击跳转至详情页面 -->
          <el-table-column label="作业标题" width="200" label-class-name="table-label-center">
            <template #default="scope">
              <el-button type="text" @click="viewDetails(scope.row)">
                {{ scope.row.title }}
              </el-button>
            </template>
          </el-table-column>
          <!-- 作业开始 -->
          <el-table-column prop="start" label="作业开始" width="150" label-class-name="table-label-center"></el-table-column>
          <!-- 作业截止 -->
          <el-table-column prop="end" label="作业截止" width="150" label-class-name="table-label-center"></el-table-column>
          <!-- 得分 -->
          <el-table-column prop="grade" label="得分" width="100" label-class-name="table-label-center"></el-table-column>
          <!-- 批改状态 -->
          <el-table-column prop="isGrade" label="批改状态" width="100" label-class-name="table-label-center">
            <template #default="scope">
              <!-- 判断 peerReviewFinished 的值 -->
              <span v-if="scope.row.isGrade">已批改</span>
              <span v-else>未批改</span>
            </template>
          </el-table-column>
          <el-table-column prop="peerReviewFinished" label="互评状态" width="100" label-class-name="table-label-center">
            <template #default="scope">
              <!-- 判断 peerReviewFinished 的值 -->
              <span v-if="scope.row.peerReviewFinished">已完成</span>
              <span v-else>未完成</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" label-class-name="table-label-center">
            <template #default="scope">
              <el-button round type="text" @click="handleSubmit(scope.row)"
                :disabled="+new Date() < +new Date(scope.row.start) || +new Date() > +new Date(scope.row.end)">提交
              </el-button>
              <el-button round type="text" @click="peerReview(scope.row)" 
                :disabled="!scope.row.requirePeerReview || +new Date() < +new Date(scope.row.peerReviewStart) || +new Date() > +new Date(scope.row.peerReviewEnd)">
                互评
              </el-button>
              <!-- 如果未提交作业，查看按钮置灰 -->
              <el-button round type="text" @click="viewSubmission(scope.row)" 
                :disabled="!scope.row.submitted">查看</el-button>
            </template>
          </el-table-column>

        </el-table>

        <div class="pagination-container">
          <el-pagination layout="prev, pager, next" :total="filteredData.length" :page-size="pageSize"
            :current-page="currentPage" @current-change="handleCurrentChange" />
        </div>
      </el-card>
    </div>
    <!-- 提交作业的弹窗 -->
    <el-dialog title="文件内容" v-model="dialogVisible" width="500px">
      <el-form ref="submitForm" :model="formData">
        <!-- <el-form-item label="作业内容">
          <el-input type="textarea" v-model="formData.content" placeholder="请输入3000字以内的作业内容！" rows="5"></el-input>
        </el-form-item> -->
        <el-form-item label="上传文件">
          <el-upload class="upload-demo" action="http://localhost:8080/attachment/upload"
            :limit="1" :on-exceed="exceedFile"
            :on-success="handleUploadSuccess" :headers="headers" :on-preview="handlePreview" :on-remove="handleRemove"
            :file-list="fileList" accept=".pdf,.docx,.doc,.jpg,.jpeg,.png,.gif,.bmp,.zip,.rar">
            <template v-slot:trigger>
              <el-button round style="padding: 10px;" size="small" type="primary">点击上传</el-button>
            </template>
            <template v-slot:tip>
              <div class="el-upload__tip">
                允许上传的文件类型: pdf, doc, docx, jpg, jpeg, png, gif, bmp, zip, rar，文件不超过2G。
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <span class="dialog-footer">
          <el-button round style="padding: 10px;" type="primary" @click="submitAssignment">确定</el-button>
          <el-button round style="padding: 10px;" @click="dialogVisible = false">取消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getcourseAssignmentsStudent, getSubmissions } from '@/api/assignments.js';

const router = useRouter();
const tableData = ref([]);
const headers = {
  Authorization: localStorage.getItem('token')
};

const dialogVisible = ref(false);
const formData = ref({
  content: '',
  attachments: []
});
const fileList = ref([]);
const searchQuery = ref('');
const currentPage = ref(1);
const pageSize = ref(8);
const searchResult = ref([]);
const assignmentId = ref('');

// Computed properties for filtered and paginated data
const filteredData = computed(() => searchResult.value);
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredData.value.slice(start, start + pageSize.value);
});

// Fetch assignment list on component mount
onMounted(async () => {
  await getCourseAssignmentsList();
});

async function getCourseAssignmentsList() {
  try {
    const courseId = localStorage.getItem('courseId');
    if (!courseId) {
      console.error('Course ID is missing!');
      return;
    }
    
    const response = await getcourseAssignmentsStudent({ id: courseId, userId: +localStorage.getItem('userId')});
    
    if (Array.isArray(response)) {
      tableData.value = response;
      searchResult.value = tableData.value;
    } else {
      tableData.value = []; 
      searchResult.value = [];
    }
  } catch (error) {
    console.error('Error fetching assignments:', error);
  }
}


function handleSubmit(row) {
  assignmentId.value = row.id;
  dialogVisible.value = true;
}

function viewSubmission(row) {
  console.log('点击查看:', row);
  router.push({
    path: '/stu-end/course/examine/view-submission',
    query: {
      assignmentId: row.id,
      studentId: localStorage.getItem('userId'),
      title: row.title
    }
  });
}


/**
 * 点击互评跳转页面
 * @param row 
 */
function peerReview(row) {
  router.push({
    path: '/stu-end/course/examine/rating-page',
    query: {
      assignmentId:row.id,
      count:row.minPeerReview
    }
  });
}
function viewDetails(row) {
  console.log('Navigating to details of assignment:', row.id);
  router.push({
    path: `/stu-end/course/examine/homework-details`,
    query: {
      assignmentId: row.id
    }
  });
}



function handlePreview(file) {
  console.log('Previewing file:', file);
}

function handleRemove(file) {
  console.log('Removing file:', file);
  formData.value.attachments = []
}

function handleCurrentChange(page) {
  currentPage.value = page;
}

function handleSearch() {
  currentPage.value = 1;
  if (!searchQuery.value) {
    searchResult.value = tableData.value;
  } else {
    searchResult.value = tableData.value.filter(item =>
      item.title.includes(searchQuery.value)
    );
  }
}

function handleUploadSuccess(res) {
  formData.value.attachments.push(res.id);
}

function formatDateToMysql(date) {
  return date.toISOString().slice(0, 19).replace('T', ' ');
}

async function submitAssignment() {

  // 判断是否有文件上传
  if (formData.value.attachments.length <= 0) {
        alert('请先上传文件！'); // 提示用户上传文件
        return;
      }
  try {
    const obj = {
      assignmentId: assignmentId.value,
      studentId: localStorage.getItem('userId'),
      submittedAt: formatDateToMysql(new Date()),
      content: formData.value.content,
      attachments: formData.value.attachments || [],
    };

    const response = await getSubmissions(obj);
    // 找到对应的作业记录并更新其submitted状态
    const assignment = tableData.value.find(item => item.id === assignmentId.value);
    console.log(assignment);
    dialogVisible.value = false;
  } catch (error) {
    console.error('Error submitting assignment:', error);
  }
}

function exceedFile(){
  alert(
    '只能上传一个文件'
  );
}

</script>

<style scoped>
.table-label-center {
  text-align: center;
}
.el-table th, .el-table td {
  text-align: center;
}
.dialog-footer {
  text-align: right;
}
.box-card {
  margin: 20px;
  margin-top: 10px;
}
.search-input {
  flex: 1;
  width: 300px;
  margin-left: 20px;
}
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
