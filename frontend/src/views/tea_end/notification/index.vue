<template>
  <div class="common_layout">
    <el-container style="min-height: 100vh;">
      <el-header class="common-header">
        <notification_header />
      </el-header>
      <el-container style="margin-top: 20px">
        <el-aside width="200px" style="z-index: 5;">
          <sidebar @update:filterStatus="filterStatus = $event" />
        </el-aside>
        <el-main>
          <div class="main-content">
            <router-view
                :filter-status="filterStatus"
                :search-text="searchText"
                :sort-option="sortOption"
            />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script>
import notification_header from "./components/notification_header.vue";
import sidebar from './components/sidebar.vue';

export default {
  data() {
    return {
      filterStatus: 'all', // 初始化状态
      searchText: '', // 搜索文本
      sortOption: 'date' // 排序选项
    };
  },
  components: {
    notification_header,
    sidebar,
  }
};
</script>

<style scoped>
.common_layout {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
.common-header {
  z-index: 10;
  position: relative;
  padding-left: 0;
  padding-right: 0;
}
.main-content {
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
el-header {
  position: relative;
}
el-aside {
  position: relative;
}
</style>
