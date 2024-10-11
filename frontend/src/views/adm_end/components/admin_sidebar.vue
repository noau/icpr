<template>
  <el-menu
    :default-active="currentIndex"
    class="admin-sidebar"
    :collapse="false"
    @select="handleSelect"
  >
    <!-- 学生信息 -->
    <el-menu-item index="stu" :class="{ active: currentIndex === 'stu' }">
      <el-icon><User /></el-icon>
      <span>学生信息</span>
    </el-menu-item>

    <!-- 教师信息 -->
    <el-menu-item index="tea" :class="{ active: currentIndex === 'tea' }">
      <el-icon><UserFilled /></el-icon>
      <span>教师信息</span>
    </el-menu-item>

    <!-- 课程信息 -->
    <el-menu-item index="cou" :class="{ active: currentIndex === 'cou' }">
      <el-icon><Notebook /></el-icon>
      <span>课程信息</span>
    </el-menu-item>

    <!-- 选课信息 -->
    <el-menu-item index="sc" :class="{ active: currentIndex === 'sc' }">
      <el-icon><Document /></el-icon>
      <span>选课信息</span>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const currentIndex = ref('stu'); // 默认选中的菜单项

const handleSelect = (key) => {
  currentIndex.value = key;
  switch (key) {
    case 'stu':
      router.push('/adm-end/stu/');
      break;
    case 'tea':
      router.push('/adm-end/tea/');
      break;
    case 'cou':
      router.push('/adm-end/cou/');
      break;
    case 'sc':
      router.push('/adm-end/sc/');
      break;
  }
};
</script>

<style scoped>
.admin-sidebar {
  width: 200px;
  height: 90vh; /* 确保高度合适 */
  border-right: 1px solid #ebeef5;
  padding: 0;
  margin: 0;
}

.el-menu-item {
  height: 56px;
  line-height: 56px;
  padding: 0 20px;
  color: #333; /* 设置默认文字颜色 */
}

.el-menu-item.is-active {
  /* background-color: #07395f; */ /* 移除背景颜色 */
  color: #07395f; /* 设置激活状态下的文字颜色 */
}

.active {
  font-weight: bold;
}

.el-icon {
  margin-right: 8px;
}
</style>
