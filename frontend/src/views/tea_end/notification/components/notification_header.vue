<template>
  <el-header class="notification-header">
    <div class="header-content">
      <!-- 返回按钮 -->
      <el-icon class="common-icon" @click="return_back">
        <ArrowLeftBold />
      </el-icon>
      <!-- 通知标题 -->
      <div class="title">通知</div>
    </div>
  </el-header>
</template>

<script setup>
import { useRouter } from 'vue-router';
import {ArrowLeftBold} from "@element-plus/icons-vue";
const router = useRouter();
const return_back = () => {
  router.push('/tea-end');
};
</script>


<style scoped>
.notification-header {
  background-color: #07395f; /* 自定义头部背景色 */
  display: flex;
  justify-content: flex-start; /* 左对齐内容 */
  align-items: center;
  height: 60px;
  width: 100%;
}

.header-content {
  display: flex;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #ffffff; /* 设置标题的颜色 */
  margin-left: 10px; /* 给返回按钮和标题之间留出一点间距 */
}

.common-icon {
  color: #ffffff; /* 设置图标颜色 */
  cursor: pointer;
}
</style>
