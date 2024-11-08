<template v-slot:header>
  <el-row :gutter="20" class="header" style="margin-top: 10px; margin-left: 10px; margin-right: 20px;">
    <el-col :span="12">
      <el-button round type="primary" @click="openUploadDialog" style="padding:10px;">上传文件</el-button>
      <el-button round type="primary" @click="openCreateDialog" style="padding:10px;">新建目录</el-button>
    </el-col>
    <el-col :span="12" style="text-align: right;">
      <el-button circle type="primary" @click="batchDelete" style="padding:10px;">
        <el-icon>
          <Delete />
        </el-icon>
      </el-button>
    </el-col>
  </el-row>
  <div class="box-card">
    <!-- 目录卡片 -->
    <el-card style="margin-top: 2px;">
      <div class="forum-list" id="directory-data" style="max-height: 200px; margin-top: -10px; overflow-y: auto;">
        <el-table :data="directoryData" style="width: 100%;" class="resource-table"
          @selection-change="handleDirectorySelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="目录名称" width="300" align="center" header-align="center">
            <template #default="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="目录描述" width="300" align="center" header-align="center">
            <template #default="scope">
              <span>{{ scope.row.description }}</span>
            </template>
          </el-table-column>
          <el-table-column label="发布状态" width="150" align="center" header-align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.published"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="300" align="center" header-align="center">
            <template #default="scope">
              <el-button type="text" @click="openEditDialog(scope.row)">编辑</el-button>
              <el-button type="text" @click="handleDeleteDirectory(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <!-- 文件排序方式选择 -->
    <el-row :gutter="20" class="header" style="margin-top: 10px;">
      <el-col :span="12" style="margin-left: 20px;">
        <el-select v-model="selectedDirectory" placeholder="选择目录"
          style="width: 200px; margin-right: 15px; margin-left: -20px;" @change="handleDirectoryChange">
          <el-option label="所有文件" value="all" />
          <el-option v-for="directory in directoryData" :key="directory.name" :label="directory.name"
            :value="directory.name" />
        </el-select>
        <el-select v-model="sortOrder" placeholder="选择排序方式" style="width: 200px;" @change="handleSortChange">
          <el-option label="按文件名称排序" value="name" />
          <el-option label="按上传时间排序" value="uploadTime" />
        </el-select>
      </el-col>
      <el-col :span="8">
        <el-input v-model="searchQuery" placeholder="输入课件名称进行搜索" class="search-input" />
      </el-col>
      <el-col :span="2">
        <el-button circle style="margin-left: 115px; padding: 10px;" type="primary" @click="handleSearch">
          <el-icon>
            <Search />
          </el-icon>
        </el-button>
      </el-col>
    </el-row>
    <!-- 文件卡片 -->
    <el-card style="margin-top: 2px;">
      <div class="forum-list" id="file-data" style="max-height: 300px; margin-top: -10px; overflow-y: auto;">
        <el-table :data="sortedFiles" style="width: 100%;" class="resource-table"
          @selection-change="handleFileSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="文件名称" width="350" align="center" header-align="center">
            <template #default="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="发布状态" width="150" align="center" header-align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.published"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="是否允许学生下载" width="150" align="center" header-align="center">
            <template #default="scope">
              <el-checkbox v-model="scope.row.allowDownload"></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column label="分享" width="150" align="center" header-align="center">
            <template #default="scope">
              <el-icon @click="handleShare(scope.row)">
                <Share />
              </el-icon>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center" header-align="center">
            <template #default="scope">
              <!-- <el-button type="text" @click="openEditFileDialog(scope.row)">编辑</el-button> -->
              <el-button type="text" @click="handleDownload(scope.row)">下载</el-button>
              <el-button type="text" @click="handleDeleteFile(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pagination-container">
        <el-pagination layout="prev, pager, next" :total="sortedFiles.length" :page-size="pageSize"
          :current-page="currentPage" @current-change="handleCurrentChange" />
      </div>
    </el-card>
  </div>

  <!-- 新建目录对话框 -->
  <el-dialog title="新建目录" v-model="createDialogVisible">
    <el-form :model="newDirectory">
      <el-form-item label="目录名称" :label-width="formLabelWidth">
        <el-input v-model="newDirectory.name" maxlength="60"></el-input>
        <span class="input-tip">目录名称长度不超过60个汉字</span>
      </el-form-item>
      <!-- <el-form-item label="目录描述" :label-width="formLabelWidth">
        <el-input type="textarea" v-model="newDirectory.description" maxlength="300"></el-input>
        <span class="input-tip">300字以内</span>
      </el-form-item> -->
    </el-form>
    <template v-slot:footer>
      <div class="dialog-footer">
        <el-button round type="primary" @click="createDirectory">确定</el-button>
        <el-button round @click="createDialogVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 编辑目录对话框 -->
  <el-dialog title="编辑目录" v-model="editDialogVisible">
    <el-form :model="currentDirectory">
      <el-form-item label="目录名称" :label-width="formLabelWidth">
        <el-input v-model="currentDirectory.name" maxlength="60"></el-input>
        <span class="input-tip">目录名称长度不超过60个汉字</span>
      </el-form-item>
      <!-- <el-form-item label="目录描述" :label-width="formLabelWidth">
        <el-input type="textarea" v-model="currentDirectory.description" maxlength="300"></el-input>
        <span class="input-tip">300字以内</span>
      </el-form-item> -->
    </el-form>
    <template v-slot:footer>
      <div class="dialog-footer">
        <el-button round type="primary" @click="updateDirectory">确定</el-button>
        <el-button round @click="editDialogVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 上传文件对话框 -->
  <el-dialog title="上传文件" v-model="uploadDialogVisible">
    <el-form :model="newFile">
      <!-- <el-form-item label="课程资源名称" :label-width="formLabelWidth">
        <el-input v-model="newFile.name" maxlength="60"></el-input>
      </el-form-item> -->
      <el-form-item label="上传文件" :label-width="formLabelWidth">
        <el-upload action="http://localhost:8080/attachment/upload" :before-upload="beforeUpload"
          :on-success="handleUploadSuccess" :file-list="fileList" :headers="headers">
          <!-- :headers="headers" -->
          <el-button round type="primary">点击上传</el-button>
        </el-upload>
        <span class="input-tip">允许上传的文件类型: doc, pdf, ppt, xls, docx, pptx, xlsx, mp4, mp3, avi, wmv, 3gp, mov, rmvb,
          flv,
          f4v, asf, asx, jpg, gif, jpeg, png, bmp, zip, rar, 文件不超过2G。</span>
      </el-form-item>
      <!-- <el-form-item label="描述" :label-width="formLabelWidth">
        <el-input type="textarea" v-model="newFile.description" maxlength="300"></el-input>
      </el-form-item> -->
      <el-form-item label="允许学生下载" :label-width="formLabelWidth">
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

  <!-- 编辑文件对话框 -->
  <el-dialog title="编辑文件" v-model="editFileDialogVisible">
    <el-form :model="currentFile">
      <el-form-item label="文件名称" :label-width="formLabelWidth">
        <el-input v-model="currentFile.name" maxlength="60"></el-input>
      </el-form-item>
      <el-form-item label="描述" :label-width="formLabelWidth">
        <el-input type="textarea" v-model="currentFile.description" maxlength="300"></el-input>
      </el-form-item>
      <el-form-item label="允许学生下载" :label-width="formLabelWidth">
        <el-radio-group v-model="currentFile.allowDownload">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template v-slot:footer>
      <div class="dialog-footer">
        <el-button round type="primary" @click="updateFile">确定</el-button>
        <el-button round @click="editFileDialogVisible = false">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { deleteFile, getppt, resourceppt, createattachmentfolder, getexercise,resourcecourse } from '@/api/course.js'
