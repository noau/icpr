<template>
  <div class="course-header">
    <!-- 教学日历标题 -->
    <div class="course-name">教学日历</div>

    <!-- 上传按钮，右对齐 -->
    <div class="upload-section-bottom">
      <el-button round type="primary" @click="dialogVisible = true" style="padding: 8px">上传</el-button>
    </div>

    <!-- PDF 预览区域，优先使用 VueOfficePdf 组件展示 PDF 文件 -->
    <vue-office-pdf 
      v-if="pdfUrl"    
      :src="pdfUrl"
      style="height: calc(100vh - 200px); width: 100%;"
      @rendered="renderedHandler"
      @error="errorHandler"
    />
    <div v-else-if="pdfIframeUrl" class="pdf-preview-section">
      <iframe :src="pdfIframeUrl" style="width: 100%; height: 100%;" frameborder="0"></iframe>
    </div>

    <!-- 上传对话框 -->
    <el-dialog title="上传教学日历" v-model="dialogVisible" width="40%" @close="handleClose">
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
          允许上传的文件类型：pdf
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
import VueOfficePdf from '@vue-office/pdf'
import { resourcecalendar, getcalendar, getAttachmentUrl } from '@/api/course'

const fileList = ref([])
const uploadRef = ref(null)
const acceptedFileTypes = ".pdf" // 支持格式
const selectedPermission = ref('course')
const dialogVisible = ref(false)


const pdfUrl = ref(null) // 存储 VueOfficePdf 文件的 URL
const pdfIframeUrl = ref(null) // 用于 iframe 预览的 URL
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
const pdfId = ref('')
// 上传文件成功后的处理
const handleUploadSuccess = (response, file) => {
  alert(`${file.name} 上传成功`)
  pdfId.value = response.id // 记录文件的 ID
  loadFileUrl(pdfId.value) // 使用加载文件 URL 函数加载预览
  // dialogVisible.value = false // 关闭上传对话框
}

// 上传文件失败后的处理
const handleUploadError = () => {
  alert('上传失败')
}

// 提交上传操作
const confirmUpload = () => {
  if (!pdfId.value) {
    alert('请先选择文件上传')
    return
  }

  // 将附件信息保存到课程日历
  resourcecalendar({
    attachmentIdList: [pdfId.value], //attachmentIdList.value,
    id: localStorage.getItem('courseId')
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

// 加载教学日历的 PDF 文件
const loadCalendar = () => {
  const courseId = localStorage.getItem('courseId')
  getcalendar(courseId).then(res => {
    if (res && res.attachmentIdList && res.attachmentIdList.length > 0) {
      const attachmentId = res.attachmentIdList[res.attachmentIdList.length-1].id
      loadFileUrl(attachmentId)
    } else {
      console.log("未找到教学日历附件")
    }
  }).catch(error => {
    console.log("获取教学日历失败", error)
  })
}

// 根据附件 ID 获取文件 URL
const loadFileUrl = (attachmentId) => {
  getAttachmentUrl(attachmentId).then(res => {
    if (res && res.url) {
      pdfUrl.value = res.url.endsWith('.pdf') ? res.url : null // 若为 PDF，设置为 PDF URL
      pdfIframeUrl.value = !pdfUrl.value ? encodeURI(res.url) : null // 若非 PDF，则设置 iframe URL
      console.log("文件URL加载成功")
    } else {
      console.log("未获取到文件URL")
    }
  }).catch(error => {
    console.log("获取文件URL失败", error)
  })
}

// 在页面加载时调用教学日历加载函数
loadCalendar()
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

