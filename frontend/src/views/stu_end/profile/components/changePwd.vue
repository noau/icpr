<template> 
  <el-dialog 
    :modelValue="dialogVisible"
    title="修改密码"
    width="500" 
  >
    <el-form
        ref="ruleFormRef"
        style="max-width: 600px"
        :model="ruleForm"
        status-icon
        :rules="rules"
        label-width="auto"
        class="demo-ruleForm"
    >
        <el-form-item label="旧密码" prop="password">
            <el-input v-model="ruleForm.password" type="password" placeholder="请输入旧密码" autocomplete="off" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
            <el-input
                v-model="ruleForm.newPassword"
                type="password"
                autocomplete="off"
                placeholder="请输入新密码"
            />
        </el-form-item> 
        <el-form-item label="确认密码" prop="newPassword1">
            <el-input
                v-model="ruleForm.newPassword1"
                type="password"
                placeholder="请输入新密码"
                autocomplete="off"
            />
        </el-form-item>
        <el-form-item label=" " >
            <el-button type="primary" @click="submitForm(ruleFormRef)">
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
import { userchange_password } from '@/api/user.js'   
import { useUserStore } from '@/stores/user.js' 
const props = defineProps({
    dialogVisible:{
        type:Boolean, 
    }
})
const emits = defineEmits(['cancel'])
const ruleForm = reactive({
    "id":  localStorage.getItem('userId'),
    "password": "123",
    "newPassword": "1234",
    newPassword1:"",
    
})
const handleClose = (done: () => void) => {
    
}
const submitForm = async()=>{
    // const res = await userchange_password(ruleForm)  
    userchange_password(ruleForm).then(res => {
        emits('cancel')

}).catch(err => {

})
}
const cancel = ()=>{
  emits('cancel')
}
</script>