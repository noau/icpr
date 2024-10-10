<template>
  <el-menu
    default-active="2"
    class="el-menu-vertical-demo"
    :collapse="false"
    @open="handleOpen"
    @close="handleClose"
    @select="handleSelect"
  >
    <el-sub-menu index="1">
      <template #title>
        <el-icon><InfoFilled /></el-icon>
        <span> 基础信息</span>
      </template>
      <el-menu-item :class="{ active: currentIndex === '1-1' }" index="1-1">课程介绍</el-menu-item>
      <el-menu-item :class="{ active: currentIndex === '1-2' }" index="1-2">教学大纲</el-menu-item>
      <el-menu-item :class="{ active: currentIndex === '1-3' }" index="1-3">教学日历</el-menu-item>
      <el-menu-item :class="{ active: currentIndex === '1-4' }" index="1-4">教师信息</el-menu-item>
    </el-sub-menu>

    <el-sub-menu index="2">
      <template #title>
        <el-icon><FolderOpened /></el-icon>
        <span> 课程资源</span>
      </template>
      <el-menu-item :class="{ active: currentIndex === '2-1' }" index="2-1">课件</el-menu-item>
      <el-menu-item :class="{ active: currentIndex === '2-2' }" index="2-2">历年试题库</el-menu-item>
      <el-menu-item :class="{ active: currentIndex === '2-3' }" index="2-3">习题库</el-menu-item>
    </el-sub-menu>

    <el-sub-menu index="3">
      <template #title>
        <el-icon><Document /></el-icon>
        <span>课程考核</span>
      </template>
      <el-menu-item :class="{ active: currentIndex === '3-1' }" index="3-1">作业</el-menu-item>
    </el-sub-menu>

    <el-menu-item :class="{ active: currentIndex === '4' }" index="4">
      <el-icon><ChatLineRound /></el-icon>
      <template #title>答疑讨论</template>
    </el-menu-item>
    <el-menu-item :class="{ active: currentIndex === '5' }" index="5">
      <img src="@/assets/img/chat.jpg" alt="ChatGPT" class="menu-icon">
      <template #title>AI助手</template>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

// const isTeacher = ref(true) // 根据实际逻辑设置用户角色
const router = useRouter()
const currentIndex = ref('2') // 当前选中的菜单项

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}

const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}

// 处理菜单选择，跳转到对应路由并更新当前选中的菜单项
const handleSelect = (key) => {
  currentIndex.value = key
  switch (key) {
    case '1-1':
      router.push('/stu-end/course/base/course-introduction')
      break
    case '1-2':
      router.push('/stu-end/course/base/teaching-outline')
      break
    case '1-3':
      router.push('/stu-end/course/base/teaching-calendar')
      break
    case '1-4':
      router.push('/stu-end/course/base/teacher-info')
      break
    case '2-1':
      router.push('/stu-end/course/resource/slides')
      break
    case '2-2':
      router.push('/stu-end/course/resource/old-exam')
      break
    case '2-3':
      router.push('/stu-end/course/resource/exercise')
      break
    case '3-1':
      router.push('/stu-end/course/examine/stu-homework')
      break
    case '4':
      router.push('/stu-end/course/discussion')
      break  
  }
}
</script>

<style>
.el-menu-vertical-demo {
  width: 200px;
  min-height: 400px;
}

.menu-icon {
  width: 18px;
  height: 18px;
  margin-left: 3px;
  margin-right: 8px;
}

.active {
  font-weight: bold;
}
</style>