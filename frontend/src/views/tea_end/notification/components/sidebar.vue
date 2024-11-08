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
    <el-sub-menu index="3">
      <template #title>
        <el-icon><Filter /></el-icon>
        筛选条件
      </template>
      <el-menu-item index="3-1">
        <el-icon><ChatDotRound /></el-icon>
        讨论区通知
      </el-menu-item>
      <el-menu-item index="3-2">
        <el-icon><DataBoard /></el-icon>
        系统通知
      </el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script>
import {MessageBox, Management, Filter, Bell, ChatDotRound, DataBoard} from '@element-plus/icons-vue';

export default {
  components: {DataBoard, ChatDotRound, Bell, Filter, MessageBox, Management},
  methods: {
    handleSelect(index) {
      const menuMap = {
        '1': { route: '/tea-end/notification/list', filterStatus: 'all' },
        '2': { route: '/tea-end/notification/collection', filterStatus: 'starred' },
        '3-1': { route: '/tea-end/notification/type/comment', filterStatus: 'comment' },
        '3-2': { route: '/tea-end/notification/type/system', filterStatus: 'system' }
      };

      const selectedMenu = menuMap[index];
      if (selectedMenu) {
        this.$router.push(selectedMenu.route); // 跳转到相应的页面
        if (selectedMenu.filterStatus) {
          this.$emit('update:filterStatus', selectedMenu.filterStatus); // 更新通知状态
        }
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
