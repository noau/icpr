<template>
  <div class="course-header">
    <!-- 教学大纲标题 -->
    <div class="course-name">教学大纲</div>

    <!-- 上传按钮，右对齐 -->
    <div class="upload-section-bottom">
      <el-button round type="primary" @click="dialogVisible = true" style="padding: 8px">上传</el-button>
    </div>

    <!-- PDF 预览区域，放置在标题和上传按钮的正下方，并占满空白区域 -->
    <div v-if="pdfUrl" class="pdf-preview-section">
      <iframe :src="pdfUrl" style="width: 100%; height: 100%;" frameborder="0"></iframe>
    </div>

    <!-- 上传对话框 -->
    <el-dialog title="上传教学大纲" v-model="dialogVisible" width="40%" @close="handleClose">
      <div class="upload-permission-section">
        <div class="dialog-permission-section">
          <div>权限设置：</div>
          <el-select v-model="selectedPermission" placeholder="选择权限" style="width: 150px;">
            <el-option label="本课程师生" value="course"></el-option>
            <el-option label="全校师生" value="school"></el-option>
            <el-option label="私有" value="private"></el-option>
          </el-select>
        </div>

        <div class="dialog-upload-section">
          <el-upload
            class="upload-demo"
            :limit="1"
            :on-exceed="handleExceed"
            :file-list="fileList"
            :before-upload="beforeUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :accept="acceptedFileTypes"
            ref="uploadRef"
            action="http://localhost:8080/attachment/upload"
            :headers="headers"
          >
            <el-button type="text" style="margin-right: 30px">点击选择文件</el-button>
          </el-upload>
        </div>

        <div class="file-type-warning">
          允许上传的文件类型：doc、pdf、docx、jpg、png，文件不能超过2G
        </div>
      </div>

      <br />

      <template v-slot:footer>
        <span class="dialog-footer">
          <el-button round type="primary" @click="confirmUpload" style="padding: 10px">确认</el-button>
          <el-button round @click="dialogVisible = false" style="padding: 10px">取消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { resourcesyllabus } from '@/api/course'

const fileList = ref([])
const uploadRef = ref(null)
const acceptedFileTypes = ".doc,.docx,.pdf,.jpg,.png" // 支持格式
const selectedPermission = ref('course')
const dialogVisible = ref(false)

const attachmentIdList = ref([])
const pdfUrl = ref(null) // 存储预览文件的 URL
const headers = {
  Authorization: localStorage.getItem('token')
}

// 上传文件前的检查
const beforeUpload = (file) => {
  const isAllowedSize = file.size / 1024 / 1024 < 2048 // 文件大小限制为 2G
  if (!isAllowedSize) {
    alert('文件大小不能超过2G！')
    return false
  }
  return true
}

// 上传文件成功后的处理
const handleUploadSuccess = (response, file) => {
  alert(`${file.name} 上传成功`)
  console.log('response',response)
  pdfUrl.value = encodeURI(response.url) // 假设服务器返回的 URL 位于 response.url，编码后保存到 pdfUrl
  dialogVisible.value = false // 关闭上传对话框
}

// 上传文件失败后的处理
const handleUploadError = () => {
  alert('上传失败')
}

// 提交上传操作
const confirmUpload = () => {
  if (attachmentIdList.value.length === 0) {
    alert('请先选择文件上传')
    return
  }

  // 将附件信息保存到课程大纲
  resourcesyllabus({
    attachmentIdList: attachmentIdList.value
  }).then(res => {
    console.log(res)
    alert('文件信息保存成功')
    dialogVisible.value = false // 关闭对话框
  }).catch(error => {
    alert('文件信息保存失败，请稍后再试')
    console.error(error)
  })
}

// 处理上传文件超过限制
const handleExceed = () => {
  alert('一次只能上传一个文件')
}
</script>

<style scoped>
.course-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 10px;
}
.course-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}
.upload-section-bottom {
  text-align: right;
  width: 100%;
  margin-bottom: 20px;
}
.pdf-preview-section {
  width: 100%;
  height: calc(100vh - 200px); /* 动态高度，确保占满页面空白区域 */
  overflow: auto;
  border: 1px solid #e0e0e0;
}
.dialog-permission-section {
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
}
.upload-permission-section {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 20px;
  margin-top: 20px;
}
.file-type-warning {
  margin-top: -25px;
  font-size: 12px;
  color: gray;
}
.dialog-footer {
  text-align: right;
}
</style>
