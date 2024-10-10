
<template>
    <div class="search-bar">
      <el-input
        v-model="query"
        placeholder="标题"
        style="width: 300px; height: 36px;"
      ></el-input>
      <el-button type="primary" @click="search">搜索</el-button>
      <el-button @click="reset">重置</el-button>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        query: '',
      };
    },
    methods: {
      search() {
        this.$emit('search', this.query);
      },
      reset() {
        this.query = '';
        this.$emit('search', '');
      },
      viewAll() {
        this.$emit('search', '');
      },
    },
  };
  </script>
  
  <style scoped>
  .search-bar {
    margin-bottom: 20px;
    display: flex;
    gap: 10px;
    align-items: center;
  }
  
  /* 优化按钮间距 */
  .el-button {
    flex-shrink: 0;
  }
  </style>