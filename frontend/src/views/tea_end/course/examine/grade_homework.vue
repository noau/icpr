<template>
  <div>
    <el-card shadow="always" class="header-card">
      <el-row :gutter="20" class="header-content">
        <el-col :span="12" class="left-section">
          <h3>{{ homeworkTitle }}</h3>
          <p>提交起止时间：{{ startTime }} - {{ endTime }}</p>
        </el-col>
        <el-col :span="12" class="right-section">
          <el-row :gutter="20" class="grade-homework">
            <el-col :span="8">
              <el-card shadow="hover" class="status-box">
                <div>应提交</div>
                <div class="status-number" style="color: #07395f;">{{ submitTotal }}</div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" class="status-box">
                <div>已提交</div>
                <div class="status-number" style="color: darkgreen;">{{ submitted }}</div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" class="status-box">
                <div>未提交</div>
                <div class="status-number" style="color: #780202da;">{{ submitTotal - submitted }}</div>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card">
      <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto;">
        <el-table :data="paginatedData" border style="width: 100%" class="resource-table">
          <el-table-column prop="studentId" label="学号" width="130" align="center" header-align="center" />
          <el-table-column prop="name" label="姓名" width="130" align="center" header-align="center" />
          <!-- <el-table-column prop="class" label="班级" width="130" align="center" header-align="center" /> -->
          <!-- <el-table-column prop="email" label="邮箱" width="200" align="center" header-align="center" /> -->
          <!-- <el-table-column prop="submitTime" label="提交时间" align="center" header-align="center" /> -->
          <!-- <el-table-column prop="finalScore" label="成绩" width="130" align="center" header-align="center" /> -->
          <el-table-column prop="submittedAt" label="提交时间" align="center" header-align="center" />
            <!-- 直接判断是否需要显示“是否完成互评”列 -->
          <el-table-column v-if="requirePeerReview" label="是否完成互评" width="130" align="center" header-align="center">
            <template #default="scope">
              {{ scope.row.peerReviewFinished ? '已完成' : '未完成' }}
            </template>
          </el-table-column>
          <el-table-column prop="grade" label="成绩" width="130" align="center" header-align="center" />
          <el-table-column label="操作" width="180" align="center" header-align="center">
            <template #default="scope">
              <el-button round @click="openReviewPage(scope.row)" type="text" size="small">批阅</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="pagination-container">
        <el-pagination layout="prev, pager, next" :total="filteredData.length" :page-size="pageSize"
          :current-page="currentPage" @current-change="handlePageChange" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { getReviewList } from '@/api/assignments.js';

const router = useRouter();
const route = useRoute();

const requirePeerReview = ref(false) // 是否需要互评

const homeworkTitle = ref(route.query.title || '作业标题');
const startTime = ref(route.query.start || '');
const endTime = ref(route.query.end || '');
const submittedData = ref({})
const submitted = ref(route.query.submitted || 0);
const submitTotal = ref(route.query.submitTotal || 0);

const goBack = () => {
  router.back();
};

const tableData = ref([]);
const pageSize = ref(8);
const currentPage = ref(1);
const filterType = ref('all');

const init = async () => {
  try {
    const res = await getReviewList({ id: route.query.id });
    
    tableData.value = res.submissions || [];
    console.log(res);
    
    submittedData.value = res || {}
    requirePeerReview.value = res.requirePeerReview
  } catch (error) {
    console.error("获取批改作业列表失败:", error);
  }
};

onMounted(init);

// const submittedCount = computed(() => tableData.value.filter(item => item.submitted).length);
// const notSubmittedCount = computed(() => tableData.value.length - submittedCount.value);

const filteredData = computed(() => {
  if (filterType.value === 'submitted') {
    return tableData.value.filter(item => item.submitted);
  } else if (filterType.value === 'notSubmitted') {
    return tableData.value.filter(item => !item.submitted);
  } else {
    return tableData.value;
  }
});

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredData.value.slice(start, end);
});

const handlePageChange = (page) => {
  currentPage.value = page;
};

// const filterSubmitted = (type) => {
//   filterType.value = type;
//   currentPage.value = 1;
// };

// 打开批阅页面
const openReviewPage = (row) => {
  // console.log('Opening review page for:', row);
  // return
  router.push({
    path: '/tea-end/course/examine/rating-page',
    query: {
      id: row.id, // 提交的作业ID
      assignmentId:row.assignmentId,
      submissionId:route.query.id,
      title: homeworkTitle.value,
      startTime: startTime.value,
      endTime: endTime.value
    }
  });
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
  background-color: #ffffff;
}

.right-section {
  margin-top: -40px;
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
