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
        <el-icon><Bell /></el-icon>
        作业通知
      </el-menu-item>
      <el-menu-item index="3-2">
        <el-icon><ChatDotRound /></el-icon>
        讨论区通知
      </el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>


<script>
import { MessageBox, Management, Filter, Bell, ChatDotRound } from '@element-plus/icons-vue';

export default {
  components: {ChatDotRound, Bell, Filter, MessageBox, Management},
  methods: {
    handleSelect(index) {
      console.log('Selected:', index);
      if (index === '1') {
        this.$router.push('/stu-end/notification/list'); // 跳转到通知列表页面
        this.$emit('update:filterStatus', 'all'); // 显示所有通知
      } else if (index === '2') {
        this.$router.push('/stu-end/notification/collection'); // 跳转到收藏页面
        this.$emit('update:filterStatus', 'starred'); // 显示收藏的通知
      } else if (index === '3-1') {
        this.$router.push('/stu-end/notification/type/assignment'); // 跳转到通知列表页面
        this.$emit('update:filterStatus', 'homework'); // 显示作业通知
      } else if (index === '3-2') {
        this.$router.push('/stu-end/notification/type/comment'); // 跳转到通知列表页面
        this.$emit('update:filterStatus', 'comment'); // 显示评论通知
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
