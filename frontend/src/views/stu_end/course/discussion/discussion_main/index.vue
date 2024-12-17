<template>
    <div>
      <!-- 搜索框和发起讨论按钮的容器 -->
      <div class="toolbar">
        <div class="search-container">
          <!-- 搜索框 -->
          <SearchBar @search="handleSearch" />
        </div>
  
        <div class="create-post-container">
          <!-- 发起讨论按钮 -->
          <create_post />
        </div>
      </div>
      
      <!-- 帖子列表 -->
      <PostList :search="search" />
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  // 引入子组件
  import SearchBar from './components/search_bar.vue';
  import PostList from './components/discussion_list.vue';
  import create_post from './components/create_post.vue';
  const search = ref('')
  function handleSearch(query) {
  search.value = query;  // 接收子组件传递的参数
}
  </script>
  
  <style scoped>
  /* 定义搜索框和发起讨论按钮的布局 */
  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px; /* 下方留出一定的空间 */
  }
  

  .search-container {
  flex: 1;
}
  
  .create-post-container {
    margin-left: 40px; /* 或者可以尝试更大的值 */
  }
  </style>
  