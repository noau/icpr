<template>
  <div class="course-header">
      <div class="course-name">课程简介</div>
    <div class="button-container">
      <el-button round type="text" @click="isEditing = !isEditing">{{ isEditing ? '保存' : '编辑' }}</el-button>
    </div>
  </div>
  
  <el-card class="course-card">
    
    <div class="course-info">
      <el-input
        v-if="isEditing"
        type="textarea"
        v-model="courseIntroduction"
        placeholder="请输入课程简介"
        rows="5"
      ></el-input>
      <div v-else>{{ courseIntroduction }}</div>
    </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue';

const isEditing = ref(false);
const courseIntroduction = ref('请输入课程简介');

// function saveCourseIntroduction() {
//   console.log('课程介绍已保存:', courseIntroduction.value);
//   isEditing.value = false;
// }
</script>

<style scoped>
.course-card {
  width: 100%;
  height: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 10px;
}

.course-header {
  display: flex;
  align-items: center;
  justify-content: space-between; 
  margin-bottom: 10px;
}

.course-name {
  font-size: 16px;
  font-weight: bold;
}

.course-info {
  font-size: 14px;
  color: gray;
}

.button-container {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 5px;
}
</style>