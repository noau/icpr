<template>
    <el-card class="course-card">
      <div class="course-header">
        <div class="course-name">课程名</div>
      </div>
      <div class="course-info">
        教学大纲的详细内容
      </div>
    </el-card>
  </template>
  
  <script setup>
  import { Notebook } from '@element-plus/icons-vue';
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
  