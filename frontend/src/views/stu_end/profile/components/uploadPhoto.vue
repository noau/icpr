<template>
  <el-dialog
      :modelValue="dialogVisible"
      title="上传头像"
      width="340"
  >
    <div style="text-align: center;">
      <el-upload
          class="avatar-uploader"
          action="http://localhost:8080/attachment/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :headers="headers"
          style="width: 300px; margin:0 auto"
      >
        <img v-if="imageUrl" :src="imageUrl" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
      <el-button type="primary" @click="submitForm" style="margin:30px 0">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, defineProps } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { setAvatar } from '@/api/user.js';
import { useUserStore } from '@/stores/user.js';
import type { UploadProps } from 'element-plus';

const props = defineProps({
  dialogVisible: {
    type: Boolean,
  },
});

const user = useUserStore();
const headers = {
  'Authorization': user?.token,
};
const userId = user?.id;

const imageUrl = ref(''); // 用于存储服务器返回的图片 URL
let uploadedUrl = ''; // 用于存储上传后端返回的最终图片 URL

// 修改后的 handleAvatarSuccess 函数
const handleAvatarSuccess: UploadProps['onSuccess'] = (response) => {
  // 从后端响应中获取图片的 URL
  uploadedUrl = response.url; // 假设后端返回的数据中有 `url` 字段
  imageUrl.value = response.url;
};

// 上传前校验
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('Avatar picture must be JPG format!');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!');
    return false;
  }
  return true;
};

// 上传接口
const submitForm = async () => {
  if (!uploadedUrl) {
    ElMessage.error('请先上传头像！');
    return;
  }
  console.log("触发上传头像接口")
  console.log(uploadedUrl)
  try {
    const res = await setAvatar({ id: userId, avatar: uploadedUrl });
    console.log(res.status);
    if (res.status == 200) {
      console.log("123456789098765");
      ElMessage.success('头像上传成功');
      props.dialogVisible = false;
    }
  } catch (error) {
    console.log(error)
    ElMessage.error('头像上传失败');
  }
};
</script>

<style scoped>
.itemBlock {
  padding: 10px 0;
  width: 100%;
  border-bottom: 1px solid #ddd;
}
</style>
