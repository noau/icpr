<template>
  <el-row :gutter="20">
    <el-col :span="6" v-for="(course, index) in courses" :key="index">
      <el-card class="course-card" @click="openCourse(course)">
        <div class="course-header">
          <el-icon><Notebook /></el-icon>
          <div class="course-name">{{ course.name }}</div>
        </div>
        <div class="course-info">课程号: {{ course.courseNumber }}</div>
        <div class="course-info">班级号: {{ course.classNumber }}</div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import { Notebook } from '@element-plus/icons-vue';

export default {
  props: {
    courses: {
      type: Array,
      required: true,
    },
  },
  components: {
    Notebook,
  },
  methods: {
    openCourse(course) {
      // 存储课程ID到本地存储

      localStorage.setItem("courseId", course.id)

      // 跳转到课程主页
      this.$router.push({ path: '/stu-end/course', query: { courseId: course.id } });
    },
  },
};
</script>

<style scoped>
.course-card {
  width: 100%;          /* 使卡片占满列的宽度 */
  height: 120px;        /* 调整卡片的高度 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 10px;
  /* box-shadow: none;    */
}

.course-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px; /* 增加下方信息的间距 */
}

.course-name {
  font-size: 16px;
  font-weight: bold;
  margin-left: 10px; /* 增加图标和文字之间的间距 */
}

.course-info {
  font-size: 14px;
  color: gray;
}
</style>
