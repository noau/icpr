<template> 
  <el-dialog 
    :modelValue="dialogVisible"
    title="创建收藏夹"
    width="500"
    @close="cancel" 
  >
    <el-form
      ref="ruleFormRef"
      :model="ruleForm"
      status-icon 
      label-width="auto"
      class="demo-ruleForm"
    >
      <el-form-item label="收藏夹名称" prop="name" :rules="[{ required: true, message: '请输入收藏夹名称', trigger: 'blur' }]">
        <el-input v-model="ruleForm.name" placeholder="请输入" />
      </el-form-item>
      
      <el-form-item label="是否公开" prop="isPrivate">
        <el-radio-group v-model="ruleForm.isPrivate">
          <el-radio :value="1">公开</el-radio>
          <el-radio :value="0">保密</el-radio> 
        </el-radio-group>
      </el-form-item>  
      
      <el-form-item>
        <el-button type="primary" @click="submitForm">
          确定
        </el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus' 
import { userCreate_folder } from '@/api/user.js' 
import { useUserStore } from '@/stores/user.js'

// 获取用户 ID
const userId = localStorage.getItem('userId') || useUserStore()?.id;

const props = defineProps({
  dialogVisible: {
    type: Boolean,
    required: true
  }
});

const emits = defineEmits(['cancel', 'refresh']);  // 添加 'refresh' 用于通知父组件刷新收藏夹列表

const ruleForm = reactive({
  name: "",
  isPrivate: 1
});

const submitForm = async () => {
  if (!ruleForm.name) {
    ElMessage.error('收藏夹名称不能为空');
    return;
  }

  try {
    const createdAt = new Date().toISOString().slice(0, 19).replace('T', ' ');  // 动态生成当前时间

    // 调用创建收藏夹的 API
    await userCreate_folder({
      userId: parseInt(userId, 10),  // 确保 userId 是整数
      name: ruleForm.name,
      createdAt,
      isPrivate: ruleForm.isPrivate
    });

    ElMessage.success('收藏夹创建成功');
    emits('refresh');  // 通知父组件刷新收藏夹列表
    cancel();  // 成功后关闭弹窗
  } catch (error) {
    ElMessage.error('创建收藏夹失败，请稍后重试');
    console.error('创建收藏夹错误:', error.response?.data || error.message);
  }
};

const cancel = () => {
  emits('cancel');  // 关闭弹窗
};

</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
