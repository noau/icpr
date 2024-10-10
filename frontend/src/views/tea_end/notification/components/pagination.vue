<template>
    <div>
      <el-pagination 
        @size-change="handleSizeChange" 
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next" 
        :total="totalNotifications" 
      />
  
      
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        currentPage: 1, // 当前页码
        pageSize: 10, // 每页显示的记录数
        totalNotifications: 100 // 假设有100条通知数据
      };
    },
    methods: {
      handleSizeChange(size) {
        this.pageSize = size;
        console.log("Page size changed to:", size);
        // 在这里可以根据新的 pageSize 加载相应的数据
      },
      handleCurrentChange(page) {
        this.currentPage = page;
        console.log("Current page changed to:", page);
        // 在这里可以加载当前页的数据
      }
    }
  };
  </script>
  
  <style scoped>

  
  .el-pagination {
    margin-top: 10px;
  }
  </style>
  
