<!-- <template> 
  <el-card class="course-card">
    <div class="course-header">
      <div class="course-name">{{ courseName }}</div>
    </div>
    <div class="course-info">
      {{ courseIntroduction }}
    </div>
  </el-card>
</template> -->

<template>
  <el-card class="course-card">
    <div class="course-header">
      <div class="course-name">{{ courseInfo }}</div>
    </div>
    <div class="course-info">
      {{ courseInfo }}
    </div>
  </el-card>
</template>



<script setup>
import { userCourses } from '@/api/course';
import { ref, onMounted } from 'vue';

const courseInfo = ref(null);

const fetchCourseInfo = async () => {
  const userId = localStorage.getItem('userId'); // 确保 userId 是用户 ID
  const token = localStorage.getItem('token');

  if (!userId) {
    console.error("用户 ID 不存在，请检查 'userId' 是否存储在 localStorage 中");
    return;
  }

  if (!token) {
    console.error("Token 不存在，请检查 'token' 是否存储在 localStorage 中");
    return;
  }

  try {
    const response = await userCourses(userId, token);
    courseInfo.value = response.data; // 假设后端返回的课程信息数据在 `response.data` 中
    console.log("课程信息：", courseInfo.value);
  } catch (error) {
    console.error("获取课程信息失败:", error);
  }
};

// 在组件挂载时调用 fetchCourseInfo
onMounted(() => {
  fetchCourseInfo();
});

</script>

<style scoped>
.course-card {
  width: 100%;          /* 使卡片占满列的宽度 */
  height: auto;         /* 高度根据内容自动调整 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 10px;
}

.course-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px; /* 增加下方信息的间距 */
}

.course-name {
  font-size: 16px;
  font-weight: bold;
}

.course-info {
  font-size: 14px;
  color: gray;
}
</style>
