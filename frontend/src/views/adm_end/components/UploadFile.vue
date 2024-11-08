<template>
  <div class="action-container">
    <!-- 文件上传 -->
    <el-upload
      class="upload"
      accept=".csv, .xlsx, .xls"
      action=""
      :before-upload="beforeUpload"
      :http-request="uploadFile"
      :file-list="fileList"
      :show-file-list="true"
      ref="uploadRef"
    >
      <el-button type="primary" @click="handleUpload">上传文件</el-button>
    </el-upload>
  </div>
</template>

<script setup>
import { ref, defineProps } from "vue";
import {
  ElUpload,
  ElButton,
  ElTable,
  ElTableColumn,
  ElMessage,
} from "element-plus";

const fileList = ref([]);
const uploadRef = ref(null);

const props = defineProps({
  uploadPost: {
    type: Function,
    default: () => {},
  },
});

// 拦截上传，防止自动上传到服务器
const beforeUpload = (file) => {
  if (
    file.type !==
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
  ) {
    ElMessage.error("请上传Excel文件");
    return false;
  }
  return true;
};

const handleUpload = () => {
  if (uploadRef.value) {
    uploadRef.value.submit();
  }
};

const uploadFile = async (options) => {
  const { file, onSuccess, onError } = options;

  // 模拟上传逻辑（替换为你的实际上传逻辑）
  const formData = new FormData();
  formData.append("file", file);

  // 使用 fetch 或 axios 发送请求
  const data = await props.uploadPost(formData);
  ElMessage.info("上传成功");
};
</script>
<style scoped>
/* 上传按钮和操作按钮的容器 */
.action-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
  width: 100%;
}
</style>
