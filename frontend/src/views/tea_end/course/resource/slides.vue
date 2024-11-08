<template>
  <!-- Rest of the template code -->
  <!-- Upload File Dialog -->
  <el-dialog title="上传文件" v-model="uploadDialogVisible">
    <el-form :model="newFile">
      <el-form-item label="上传文件">
        <el-upload
          :file-list="fileList"
          :auto-upload="false"
          :on-change="handleFileChange"
          list-type="text"
        >
          <el-button round type="primary">点击上传</el-button>
        </el-upload>
        <span class="input-tip">允许上传的文件类型: doc, pdf, ppt, 等...</span>
      </el-form-item>
      <el-form-item label="允许学生下载">
        <el-radio-group v-model="newFile.allowDownload">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <div class="dialog-footer">
        <el-button round type="primary" @click="uploadFile">确定</el-button>
        <el-button round @click="uploadDialogVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getppt, deleteFile, createattachmentfolder, upload } from '@/api/course';

const directoryData = ref([]); // Folder data
const filteredFiles = ref([]); // File data
const pageSize = ref(8);
const currentPage = ref(1);
const uploadDialogVisible = ref(false);
const fileList = ref([]);
const headers = { Authorization: localStorage.getItem('token') };
const courseId = localStorage.getItem('courseId');

// File data for upload
const newFile = ref({
  name: '',
  allowDownload: true,
  url: ''
});

const selectedFile = ref(null); // Stores the selected file

// Fetch file list
const fetchPptList = async () => {
  try {
    if (courseId) {
      const response = await getppt(courseId);
      filteredFiles.value = response.data || [];
    } else {
      console.error('课程ID未找到');
    }
  } catch (error) {
    console.error('获取电子课件列表失败:', error);
  }
};

// Initialize data
onMounted(() => {
  fetchPptList();
});

// Handle file selection
const handleFileChange = (file) => {
  selectedFile.value = file.raw;
};

// Upload file handling
const uploadFile = async () => {
  if (!selectedFile.value) {
    ElMessage.error('请选择要上传的文件');
    return;
  }
  
  try {
    const response = await upload(selectedFile.value, headers.Authorization);
    if (response.status === 200) {
      ElMessage.success('文件上传成功');
      uploadDialogVisible.value = false;
      fetchPptList(); // Refresh the file list after upload
    } else {
      ElMessage.error('文件上传失败');
    }
  } catch (error) {
    console.error('文件上传失败:', error);
    ElMessage.error('文件上传失败');
  }
};
</script>


<style scoped>
.box-card {
  margin: 20px;
  margin-top: 2px;
}

.search-input {
  flex: 1;
  width: 250px;
  margin-left: 250px;
}

.forum-list {
  padding: 0;
}

.resource-table .el-button {
  padding: 8px 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.custom-icon-document {
  width: 30px;
  height: 30px;
  margin-right: 3px;
}

.input-tip {
  color: #999;
  font-size: 12px;
  margin-left: 10px;
}
</style>
