<template>
  <div class="course-header">
    <div class="course-name">教学日历</div>
    <!-- 下载按钮，右对齐 -->
    <div class="actions-section" style="margin-bottom: 8px; margin-left: 1px;">
      <!-- <div class="template-download-section" style="margin-right: 20px;">
        <el-button round @click="downloadTemplate" type="text">下载模板</el-button>
      </div> -->
      <div class="download-section">
        <el-button round @click="downloadFile" type="text">下载教学日历</el-button>
      </div>
    </div>
  </div>

  <!-- 课程信息 -->
  <el-card class="course-card">
    <div class="course-info">
      <p>教学日历的详细内容</p>
    </div>
  </el-card>

  <!-- 上传按钮，卡片下方，右对齐 -->
  <div class="upload-section-bottom">
    <el-button round type="primary" @click="dialogVisible = true" style="padding: 8px">上传</el-button>
  </div>

  <!-- 上传对话框 -->
  <el-dialog title="上传教学日历" v-model="dialogVisible" width="40%" @close="handleClose">
    <!-- 上传文件和权限设置 -->
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
        <el-upload class="upload-demo" action="http://localhost:8080/courses/resource-calendar" :limit="1"
          :on-exceed="handleExceed" :file-list="fileList" :before-upload="beforeUpload" :http-request="uploadFile"
          :on-success="handleUploadSuccess" :on-error="handleUploadError" :auto-upload="false"
          :accept="acceptedFileTypes" ref="uploadRef" :headers="headers">
          <el-button type="text" style="margin-right: 30px">点击选择文件</el-button>
        </el-upload>
      </div>
    </div>

    <!-- 提示信息 -->
    <div class="file-type-warning">
      允许上传的文件类型：doc、docx，文件不能超过2G
    </div>

    <br>

    <!-- 对话框底部按钮 -->
    <template v-slot:footer>
      <span class="dialog-footer">
        <el-button round type="primary" @click="submitUpload" style="padding: 10px">确认</el-button>
        <el-button round @click="dialogVisible = false" style="padding: 10px">取消</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { getcalendar } from '@/api/course'

const fileList = ref([])
const fileContent = ref('')
const uploadRef = ref(null)
const acceptedFileTypes = ".doc,.docx"  // 更新为仅支持 .doc 和 .docx 格式
const selectedPermission = ref('course')
const dialogVisible = ref(false)
const headers = {
  'Authorization': localStorage.getItem('token')
}

const beforeUpload = (file) => {
  const isAllowedSize = file.size / 1024 / 1024 < 2048  // 文件大小限制为 2G
  if (!isAllowedSize) {
    alert('文件大小不能超过2G！')
    return false
  }
  return true
}

const uploadFile = (options) => {
  const { file } = options
  const reader = new FileReader()

  reader.onload = (e) => {
    fileContent.value = e.target.result
  }
}

const submitUpload = () => {
  uploadRef.value.submit()
}

const handleUploadSuccess = (response, file) => {
  alert(`${file.name} 上传成功`)
}

const handleUploadError = () => {
  alert('上传失败')
}

const handleExceed = () => {
  alert('一次只能上传一个文件')
}

const downloadFile = () => {
  let id = localStorage.getItem('kcid')


  window.location.href = 'http://localhost:8080/courses/get-calendar?id=' + id  // 更新下载链接
}

const downloadTemplate = () => {
  window.location.href = '/path-to-your-template-file'
}

const handleClose = () => {
  dialogVisible.value = false
}

const getCalendar = (params) => {
  let id = localStorage.getItem('kcid')
  getcalendar(id).then(res => {
    console.log(res)
  })

};
getCalendar()

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

/* 上传按钮区域，右对齐 */
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
  /* 垂直排列 */
  align-items: flex-start;
  /* 左对齐 */
  gap: 20px;
  margin-top: 20px;
}

.file-type-warning {
  margin-top: -5px;
  font-size: 12px;
  color: gray;
}

.dialog-footer {
  text-align: right;
}
</style>
