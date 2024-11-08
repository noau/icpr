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
          <!-- 作业标题 -->
          <el-table-column prop="title" label="作业标题" width="200"></el-table-column>
          <!-- 作业开始 -->
          <el-table-column prop="start" label="作业开始" width="180"></el-table-column>
          <!-- 作业截止 -->
          <el-table-column prop="end" label="作业截止" width="180"></el-table-column>
          <!-- 提交人数 -->
          <el-table-column prop="submitted" label="提交人数" width="100"></el-table-column>
          <!-- 提交时间 -->
          <el-table-column prop="submitTime" label="提交时间" width="150"></el-table-column>
          <!-- 得分 -->
          <el-table-column prop="score" label="得分" width="100"></el-table-column>
          <!-- 批改状态 -->
          <el-table-column prop="reviewStatus" label="批改状态" width="100"></el-table-column>
          <!-- 操作 -->
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button round type="text" @click="handleSubmit(scope.row)">提交</el-button>
              <el-button round type="text" @click="viewDetails(scope.row)">查看</el-button>
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
        <el-form-item label="作业内容">
          <el-input type="textarea" v-model="formData.content" placeholder="请输入3000字以内的作业内容！" rows="5"></el-input>
        </el-form-item>
        <el-form-item label="上传文件">
          <el-upload class="upload-demo" action="http://localhost:8080/attachment/upload"
            :on-success="handleUploadSuccess" :headers="headers" :on-preview="handlePreview" :on-remove="handleRemove"
            :file-list="fileList">
            <template v-slot:trigger>
              <el-button round style="padding: 10px;" size="small" type="primary">点击上传</el-button>
            </template>
            <template v-slot:tip>
              <div class="el-upload__tip">
                允许上传任意格式的文件
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
import { getcourseAssignments, getSubmissions } from '@/api/assignments.js';

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
    console.log('Course ID:', courseId);
    
    const response = await getcourseAssignments({ id: courseId });
    console.log('Full API Response:', response);

    if (Array.isArray(response)) {
      tableData.value = response;
      searchResult.value = tableData.value;
    } else {
      console.error('Invalid response structure:', response);
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

function viewDetails(row) {
  router.push({
    path: `/stu-end/course/examine/homework-details/${row.id}`
  });
}

function handlePreview(file) {
  console.log('Previewing file:', file);
}

function handleRemove(file) {
  console.log('Removing file:', file);
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
  console.log('Upload success:', res);
  formData.value.attachments = formData.value.attachments || [];
  formData.value.attachments.push(res.id); // 假设 res.id 是文件 ID
}
function formatDateToMysql(date) {
  return date.toISOString().slice(0, 19).replace('T', ' ');
}

async function submitAssignment() {
  try {
    const obj = {
      assignmentId: assignmentId.value,
      studentId: localStorage.getItem('userId'),
      submittedAt: formatDateToMysql(new Date()), // 转换为 MySQL 格式
      content: formData.value.content,
      attachments: formData.value.attachments || [],
    };
    const response = await getSubmissions(obj);
    console.log('Submission response:', response);
    dialogVisible.value = false;
  } catch (error) {
    console.error('Error submitting assignment:', error);
  }
}

</script>

<style scoped>
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

