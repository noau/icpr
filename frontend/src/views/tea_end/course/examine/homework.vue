<template>
  <div class="homework-list">
    <el-row :gutter="20" class="header" style="margin-top: -25px; margin-bottom: 2px;">
      <el-col :span="8">
        <el-form :inline="true" :model="form">
          <el-form-item label="">
            <el-input v-model="form.title" placeholder="请输入作业标题" class="search-input"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" style="margin-right: 100px; margin-left: -80px; padding: 10px;" @click="search">搜索</el-button>
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" style="margin-left: 495px;" @click="goToAssignHomework">布置作业</el-button>
      </el-col>
    </el-row>
    <div class="box-card">
      <el-card>
        <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto;">
          <el-table :data="paginatedData" style="width: 100%;" class="homework-table">
            <!-- 删除这一行：复选框列 -->
            <!-- <el-table-column type="selection" width="70"></el-table-column> -->

            <el-table-column prop="title" label="作业标题" width="350" align="center" header-align="center">
              <template #default="scope">
                <div>{{ scope.row.title }}</div>
                <div class="gray-text">提交起止时间：{{ scope.row.startTime }} - {{ scope.row.endTime }}</div>
              </template>
            </el-table-column>

            <el-table-column label="提交人数" width="150" align="center" header-align="center">
              <template #default="scope">
                {{ scope.row.submitted }}/27
              </template>
            </el-table-column>

            <el-table-column label="批阅作业" width="150" align="center" header-align="center">
              <template #default="scope">
                <el-icon class="custom-icon" @click="goToSubmissonCondition(scope.row)"><EditPen /></el-icon>
              </template>
            </el-table-column>

            <el-table-column label="成绩统计" width="150" align="center" header-align="center">
              <template #default="scope">
                <el-icon class="custom-icon" @click="goToHomeworkStatistics(scope.row)"><TrendCharts /></el-icon>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="250" align="center" header-align="center">
              <template #default="scope">
                <el-button type="text" @click="publishHomework(scope.row)">发布作业</el-button>
                <el-button type="text" @click="publishGrades(scope.row)">公布成绩</el-button>
                <el-button type="text" @click="publishAnswers(scope.row)">公布答案</el-button>
                <el-button type="text" @click="editHomework(scope.row)">修改</el-button>
                <el-button type="text" @click="deleteHomework(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-container">
          <el-pagination
            layout="prev, pager, next"
            :total="filteredHomeworkList.length"
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
import { useRouter } from 'vue-router';

const router = useRouter();

const form = ref({
  title: ''
});

const homeworkList = ref([
  { id: 1, title: '班级作业1', startTime: '2023-10-01', endTime: '2023-10-10', submitted: 0 },
  { id: 2, title: '班级作业2', startTime: '2023-10-05', endTime: '2023-10-15', submitted: 5 },
  { id: 3, title: '班级作业3', startTime: '2023-10-07', endTime: '2023-10-17', submitted: 10 },
]);

const pageSize = ref(8);
const currentPage = ref(1);
const searchQuery = ref('');

const filteredHomeworkList = computed(() => {
  if (!searchQuery.value) {
    return homeworkList.value;
  }
  return homeworkList.value.filter(homework =>
    homework.title.includes(searchQuery.value)
  );
});

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredHomeworkList.value.slice(start, start + pageSize.value);
});

const search = () => {
  searchQuery.value = form.value.title;
  currentPage.value = 1; // 重置到第一页
};

const publishHomework = (row) => {
  console.log('发布作业:', row);
};

const publishGrades = (row) => {
  console.log('公布成绩:', row);
};

const publishAnswers = (row) => {
  console.log('公布答案:', row);
};

const editHomework = (row) => {
  router.push({
    path: '/tea-end/course/examine/assign-homework',
    query: { id: row.id }
  });
};

const deleteHomework = (row) => {
  console.log('删除作业:', row);
};

const goToAssignHomework = () => {
  router.push({ path: '/tea-end/course/examine/assign-homework' });
};

const goToSubmissonCondition = (row) => {
  router.push({
    path: '/tea-end/course/examine/grade-homework',
    query: {
      title: row.title,
      startTime: row.startTime,
      endTime: row.endTime
    }
  });
};

const goToHomeworkStatistics = (row) => {
  router.push({ path: '/tea-end/course/examine/homework-statistics' });
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

.box-card {
  margin: 20px;
  margin-top: -2px;
}
.el-button {
  font-size: 14px;
  padding: 8px 10px;
}
.search-input {
  flex: 1;
  width: 300px;
  margin-left: 20px;
}
.gray-text {
  color: gray;
  font-size: 12px;
}

.custom-icon {
  font-size: 18px;
}
</style>