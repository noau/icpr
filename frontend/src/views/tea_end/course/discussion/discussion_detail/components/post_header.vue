<template>
    <div class="post-header">
      <div class="author-info">
        <el-avatar :src="post.avatar" />
        <span class="author-name">{{ post.name }}</span>
      </div>
      <h1 class="post-title">{{ post.title }}</h1>
    </div>
  </template>
  
  <script setup>
  const props = defineProps({
    post: Object,
  });
  </script>
  
  <style scoped>
  /* .post-header {
    border-bottom: 1px solid #eaeaea;
    padding-bottom: 10px;
    margin-bottom: 20px;
  } */
  .post-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 左对齐 */
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

  
  .author-info {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
  }

  .author-name {
    margin-left: 10px;
    font-weight: bold;
  }
  
  .post-title {
    margin: 0;
    font-size: 30px; /* 设置标题字体大小 */
  }
  </style>
  