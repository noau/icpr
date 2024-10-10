<template>
  <el-header>
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
const router = useRouter();
const return_back = () => {
  router.push('/stu-end');
};
</script>

<style scoped>
.el-header {
  background-color: #07395f; /* 自定义头部背景色 */
  display: flex;
  justify-content: flex-start; /* 左对齐内容 */
  align-items: center;
  padding: 0 20px;
  height: 60px;
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
