<template> 
  <el-dialog 
    :modelValue="dialogVisible"
    title="添加收藏夹"
    width="500" 
  >
    <el-form
        ref="ruleFormRef"
        style="max-width: 600px"
        :model="ruleForm"
        status-icon 
        label-width="auto"
        class="demo-ruleForm"
    >
        <el-form-item label="收藏夹名称" prop="name">
            <el-input v-model="ruleForm.name"   placeholder="请输入"   />
        </el-form-item>
        <el-form-item label=" " prop="checkPass">
            <el-radio-group v-model="ruleForm.isPrivate">
                <el-radio :value="1">公开</el-radio>
                <el-radio :value="0">保密</el-radio> 
            </el-radio-group>
        </el-form-item>  
        <el-form-item label=" " >
            <el-button type="primary" @click="submitForm">
                确定
            </el-button>
            <el-button @click="cancel">取消</el-button>
        </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
         
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref,defineProps,reactive } from 'vue'
import { ElMessageBox } from 'element-plus' 
import { userCreate_folder } from '@/api/user.js' 
import { useUserStore } from '@/stores/user.js' 
const userId = useUserStore()?.id;

const props = defineProps({
    dialogVisible:{
        type:Boolean, 
    }
})
const emits = defineEmits(['cancel'])
const ruleForm = reactive({
    "userId": localStorage.getItem('userId'),
    "name": "",
    "createdAt": "2024-08-24 07:48:31",
    "isPrivate": 1
})
const submitForm = async() => {
  const res = await userCreate_folder({ 
    "userId": localStorage.getItem('userId'),
    "name": ruleForm?.name,
    "createdAt": "2024-08-24 07:48:31",
    "isPrivate": ruleForm?.isPrivate
   })  
}
const cancel = ()=>{
  emits('cancel')
}
</script>