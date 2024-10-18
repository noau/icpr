<template>
  <div class="student-list">
    <el-row :gutter="20" class="header" style="margin-top: -25px; margin-bottom: 5px; margin-left: 10px;">
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
            <el-table-column prop="index" label="序号" width="100" align="center" header-align="center"></el-table-column>
            <el-table-column prop="studentId" label="学号" width="200" align="center" header-align="center"></el-table-column>
            <el-table-column prop="name" label="姓名" width="200" align="center" header-align="center"></el-table-column>
            <el-table-column prop="class" label="班级" width="200" align="center" header-align="center"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="220" align="center" header-align="center"></el-table-column>
            <el-table-column prop="phone" label="电话" width="200" align="center" header-align="center"></el-table-column>
          </el-table>
        </div>
        <div class="pagination-container">
          <el-pagination
            layout="prev, pager, next"
            :total="filteredStudentList.length"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import {allstudents} from '@/api/course'


const form = ref({
  query: ''
});

const studentList = ref([
  { index: 1, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 2, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
  { index: 3, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 4, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
  { index: 5, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 6, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
  { index: 7, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 8, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
  { index: 1, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 2, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
  { index: 1, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 2, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
  { index: 1, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 2, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
  { index: 1, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 2, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
  { index: 1, studentId: '20230001', name: '张三', class: '软件2201', email: 'zhangsan@example.com', phone: '12345678901' },
  { index: 2, studentId: '20230002', name: '李四', class: '软件2202', email: 'lisi@example.com', phone: '12345678902' },
]);

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

const exportStudentList = () => {
  console.log('导出学生名单');
  // 这里可以添加导出学生名单的逻辑，例如生成 CSV 文件并下载
};

function handleCurrentChange(page) {
  currentPage.value = page;
}
function getallstudents(){
  let id=localStorage.getItem('kcid')
  allstudents(id).then(res=>{
    console.log(res.students)
  })
}
getallstudents()
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