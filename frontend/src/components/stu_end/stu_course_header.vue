<template>
  <el-header>
    <div class="left-section">
      <el-button type="text" class="back-button" @click="goBack">
        <el-icon><ArrowLeft /></el-icon> 返回首页
      </el-button>
      <!-- 自定义面包屑 -->
      <div class="custom-breadcrumb">
        <router-link
          v-for="(item, index) in breadcrumbItems"
          :key="index"
          :to="item.path"
          class="breadcrumb-item"
        >{{ item.name }}</router-link>
      </div>
    </div>
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
import { Bell, ArrowLeft, User } from '@element-plus/icons-vue';

export default {
  components: {
    Bell,
    ArrowLeft,
    User,
  },
  data() {
    return {
      breadcrumbItems: [], // 动态更新面包屑
    };
  },
  watch: {
    $route(to) {
      this.updateBreadcrumb(to);
    },
  },
  created() {
    this.updateBreadcrumb(this.$route);
  },
  methods: {
    goBack() {
      this.$router.push('/stu-end'); // 返回首页
    },
    goToNotifications() {
      this.$router.push('/stu-end/notification'); // 跳转到通知页面
    },
    goToProfile() {
      this.$router.push('/profile'); // 跳转到个人中心页面
    },
    updateBreadcrumb(route) {
      const matchedRoutes = route.matched.slice(1); // 删除第一级
      this.breadcrumbItems = matchedRoutes.map((r) => ({
        name: r.meta?.breadcrumbName || r.name,
        path: r.path,
      }));
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

.left-section {
  display: flex;
  align-items: center;
}

.back-button {
  color: #ffffff;
  display: flex;
  align-items: center;
  margin-right: 20px;
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

.custom-breadcrumb {
  display: flex;
  align-items: center;
}

.breadcrumb-item {
  color: white;
  text-decoration: none;
  position: relative;
  /* padding: 0 8px; */
}

.breadcrumb-item:hover {
  text-decoration: underline;
}

.breadcrumb-item:not(:last-child):after {
  content: "/";
  margin-left: 8px;
  color: #ccc;
}

.custom-breadcrumb .router-link-exact-active {
  font-weight: bold;
}
</style>