const headers = {
  Authorization: localStorage.getItem('token')
}
const handleShare = (file) => {
  if (navigator.share) {
    // 使用 Web Share API
    navigator.share({
      title: '分享文件',
      text: `我想分享文件: ${file.name}`,
      url: file.url,
    })
      .then(() => console.log('分享成功'))
      .catch(error => console.error('分享失败:', error));
  } else {
    // 兼容不支持 Web Share API 的浏览器
    const shareLink = document.createElement('textarea');
    shareLink.value = `我想分享文件: ${file.name}\n链接: ${file.url}`;
    document.body.appendChild(shareLink);
    shareLink.select();
    document.execCommand('copy');
    document.body.removeChild(shareLink);
    ElMessage.success('分享链接已复制到剪贴板');
  }
};

const directoryData = ref([
  {
    name: '2023-2024第一学期',
    published: true,
    description: '这是2023-2024学年的第一学期',
    files: [
      { name: '文件2.pdf', url: 'https://example.com/file2.pdf', allowDownload: true, published: true, uploadTime: '2023-02-01' },
      { name: '文件1.pdf', url: 'https://example.com/file1.pdf', allowDownload: true, published: true, uploadTime: '2023-01-01' },

    ]
  },
  {
    name: '2023-2023第二学期',
    published: true,
    description: '这是2023-2023学年的第二学期',
    files: [
      { name: '文件3.pdf', url: 'https://example.com/file3.pdf', allowDownload: true, published: true, uploadTime: '2023-03-01' },
      { name: '文件4.pdf', url: 'https://example.com/file4.pdf', allowDownload: true, published: true, uploadTime: '2023-04-01' }
    ]
  },
  {
    name: '2022-2023第一学期',
    published: true,
    description: '这是2022-2023学年的第一学期',
    files: [
      { name: '文件5.pdf', url: 'https://example.com/file5.pdf', allowDownload: true, published: true, uploadTime: '2023-05-01' },
      { name: '文件6.pdf', url: 'https://example.com/file6.pdf', allowDownload: true, published: true, uploadTime: '2023-06-01' }
    ]
  },
  // ... 其他目录数据 ...
]);

