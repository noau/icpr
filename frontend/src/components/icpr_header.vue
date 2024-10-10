s<template>
  <el-header>
    <div class="title">重生的智慧课程平台</div>
    <div class="icons">
        <el-badge :value="2" class="item">
          <el-icon class="common-icon" style="margin-right: -2px;"><Bell /></el-icon>
          <el-button type="text" @click="goToNotifications" class="text-button">通知</el-button>
        </el-badge>
        <el-icon class="common-icon" style="margin-left: 5px; margin-right: -7px;"><User /></el-icon>
        <el-button type="text" @click="goToProfile" class="text-button">个人中心</el-button>
      </div>
  </el-header>
</template>

<script>
  import { Bell, User } from '@element-plus/icons-vue';

export default {
  components: {
    Bell,
    User,
  },
  methods: {
    goToNotifications() {
      // 跳转到通知页面
      this.$router.push('/notification');
    },
    goToProfile() {
      // 跳转到个人中心页面
      this.$router.push('/profile');
    },
  },
};
</script>

<style scoped>
.el-header {
  background-color: #07395f; /* 自定义头部背景色 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
  margin-left: -20px;
  margin-right: -20px;
  margin-top: -20px;
}


.title {
  font-size: 18px;
  font-weight: bold;
  color: #ffffff; /* 设置标题的颜色 */
}

.text-button {
  color: #ffffff; /* 设置按钮文本的颜色 */
}

.common-icon {
  color: #ffffff; /* 设置图标颜色 */
}

.icons {
  display: flex;
  align-items: center;
  gap: 10px;
}

.item {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>
