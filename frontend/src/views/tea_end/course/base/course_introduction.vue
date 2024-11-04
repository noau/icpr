<template>
  <div class="course-header">
    <div class="course-name">课程简介</div>
    <div class="button-container">
      <el-button round type="text" @click="isEditing ? saveCourseIntroduction() : isEditing = true">
        {{ isEditing ? '保存' : '编辑' }}
      </el-button>
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
import { uploadCourseInfo } from '@/api/course'; // 引入上传课程简介的API

const isEditing = ref(false);
const courseIntroduction = ref('请输入课程简介');

// 定义课程简介上传的处理函数
const saveCourseIntroduction = () => {
  const token = localStorage.getItem('token'); // 获取用户的授权令牌
  if (!token) {
    console.error("Token 不存在，请登录");
    return;
  }

  // 构建FormData对象，用于文件上传
  const formData = new FormData();
  formData.append('file', new Blob([courseIntroduction.value], { type: 'text/plain' }), 'course-introduction.txt');

  // 调用上传课程简介的API
  uploadCourseInfo(formData, token)
    .then(response => {
      console.log("课程简介上传成功:", response);
      isEditing.value = false; // 保存成功后关闭编辑模式
    })
    .catch(error => {
      console.error("课程简介上传失败:", error);
    });
};

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