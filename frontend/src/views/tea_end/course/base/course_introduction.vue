<template>
  <div class="course-container"> <!-- 添加根容器 -->
    <div class="course-header">
      <div class="course-name">课程简介</div>
    </div>

    <el-card class="course-card">
      <div class="course-info">
        <div>{{ courseIntroduction }}</div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getCourseInfo } from '@/api/course'; // 引入获取课程信息的API方法

// 定义存储课程简介的变量
const courseIntroduction = ref('加载中...');

// 获取课程简介的函数
const fetchCourseIntroduction = async () => {
  const token = localStorage.getItem('token'); // 从localStorage获取token
  let courseId = localStorage.getItem('courseId') // 获取课程ID

  if (!token || !courseId) {
    console.error("Token或课程ID不存在，请检查");
    return;
  }

  try {
    // 调用API获取课程信息
    const response = await getCourseInfo(courseId, token);
    console.log(response);

    const courseData = response;
    // 直接显示课程的简介内容
    courseIntroduction.value = courseData.introduction || '暂无课程简介';

  } catch (error) {
    console.error("获取课程简介失败:", error);
    courseIntroduction.value = '加载课程简介失败';
  }
};

// 使用onMounted在页面加载时获取课程简介
onMounted(() => {
  fetchCourseIntroduction();
});
</script>

<style scoped>
.course-card {
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 10px;
}

.course-header {
  display: flex;
  align-items: center;
  justify-content: space-between; 
  margin-bottom: 10px;
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
