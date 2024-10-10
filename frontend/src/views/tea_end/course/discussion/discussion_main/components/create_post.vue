<template>
    <div class="create-post">
      <el-button type="primary" @click="goToCreatePost">
        <el-icon><Edit /></el-icon>
        发起讨论
      </el-button>
    </div>
  </template>
  
  <script setup>
  import { Edit } from '@element-plus/icons-vue'; // 导入Edit图标
  import { useRouter } from 'vue-router'; // 使用路由进行跳转
  
  const router = useRouter(); // 初始化路由
  
  // 跳转到发起讨论的页面
  const goToCreatePost = () => {
    router.push('/tea-end/course/discussion/create-post');
  };
  </script>
  
  <style scoped>
  .create-post {
    display: flex;
    justify-content: center;
    margin: 20px 0;
  }
  
  .el-button {
    display: flex;
    align-items: center;
  }
  
  .el-icon {
    margin-right: 5px;
  }
  </style>
  

