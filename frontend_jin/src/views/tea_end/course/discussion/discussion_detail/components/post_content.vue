<template>
    <div class="post-content" v-html="content"></div>
  </template>
  
  <script setup>
  const props = defineProps({
    content: String,
  });
  </script>
  
  <style scoped>
  .post-content {
    font-size: 20px;
    line-height: 1.8;
    margin-bottom: 10px;
    max-width: 800px; /* 设置内容的最大宽度 */
  }
  </style>
  