<template>
  <div class="create-post-page">
    <!-- 标题和发布按钮区域 -->
    <div class="title-and-button">
      <!-- 标题输入组件 -->
      <TitleInput v-model="title" class="title-input" />
      <!-- <input v-model="top" type="checkbox" > 置顶 -->
      <!-- 发布按钮 -->
      <SubmitButton @submit="submitPost" class="submit-button" />
    </div>

    <!-- 中间布局区域 -->
    <div class="layout">
      <!-- 主内容区 -->
      <div class="main-content">
        <!-- 工具栏组件 -->
        <ContentEditor v-model="content" />
      </div>

      <!-- 右侧侧边栏 -->
      <div class="sidebar-content">
        <Sidebar v-model:summary="summary" v-model:tags="tags" v-model:visibility="visibility"
          v-model:isAnonymous="isAnonymous" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import TitleInput from './components/title_input.vue';
import ContentEditor from './components/content_editor.vue';
import Sidebar from './components/sidebar.vue';
import SubmitButton from './components/submit_button.vue';
import {getthread} from '@/api/discussion';
const title = ref('');
const content = ref('');
const summary = ref('');
const tags = ref('');
const top=ref(false)
const visibility = ref('全部可见');
const isAnonymous = ref(false);
import { useRouter } from 'vue-router'; // 使用路由进行跳转
import { ElMessage } from 'element-plus';
  
  const router = useRouter(); // 初始化路由
const submitPost = () => {
  // 提交逻辑
  console.log('标题：', title.value);
  console.log('内容：', content.value);
  console.log('摘要：', summary.value);
  console.log('标签：', tags.value);
  console.log('可见范围：', visibility.value);
  console.log('匿名1111：', isAnonymous.value);
  console.log('courseId', localStorage.getItem('courseId'));
  if (!title.value) {
    ElMessage.error('标题不能为空');
    return;
  }

  let obj = {

    courseId: localStorage.getItem('courseId'),
    userId: localStorage.getItem('id'),
    title: title.value,
    content: content.value,
    top: top.value ? 1 : 0,
    tag: tags.value,
    isAnonymous: isAnonymous.value ? 1 : 0
  }
  getthread(obj).then(res => {
    console.log(res);
    router.go(-1);

  })
  // 在这里添加你的提交逻辑，例如发送 API 请求
};
</script>

<style scoped>
.create-post-page {
  display: flex;
  flex-direction: column;
  padding: 20px;
  gap: 20px;
}

/* 调整后的样式 */
.title-and-button {
  display: flex;
  align-items: center;
  /* 垂直居中对齐 */
  gap: 10px;
}

.title-and-button .title-input {
  flex: 1;
  margin-bottom: 0;
  /* 移除下边距 */
}

.title-and-button .submit-button {
  margin-bottom: 0;
  /* 确保按钮没有下边距 */
}

/* 调整按钮的高度和行高 */
.title-and-button .submit-button .el-button {
  height: 40px;
  /* 设定一个与输入框匹配的高度 */
  line-height: 40px;
  /* 行高与高度一致 */
  padding: 0 20px;
  /* 调整内边距 */
}

.layout {
  display: flex;
  gap: 20px;
}

.main-content {
  flex: 3;
}

.sidebar-content {
  flex: 1;
}
</style>