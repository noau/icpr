<template>
  <div class="admin-dashboard">
    <!-- 顶部栏 -->
    <el-header class="header">
      <admin_header />
    </el-header>
    <!-- 主体布局 -->
    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="sidebar">
        <AdminSidebar />
      </el-aside>
      <!-- 内容区域 -->
      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import admin_header from '@/components/adm_end/adm_header.vue';
import AdminSidebar from './components/admin_sidebar.vue';
</script>

<style scoped>
.admin-dashboard {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.header {
  height: 70px; /* 根据您的需要调整顶部栏高度 */
  line-height: 70px;
  padding: 0;
}

.main-container {
  position: absolute;
  top: 70px; /* 与顶部栏高度一致 */
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
}

.sidebar {
  width: 200px;
  background-color: #f5f5f5;
  padding: 0;
  margin: 0;
}

.content {
  flex: 1;
  padding: 20px;
}

.el-header,
.el-aside,
.el-main {
  margin: 0;
  padding: 0;
}

.el-container {
  margin: 0;
  padding: 0;
}
</style>
