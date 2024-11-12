<template>
    <div>
      <el-card shadow="always" class="header-card">
        <el-row :gutter="20" class="header-content">
          <el-col :span="12" class="left-section">
            <h3>{{ homeworkTitle }}</h3>
            <p>提交起止时间：{{ startTime }} - {{ endTime }}</p>
          </el-col>
          <!-- <el-col :span="12" class="right-section">
            <el-row :gutter="20" class="grade-homework">
              <el-col :span="8">
                <el-card shadow="hover" class="status-box" @click="filterSubmitted('all')">
                  <div>应提交</div>
                  <div class="status-number" style="color: #07395f;">{{ tableData.length }}</div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover" class="status-box" @click="filterSubmitted('submitted')">
                  <div>已提交</div>
                  <div class="status-number" style="color: darkgreen;">{{ submittedCount }}</div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover" class="status-box" @click="filterSubmitted('notSubmitted')">
                  <div>未提交</div>
                  <div class="status-number" style="color: #780202da;">{{ notSubmittedCount }}</div>
                </el-card>
              </el-col>
            </el-row>
          </el-col> -->
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
  
  const homeworkTitle = ref(route.query.title || '作业标题');
  const startTime = ref(route.query.start || '');
  const endTime = ref(route.query.end || '');
  
  const goBack = () => {
    router.back();
  };
  
  const tableData = ref([]);
  const pageSize = ref(8);
  const currentPage = ref(1);
  const filterType = ref('all');
  
//   const init = async () => {
//     try {
//       const res = await getReviewList({ id: route.query.assignmentId });
//       tableData.value = res || [];
//     } catch (error) {
//       console.error("获取批改作业列表失败:", error);
//     }
//   };
/**
 * 打开页面初始化,调用peer-review-list互评列表接口渲染列表
 */
const init = async () => {
    try {
      const res = await getReviewList({ id: route.query.assignmentId });
      tableData.value = res || [];
    } catch (error) {
      console.error("获取批改作业列表失败:", error);
    }
  };
  
  onMounted(init);
  
  const submittedCount = computed(() => tableData.value.filter(item => item.submitted).length);
  const notSubmittedCount = computed(() => tableData.value.length - submittedCount.value);
  
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
  
  const filterSubmitted = (type) => {
    filterType.value = type;
    currentPage.value = 1;
  };
  
  // 打开批阅页面
  const openReviewPage = (row) => {
    // console.log('Opening review page for:', row);
    // return
    router.push({
      path: '/stu-end/course/examine/rating-page',
      query: {
        // id: row.id, 
        assignmentId:row.id,// 提交的作业ID
        studentId:row.studentId,
        submissionId:route.query.id,
        title: homeworkTitle.value,
        startTime: startTime.value,
        endTime: endTime.value,
        minPeerReview: row.minPeerReview
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
  