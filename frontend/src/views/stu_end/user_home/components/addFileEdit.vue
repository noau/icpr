<template>
  <el-dialog :modelValue="dialogVisible" title="编辑收藏夹" width="500">
    <el-form ref="ruleFormRef" style="max-width: 600px" :model="ruleForm" status-icon label-width="auto"
      class="demo-ruleForm">
      <el-form-item label="收藏夹名称" prop="name">
        <el-input v-model="obj.name" placeholder="请输入" />
      </el-form-item>
      <el-form-item label=" " prop="checkPass">
        <el-radio-group v-model="obj.isPrivate">
          <el-radio :value="1">公开</el-radio>
          <el-radio :value="0">保密</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label=" ">
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

<script setup>
import { ref, defineProps, reactive } from 'vue'
import { ElMessageBox } from 'element-plus'
import { userCreate_folder, userChange_folder } from '@/api/user.js'
import { useUserStore } from '@/stores/user.js'
const userId = useUserStore()?.id;

const props = defineProps({
  dialogVisible: {
    type: Boolean,
  },
  folder: {
    type: Object,
  }
})
const obj = ref({
  name: '',
  isPrivate: 1
})


// const folder = ref({}) 
// folder.value=JSON.parse(localStorage.getItem('folder'))
const emits = defineEmits(['cancel'])

const ruleForm = reactive({
  "userId": localStorage.getItem('userId'),
  "name": props.folder?.name,
  "isPrivate": 1
})

const submitForm = async () => {
  const res = await userChange_folder({
    id: props.folder?.id,
    name: obj.value.name,
    isPrivate: obj.value.isPrivate
  })
  
  emits('cancel')
}
const cancel = () => {
  emits('cancel')
}
</script>