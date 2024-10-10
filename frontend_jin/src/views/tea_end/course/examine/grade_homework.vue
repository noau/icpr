<template>
  <div>
    <!-- Back Button -->
    <el-button round @click="goBack" type="primary" style="margin-bottom: 18px; margin-top: -20px; margin-left: -20px;">返回</el-button>

    <!-- Homework Title and Submission Time Header -->
    <el-card shadow="always" class="header-card">
      <el-row :gutter="20" class="header-content">
        <!-- Left side: Homework title and submission time -->
        <el-col :span="12" class="left-section">
          <h3>{{ homeworkTitle }}</h3>
          <p>提交起止时间：{{ startTime }} - {{ endTime }}</p>
        </el-col>

        <!-- Right side: Submission condition section -->
        <el-col :span="12" class="right-section">
          <el-row :gutter="20" class="grade-homework">
            <el-col :span="8">
              <el-card shadow="hover" class="status-box" @click="filterSubmitted('all')">
                <div>应提交</div>
                <div class="status-number" style="color: #07395f;">27</div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" class="status-box" @click="filterSubmitted('submitted')">
                <div>已提交</div>
                <div class="status-number" style="color: darkgreen;">2</div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" class="status-box" @click="filterSubmitted('notSubmitted')">
                <div>未提交</div>
                <div class="status-number" style="color: #780202da;">25</div>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-card>

    <!-- Table Section -->
    <el-card class="box-card">
      <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto;">
        <el-table :data="paginatedData" border style="width: 100%" class="resource-table">
          <el-table-column prop="id" label="学号" width="130" align="center" header-align="center" />
          <el-table-column prop="name" label="姓名" width="130" align="center" header-align="center" />
          <el-table-column prop="class" label="班级" width="130" align="center" header-align="center" />
          <el-table-column prop="email" label="邮箱" width="200" align="center" header-align="center" />
          <el-table-column prop="submitTime" label="提交时间" align="center" header-align="center" />
          <el-table-column prop="finalScore" label="成绩" width="130" align="center" header-align="center" />
          <el-table-column label="操作" width="180" align="center" header-align="center">
            <template #default="scope">
              <el-button round @click="handleEdit(scope.row)" type="text" size="small">批阅</el-button>
              <!-- <el-button round @click="handleDelete(scope.row)" type="danger" size="small">删除</el-button> -->
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- Pagination Section -->
      <div class="pagination-container">
        <el-pagination
          layout="prev, pager, next"
          :total="filteredData.length"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

const homeworkTitle = ref(route.query.title || '作业标题');
const startTime = ref(route.query.startTime || '');
const endTime = ref(route.query.endTime || '');

// Function to navigate back
const goBack = () => {
  router.back();
};

const tableData = ref([
  {
    id: '20233007',
    name: '龚敏',
    class: '软件2201',
    email: 'gongmin@example.com',
    submitTime: '2023-12-26 14:31:36',
    score: 100,
    reworkCount: 0,
    finalScore: 100,
    submitted: true,
  },
  {
    id: '20233003',
    name: '郑欧欣',
    class: '软件2202',
    email: 'zhengouxin@example.com',
    submitTime: '2023-12-26 09:59:06',
    score: 100,
    reworkCount: 0,
    finalScore: 100,
    submitted: true,
  },
  {
    id: '20233004',
    name: '李四',
    class: '软件2203',
    email: 'lisi@example.com',
    submitTime: '',
    score: 0,
    reworkCount: 0,
    finalScore: 0,
    submitted: false,
  },
]);

const filterType = ref('all');

const filteredData = computed(() => {
  if (filterType.value === 'submitted') {
    return tableData.value.filter(item => item.submitted);
  } else if (filterType.value === 'notSubmitted') {
    return tableData.value.filter(item => !item.submitted);
  } else {
    return tableData.value;
  }
});

const pageSize = ref(8);
const currentPage = ref(1);

// Paginated data based on current page
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredData.value.slice(start, start + pageSize.value);
});

const handleEdit = (row) => {
  router.push('/tea-end/course/examine/rating-page');
};

const handleDelete = (row) => {
  console.log(`Deleting: ${row.id}`);
};

const handlePageChange = (page) => {
  currentPage.value = page;
};

const filterSubmitted = (type) => {
  filterType.value = type;
  currentPage.value = 1; // Reset to the first page after filter change
};
</script>

<style scoped>
.header-card {
  margin-bottom: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 20px;
  height: 112px;
}

.header-content {
  display: flex;
  align-items: center;
}

.left-section {
  margin-top: -70px;
  background-color: #ffffff;
}

.right-section {
  margin-top: -70px;
  display: flex;
  justify-content: flex-end;
}

.grade-homework {
  display: flex;
  justify-content: flex-end;
}

.status-box {
  border-radius: 50%;
  width: 90px;
  height: 90px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.status-number {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}

.box-card {
  margin: 20px;
  margin-top: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>