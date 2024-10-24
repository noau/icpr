<template>
    <el-dialog :modelValue="dialogVisible" title="上传头像" width="340">
        <div style="text-align: center;">
            <el-upload class="avatar-uploader" action="http://localhost:8080/attachment/upload" :show-file-list="false"
                :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload" :headers="headers"
                style="width: 300px; margin:0 auto">
                <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">
                    <Plus />
                </el-icon>
            </el-upload>
            <el-button type="primary" @click="submitForm" style="margin:30px 0">
                确定
            </el-button>
        </div>

    </el-dialog>
</template>

<script lang="ts" setup>
import { ref, defineProps, reactive } from 'vue'
const props = defineProps({
    dialogVisible: {
        type: Boolean,
    }
})
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { userFolders, userUserInfo, userCreate_favorite, setAvatar } from '@/api/user.js'
import { useUserStore } from '@/stores/user.js'
const user = useUserStore();
const headers = {
    'Authorization': localStorage.getItem('token')
}
let userId = useUserStore()?.id;
import type { UploadProps } from 'element-plus'

const imageUrl = ref('')

const avatar = ref('')

const handleAvatarSuccess = (res, file) => {
    console.log(res);
    avatar.value = res.url

    // imageUrl.value = res.url
    imageUrl.value = URL.createObjectURL(file.raw)
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
    if (rawFile.type !== 'image/jpeg') {
        ElMessage.error('Avatar picture must be JPG format!')
        return false
    } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error('Avatar picture size can not exceed 2MB!')
        return false
    }
    return true
}

// 上传接口
const submitForm = async () => {
    userId=localStorage.getItem('id')
    const res = await setAvatar({ id: userId,avatar: avatar.value })
}

</script>
<style scoped>
.itemBlock {
    padding: 10px 0;
    width: 100%;
    border-bottom: 1px solid #ddd;
}
</style>