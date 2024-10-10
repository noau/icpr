<template>
  <div class="homework-list">
    <el-row :gutter="20" class="header">
      <el-col :span="8">
        <el-form :inline="true" :model="form">
          <el-form-item label="作业标题：">
            <el-input v-model="form.title" placeholder="请输入作业标题"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" style="margin-left: -130px; padding: 10px;" @click="search">搜索</el-button>
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" style="margin-left: 100px;" @click="goToAssignHomework">布置作业</el-button>
      </el-col>
    </el-row>
    <el-card>
      <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto;">
        <el-table :data="paginatedData" style="width: 100%; padding-left: 50px;" class="homework-table">
          <el-table-column prop="title" label="作业标题" width="200"></el-table-column>
          <el-table-column prop="startTime" label="开始时间" width="200"></el-table-column>
          <el-table-column prop="endTime" label="截止时间" width="200"></el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button round type="primary" @click="editHomework(scope.row)">修改</el-button>
              <el-button round type="danger" @click="deleteHomework(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pagination-container">
        <el-pagination
          layout="prev, pager, next"
          :total="homeworkList.length"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';  // Add Vue Router for navigation

const router = useRouter();  // Instantiate Vue Router

const form = ref({
  title: ''
});

const homeworkList = ref([
  { title: '作业1', startTime: '2023-10-01', endTime: '2023-10-10' },
  { title: '作业2', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业1', startTime: '2023-10-01', endTime: '2023-10-10' },
  { title: '作业2', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业1', startTime: '2023-10-01', endTime: '2023-10-10' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
  { title: '作业3', startTime: '2023-10-05', endTime: '2023-10-15' },
]);

const pageSize = ref(8);
const currentPage = ref(1);

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return homeworkList.value.slice(start, start + pageSize.value);
});

const search = () => {
  console.log('搜索作业标题:', form.value.title);
};

const editHomework = (row) => {
  console.log('修改作业:', row);
};

const deleteHomework = (row) => {
  console.log('删除作业:', row);
};

const goToAssignHomework = () => {
  router.push({ path: 'tea-end/course/examine/assign-homework' });  // Navigate to AssignHomework page
};

function handleCurrentChange(page) {
  currentPage.value = page;
}
</script>

<style scoped>
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