const pageSize = ref(8);
const currentPage = ref(1);
const searchQuery = ref('');
const filteredFiles = ref([]);
const selectedDirectory = ref('');
const fileList = ref([]);
const selectedDirectories = ref([]);
const selectedFiles = ref([]);
const editDialogVisible = ref(false);
const editFileDialogVisible = ref(false);
const currentDirectory = ref({});
const currentFile = ref({});
const sortOrder = ref('name'); // 默认按文件名称排序

// 新建目录对话框相关数据
const createDialogVisible = ref(false);
const newDirectory = ref({
  name: '',
  description: ''
});
const formLabelWidth = '100px';

// 上传文件对话框相关数据
const uploadDialogVisible = ref(false);
const newFile = ref({
  name: '',
  description: '',
  allowDownload: true,
  url: ''
});

// 过滤和分页数据
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return sortedFiles.value.slice(start, start + pageSize.value);
});

const allFiles = computed(() => {
  if (selectedDirectory.value === 'all' || !selectedDirectory.value) {
    return directoryData.value.flatMap(dir => dir.files);
  }
  const directory = directoryData.value.find(dir => dir.name === selectedDirectory.value);
  return directory ? directory.files : [];
});

function handlePreview(item) {
  window.open(item.url, '_blank');
}

function handleDownload(item) {
  const link = document.createElement('a');
  link.href = item.url;
  link.download = item.name;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

function handleCurrentChange(page) {
  currentPage.value = page;
}

function handleSearch() {
  currentPage.value = 1;
  if (!searchQuery.value) {
    filteredFiles.value = allFiles.value;
  } else {
    filteredFiles.value = allFiles.value.filter(file =>
      file.name.includes(searchQuery.value)
    );
  }
}

function handleDirectoryChange() {
  currentPage.value = 1;
  handleSearch();
}

function handleSortChange() {
  currentPage.value = 1;
}

function openCreateDialog() {
  createDialogVisible.value = true;
}

function createDirectory() {
  // if (newDirectory.value.name && newDirectory.value.description) {
  //   directoryData.value.push({
  //     name: newDirectory.value.name,
  //     description: newDirectory.value.description,
  //     published: false,
  //     files: []
  //   });
  //   createDialogVisible.value = false;
  //   newDirectory.value.name = '';
  //   newDirectory.value.description = '';
  // }
  createattachmentfolder({
    folderName: newDirectory.value.name,
    id: localStorage.getItem('kcid')
  }).then(res => {
    console.log(res);
  })
}

function openUploadDialog() {
  if (!selectedDirectory.value || selectedDirectory.value === 'all') {
    // 弹出alert('请选择一个目录！');
    alert('请选择一个目录！');
    return;
  }
  uploadDialogVisible.value = true;
}

function beforeUpload(file) {
  // 文件上传前的处理逻辑
  return true;
}
const uploadProps = ref({
  attachmentIdList: []
})
function handleUploadSuccess(response, file, fileList) {
  newFile.value.url = file.url;
  console.log(response);
  uploadProps.value.attachmentIdList.push(response.id)
  uploadProps.value.id = localStorage.getItem('kcid')

}

function uploadFile() {
  console.log(uploadProps.value);

  resourcecourse(uploadProps.value).then(res => {
    console.log(res)
  })
  // if (newFile.value.name && newFile.value.url) {
  //   const directory = directoryData.value.find(dir => dir.name === selectedDirectory.value);
  //   if (directory) {
  //     directory.files.push({
  //       name: newFile.value.name,
  //       description: newFile.value.description,
  //       allowDownload: newFile.value.allowDownload,
  //       url: newFile.value.url,
  //       published: false,
  //       uploadTime: new Date().toISOString().split('T')[0] // 添加上传时间
  //     });
  //   }
  //   uploadDialogVisible.value = false;
  //   newFile.value.name = '';
  //   newFile.value.description = '';
  //   newFile.value.allowDownload = true;
  //   newFile.value.url = '';
  //   fileList.value = [];
  //   handleSearch(); // 更新搜索结果
  // } else {
  //   this.$message.error('请填写完整信息并上传文件');
  // }
}

function openEditDialog(directory) {
  currentDirectory.value = { ...directory };
  editDialogVisible.value = true;
}

function updateDirectory() {
  // const index = directoryData.value.findIndex(dir => dir.name === currentDirectory.value.name);
  // if (index !== -1) {
  //   directoryData.value[index] = { ...currentDirectory.value };
  // }
  // editDialogVisible.value = false;

}

function openEditFileDialog(file) {
  currentFile.value = { ...file };
  editFileDialogVisible.value = true;
}

function updateFile() {
  const directory = directoryData.value.find(dir => dir.name === selectedDirectory.value);
  if (directory) {
    const index = directory.files.findIndex(f => f.url === currentFile.value.url);
    if (index !== -1) {
      directory.files[index] = { ...currentFile.value };
    }
  }
  editFileDialogVisible.value = false;
}

function handleDeleteDirectory(item) {
  const index = directoryData.value.indexOf(item);
  if (index !== -1) {
    directoryData.value.splice(index, 1);
  }
}

function handleDeleteFile(item) {
  deleteFile(item.id).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
    } else {
      ElMessage.error('删除失败')
    }
  })
  const directory = directoryData.value.find(dir => dir.name === selectedDirectory.value);
  if (directory) {
    const index = directory.files.indexOf(item);
    if (index !== -1) {
      directory.files.splice(index, 1);
    }
  }

}

