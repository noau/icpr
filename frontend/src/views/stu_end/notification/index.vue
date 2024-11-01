<template>
  <div class="common_layout">
    <el-container style="min-height: 100vh;">
      <el-header style="z-index: 10;">
        <notification_header />
      </el-header>
      <el-container style="margin-top: 20px">
        <el-aside width="200px" style="z-index: 5;">
          <sidebar @update:filterStatus="filterStatus = $event" />
        </el-aside>
        <el-main>
          <div class="main-content">
            <div class="search-filter-container">
              <read_status_filter v-model="filterStatus" class="filter-left" />
              <top_searchbar
                  v-model:searchText="searchText"
                  v-model:sort-option="sortOption"
                  class="search-bar"
              />
            </div>
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
import top_searchbar from './components/top_searchbar.vue';
import read_status_filter from './components/read_status_filter.vue';

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
    top_searchbar,
    read_status_filter,
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
    padding: 0px;
  }

  .main-content {
    padding: 20px;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }

  .search-filter-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
  }

  .filter-left {
    margin-right: 20px;
    flex-grow: 0;
  }

  .search-bar {
    flex-grow: 2;
  }

  el-header {
    position: relative;
    z-index: 10;
  }

  el-aside {
    position: relative;
    z-index: 5;
  }
  </style>
