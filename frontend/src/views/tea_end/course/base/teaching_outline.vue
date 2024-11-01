<template>
  <div class="course-header">
    <div class="course-name">教学大纲</div>
    <div class="actions-section" style="margin-bottom: 8px; margin-left: 1px;">
      <div class="template-download-section" style="margin-right: 20px;">
        <el-button round @click="downloadTemplate" type="text">下载模板</el-button>
      </div>
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
        <el-upload class="upload-demo" :limit="1" :on-exceed="handleExceed" :file-list="fileList"
          :on-success="handleUploadSuccess" :accept="acceptedFileTypes" ref="uploadRef"
          action="http://localhost:8080/attachment/upload" :headers="headers">
          <el-button type="text" style="margin-right: 30px">点击选择文件</el-button>
        </el-upload>
      </div>

      <!-- 提示信息 -->
      <div class="file-type-warning">
        允许上传的文件类型：doc、pdf、ppt、xls、docx、pptx、xlsx、mp4、
        mp3、avi、wmv、3gp、mov、rmvb、flv、f4v、rm、asf、
        asx、jpg、gif、jpeg、png、bmp，文件不能超过2G
      </div>
    </div>

    <br>

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
import axios from 'axios'

const fileList = ref([])
const fileContent = ref('')
const uploadRef = ref(null)
const acceptedFileTypes = ".doc,.docx"  // 仅支持 .doc 和 .docx 格式
const selectedPermission = ref('course')
const dialogVisible = ref(false)
const courseId = "16" // 假设课程ID为16，实际可以根据需求传递
const uploaderId = 10018 // 上传者ID
const uploaderName = "陈旭东" // 上传者姓名
import { resourcesyllabus,getsyllabus } from '@/api/course.js'

const beforeUpload = (file) => {
  const isAllowedSize = file.size / 1024 / 1024 < 2048  // 文件大小限制为 2G
  if (!isAllowedSize) {
    alert('文件大小不能超过2G！')
    return false
  }
  return true
}
const headers = {
  Authorization: localStorage.getItem('token')
}

const uploadFile = async (options) => {
  const { file } = options
  const formData = new FormData()
  formData.append('file', file)

  try {
    // 使用 FormData 上传文件，获取文件URL
    const response = await axios.post('https://jsonplaceholder.typicode.com/posts/', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.status === 201) {
      // 获取上传的文件的URL
      const fileUrl = response.data.url || 'http://example.com/your-uploaded-file-url'

      // 构造请求体数据
      const requestBody = {
        id: uploaderId,
        name: uploaderName,
        course_id: courseId,
        files: [
          {
            file_url: fileUrl
          }
        ]
      }

      // 发送POST请求，将文件信息上传到后端
      await axios.post('/courses/resourses', requestBody, {
        headers: {
          'Content-Type': 'application/json'
        }
      })

      options.onSuccess(response.data, file)
    } else {
      options.onError(new Error('文件上传失败'))
    }
  } catch (error) {
    options.onError(error)
  }
}

const submitUpload = () => {
  // uploadRef.value.submit()
  resourcesyllabus({
    attachmentIdList: attachmentIdList.value
  }).then(res => {
    console.log(res);

  })

}
const attachmentIdList = ref([])
const handleUploadSuccess = (response, file) => {
  attachmentIdList.value.push(response.id)

  // alert(`${file.name} 上传成功`)
}

const handleUploadError = () => {
  alert('上传失败')
}

const handleExceed = () => {
  alert('一次只能上传一个文件')
}

const downloadFile = () => {
  let id = localStorage.getItem('kcid')


  window.location.href = 'http://localhost:8080/courses/get-syllabus?id=' + id  // 更新下载链接
}

const downloadTemplate = () => {
  window.location.href = '/path-to-your-template-file'
}

const handleClose = () => {
  dialogVisible.value = false
}
const getSyllabus = (params) => {
  let id=localStorage.getItem('kcid')
  getsyllabus(id).then(res=>{
    console.log(res)
  })
};
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
  /* 垂直排列 */
  align-items: flex-start;
  /* 左对齐 */
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