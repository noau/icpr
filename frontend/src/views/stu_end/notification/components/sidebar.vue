<template>
    <el-menu default-active="1" class="el-menu-vertical-demo" @select="handleSelect">
      <el-menu-item index="1">
        <el-icon><MessageBox /></el-icon>
        信箱
      </el-menu-item>
      <el-menu-item index="2">
        <el-icon><Management /></el-icon>
        收藏
      </el-menu-item>
      <el-menu-item index="3">
        <el-icon><Check /></el-icon>
        已完成
      </el-menu-item>
    </el-menu>
  </template>
  
  <script>
  export default {
    methods: {
      handleSelect(index) {
        console.log('Selected:', index);
        if (index === '1') {
          this.$emit('update:filterStatus', 'all'); // 显示所有通知
        } else if (index === '2') {
          this.$emit('update:filterStatus', 'starred'); // 显示收藏的通知
        } else if (index === '3') {
          this.$emit('update:filterStatus', 'completed'); // 显示已完成的通知
        }
      }
    }
  }
  </script>
  
  
  <style scoped>
  .el-menu-vertical-demo {
    width: 200px;
    min-height: 100vh;
    border-right: 1px solid #e0e0e0;
  }
  </style>
  