function handleDirectorySelectionChange(val) {
  selectedDirectories.value = val;
}

function handleFileSelectionChange(val) {
  selectedFiles.value = val;
}

function batchDelete() {
  // 删除选中的目录
  selectedDirectories.value.forEach(directory => {
    const index = directoryData.value.indexOf(directory);
    if (index !== -1) {
      directoryData.value.splice(index, 1);
    }
  });

  // 删除选中的文件
  const directory = directoryData.value.find(dir => dir.name === selectedDirectory.value);
  if (directory) {
    selectedFiles.value.forEach(file => {
      const index = directory.files.indexOf(file);
      if (index !== -1) {
        directory.files.splice(index, 1);
      }
    });
  }

  // 清空选中的目录和文件
  selectedDirectories.value = [];
  selectedFiles.value = [];
}

function togglePublish(item) {
  item.published = !item.published;
}

const sortedFiles = computed(() => {
  let files = [...filteredFiles.value];
  if (sortOrder.value === 'name') {
    files.sort((a, b) => a.name.localeCompare(b.name));
  } else if (sortOrder.value === 'uploadTime') {
    files.sort((a, b) => new Date(b.uploadTime) - new Date(a.uploadTime));
  }
  return files;
});
function getpptlist() {
  let id = localStorage.getItem('kcid')
  getexercise(id).then(res => {
    console.log(res)
  })
}
getpptlist()
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