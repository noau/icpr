<template>
  <div class="student-list">
    <el-row :gutter="20" class="header" style="margin-top: -0px; margin-bottom: 5px; margin-left: 10px;">
      <el-col :span="8">
        <el-form :inline="true" :model="form">
          <el-form-item label="">
            <el-input v-model="form.query" placeholder="请输入姓名或学号" style="width: 250px"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" style="margin-left: -140px; padding: 10px;" @click="search">搜索</el-button>
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" style="margin-left: 460px;" @click="exportStudentList">导出学生名单</el-button>
      </el-col>
    </el-row>
    <div class="box-card">
      <el-card>
        <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto;">
          <el-table :data="paginatedData" style="width: 100%;" class="student-table">
            <el-table-column prop="userClass" label="序号" width="100" align="center" header-align="center"></el-table-column>
            <el-table-column prop="id" label="学号" width="200" align="center"
              header-align="center"></el-table-column>
            <el-table-column prop="name" label="姓名" width="200" align="center" header-align="center"></el-table-column>
            <el-table-column prop="academy" label="班级" width="200" align="center" header-align="center"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="220" align="center" header-align="center"></el-table-column>
            <el-table-column prop="phoneNumber" label="电话" width="200" align="center" header-align="center"></el-table-column>
          </el-table>
        </div>
        <div class="pagination-container">
          <el-pagination layout="prev, pager, next" :total="filteredStudentList.length" :page-size="pageSize"
            :current-page="currentPage" @current-change="handleCurrentChange" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { allstudents, exportstudentlist } from '@/api/course';

const form = ref({
  query: ''
});

const studentList = ref([]);
const pageSize = ref(10);
const currentPage = ref(1);

const filteredStudentList = computed(() => {
  if (!form.value.query) {
    return studentList.value;
  }
  return studentList.value.filter(student =>
    student.name.includes(form.value.query) || student.studentId.includes(form.value.query)
  );
});

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredStudentList.value.slice(start, start + pageSize.value);
});

const search = () => {
  console.log('搜索学生:', form.value.query);
};

// const exportStudentList = () => {
//   const id = localStorage.getItem('courseId');
//   if (!id) {
//     console.error("课程 ID 不存在，请检查 'courseId' 的存储情况。");
//     return;
//   }
//   exportstudentlist(id)
//     .then(res => {
//       console.log('exportstudentlist', res);
//     })
//     .catch(error => {
//       console.error("导出失败:", error);
//     });
// };

const exportStudentList = () => {
  const id = localStorage.getItem('courseId');
  if (!id) {
    console.error("课程 ID 不存在，请检查 'courseId' 的存储情况。");
    return;
  }
  
  exportstudentlist(id)
    .then(res => {
      console.log('exportstudentlist', res);
      const url = res.url; // Assuming the response contains the URL in 'res.url'

      // Check file type and open the URL accordingly
      const fileExtension = url.split('.').pop().toLowerCase();
      if (['jpg', 'jpeg', 'png', 'gif', 'bmp', 'xlsx'].includes(fileExtension)) {
        // Open image files in a new tab
        window.open(url, '_blank');
      } else if (['pdf', 'xlsx'].includes(fileExtension)) {
        // Open PDF files in a new tab
        window.open(url, '_blank');
      } else if (['doc', 'docx', 'ppt', 'pptx', 'xlsx'].includes(fileExtension)) {
        // Open Office files using Google Docs Viewer
        const viewerUrl = `https://docs.google.com/viewer?embedded=true&url=${encodeURIComponent(url)}`;
        window.open(viewerUrl, '_blank');
      } else {
        // Alert if file type is not supported for preview
        alert('不支持预览该文件类型');
      }
    })
    .catch(error => {
      console.error("导出失败:", error);
    });
};



function handleCurrentChange(page) {
  currentPage.value = page;
}

function getallstudents() {
  // let id = 'M310001B2计算机组成原理2024~2025上'
  const id = localStorage.getItem('courseId');
  const token = localStorage.getItem('token');
  console.log(id,token);
  
  allstudents(id, token).then(res => {
    console.log('学生列表:', res.students);
    studentList.value = res.students; // 假设返回的学生列表存储在 res.data.students
  }).catch(error => {
    console.error('获取学生列表失败:', error);
  });
}

getallstudents();
</script>


<style scoped>
.box-card {
  margin: 20px;
  margin-top: -5px;
}

.forum-list {
  padding: 0;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.el-button {
  font-size: 14px;
  padding: 8px 10px;
}
</style>