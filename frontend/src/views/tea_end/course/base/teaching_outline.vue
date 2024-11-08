<template>
  <div class="course-header">
    <div class="course-name">教学大纲</div>
    <div class="actions-section" style="margin-bottom: 8px; margin-left: 1px;">
      <!-- <div class="template-download-section" style="margin-right: 20px;">
        <el-button round @click="downloadTemplate" type="text">下载模板</el-button>
      </div> -->
      <div class="download-section">
        <el-button round @click="downloadFile" type="text">下载教学大纲</el-button>
      </div>
    </div>
  </div>

  <el-card class="course-card">
    <div class="course-info">
      <p>教学大纲的详细内容</p>
    </div>
  </el-card>

  <div class="upload-section-bottom">
    <el-button round type="primary" @click="dialogVisible = true" style="padding: 8px">上传</el-button>
  </div>

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
        允许上传的文件类型：doc、pdf、ppt、xls、docx、pptx、xlsx、mp4、mp3、avi、wmv、3gp、mov、rmvb、flv、f4v、rm、asf、asx、jpg、gif、jpeg、png、bmp，文件不能超过2G
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
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { resourcesyllabus, getsyllabus } from '@/api/course'

const fileList = ref([])
const uploadRef = ref(null)
const acceptedFileTypes = ".doc,.docx"  // 仅支持 .doc 和 .docx 格式
const selectedPermission = ref('course')
const dialogVisible = ref(false)

const attachmentIdList = ref([])
const headers = {
  Authorization: localStorage.getItem('token')
}

// 上传文件前的检查
const beforeUpload = (file) => {
  const isAllowedSize = file.size / 1024 / 1024 < 2048  // 文件大小限制为 2G
  if (!isAllowedSize) {
    alert('文件大小不能超过2G！')
    return false
  }
  return true
}

// 上传文件成功后的处理
const handleUploadSuccess = (response, file) => {
  alert(`${file.name} 上传成功`)
  attachmentIdList.value.push(response.id) // 假设 response 中包含文件的 ID
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

// 下载教学大纲文件
const downloadFile = () => {
  let id = localStorage.getItem('kcid')
  window.location.href = 'http://localhost:8080/courses/get-syllabus?id=' + id
}

// 下载模板
// const downloadTemplate = () => {
//   window.location.href = '/path-to-your-template-file'
// }

// 获取课程大纲信息
const getSyllabus = () => {
  let id = localStorage.getItem('kcid')
  getsyllabus(id).then(res => {
    console.log(res)
  })
}
getSyllabus()
</script>

<style scoped>
.actions-section {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.course-card {
  width: 100%;
  padding: 10px;
}

.course-info {
  font-size: 14px;
  color: gray;
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

.upload-section-bottom {
  text-align: right;
  margin-top: 20px;
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
