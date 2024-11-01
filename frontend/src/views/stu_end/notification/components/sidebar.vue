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
  </el-menu>
</template>

<script>
export default {
  methods: {
    handleSelect(index) {
      console.log('Selected:', index);
      if (index === '1') {
        this.$router.push('/stu-end/notification/list'); // 跳转到通知列表页面
        this.$emit('update:filterStatus', 'all'); // 显示所有通知
      } else if (index === '2') {
        this.$router.push('/stu-end/notification/collection'); // 跳转到收藏页面
        this.$emit('update:filterStatus', 'starred'); // 显示收藏的通知
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
