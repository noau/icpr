<template>
  <div> <!-- 根元素开始 -->
    <el-row :gutter="20" class="header" style="margin-top: 10px; margin-left: 10px; margin-right: 20px;">
      <el-col :span="12">
        <el-button round type="primary" @click="openCreateDialog" style="padding:10px;">新建文件夹</el-button>
      </el-col>
      <el-col :span="12" style="text-align: right;">
        <!-- <el-button circle type="primary" @click="batchDelete" style="padding:10px;">
          <el-icon>
            <Delete />
          </el-icon>
        </el-button> -->
      </el-col>
    </el-row>

    <div class="box-card">
      <!-- 文件夹卡片 -->
      <el-card style="margin-top: 2px;">
        <div class="forum-list" id="directory-data" style="max-height: 200px; margin-top: -10px; overflow-y: auto;">
          <el-table :data="directoryData" style="width: 100%;" class="resource-table" @selection-change="handleDirectorySelectionChange">
            <!-- <el-table-column type="selection" width="55" /> -->
            <el-table-column prop="name" label="文件夹名称" align="center" header-align="center">
              <template #default="scope">
                <span>{{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column label="发布状态" width="150" align="center" header-align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.published"></el-checkbox>
              </template>
            </el-table-column> -->
            <el-table-column label="操作" align="center" header-align="center">
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
          <el-select v-model="selectedDirectory" placeholder="选择文件夹" style="width: 200px; margin-right: 15px; margin-left: -20px;" @change="handleDirectoryChange">
            <el-option label="所有文件" value="all" />
            <el-option v-for="directory in directoryData" :key="directory.name" :label="directory.name" :value="directory.id" />
          </el-select>
          <el-select v-model="sortOrder" placeholder="选择排序方式" style="width: 200px;" @change="handleSortChange">
            <el-option label="按文件名称排序" value="name" />
            <el-option label="按上传时间排序" value="uploadTime" />
          </el-select>
        <el-button round type="primary" @click="openUploadDialog" style="padding:10px;">上传文件</el-button>
        </el-col>
        <el-col :span="7">
          <el-input v-model="searchQuery" placeholder="输入资源名称进行搜索" class="search-input" />
        </el-col>
        <el-col :span="2">
          <el-button circle style="margin-left: 115px; padding: 10px;" type="primary" @click="handleSearch">
            <el-icon>
              <Search />
            </el-icon>
          </el-button>
        </el-col>
      </el-row>
      <!-- sortedFiles -->
      <!-- 文件卡片 -->
      <el-card style="margin-top: 2px;">
        <div class="forum-list" id="file-data" style="max-height: 300px; margin-top: -10px; overflow-y: auto;">
          <el-table :data="paginatedData" style="width: 100%;" class="resource-table" @selection-change="handleFileSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="name" label="文件名称" width="350" align="center" header-align="center">
              <template #default="scope">
                <span>{{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <!-- <el-table-column label="发布状态" width="150" align="center" header-align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.published"></el-checkbox>
              </template>
            </el-table-column> -->
            <!-- <el-table-column label="是否允许学生下载" align="center" header-align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.allowDownload"></el-checkbox>
              </template>
            </el-table-column> -->
            <el-table-column label="操作" align="center" header-align="center">
              <template #default="scope">
                <el-button type="text" @click="handleDownload(scope.row)">下载</el-button>
                <el-button type="text" @click="handleDeleteFile(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="pagination-container">
          <el-pagination layout="prev, pager, next" :total="sortedFiles?.length || 0" :page-size="pageSize"
           :current-page="currentPage" @current-change="handleCurrentChange" />
        </div>
      </el-card>
    </div>

    <!-- 新建文件夹对话框 -->
    <el-dialog title="新建文件夹" v-model="createDialogVisible">
      <el-form :model="newDirectory">
        <el-form-item label="文件夹名称" :label-width="formLabelWidth">
          <el-input v-model="newDirectory.name" maxlength="60"></el-input>
          <span class="input-tip">文件夹名称长度不超过60个汉字</span>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button round type="primary" @click="createDirectory">确定</el-button>
          <el-button round @click="createDialogVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 编辑文件夹对话框 -->
    <el-dialog title="编辑文件夹" v-model="editDialogVisible">
      <el-form :model="currentDirectory">
        <el-form-item label="文件夹名称" :label-width="formLabelWidth">
          <el-input v-model="currentDirectory.name" maxlength="60"></el-input>
          <span class="input-tip">文件夹名称长度不超过60个汉字</span>
        </el-form-item>
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
        <el-form-item label="上传文件" :label-width="formLabelWidth">
          <el-upload action="http://localhost:8080/attachment/upload" :before-upload="beforeUpload" :on-success="handleUploadSuccess" :file-list="fileList" :headers="headers" accept=".pdf,.docx,.doc,.jpg,.jpeg,.png,.gif,.bmp,.zip,.rar" >
            <el-button round type="primary">点击上传</el-button>
          </el-upload>
          <span class="input-tip">允许上传的文件类型: pdf, doc, docx, jpg, jpeg, png, gif, bmp, zip, rar，文件不超过2G。</span>
        </el-form-item>
        <!-- <el-form-item label="允许学生下载" :label-width="formLabelWidth">
          <el-radio-group v-model="newFile.allowDownload">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group> -->
        <!-- </el-form-item> -->
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
        <!-- <el-form-item label="允许学生下载" :label-width="formLabelWidth">
          <el-radio-group v-model="currentFile.allowDownload">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item> -->
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button round type="primary" @click="updateFile">确定</el-button>
          <el-button round @click="editFileDialogVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div> <!-- 根元素结束 -->
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Search } from '@element-plus/icons-vue';
// import { ElMessage } from 'element-plus';
import {editAttachmentfolder, deleteForder, deleteFile, createattachmentfolder, getexam, resourceexam } from '@/api/course'
const headers = {
  Authorization: localStorage.getItem('token')
}


const directoryData = ref([]);

const pageSize = ref(5);
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

// 新建文件夹对话框相关数据
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
  return sortedFiles.value?.slice(start, start + pageSize.value);
});

const allFiles = computed(() => {
  if (selectedDirectory.value === 'all' || !selectedDirectory.value) {
    return directoryData.value.flatMap(dir => dir.files || []);
  }
  const directory = directoryData.value.find(dir => dir.id === selectedDirectory.value);
  return directory ? directory.files : [];
});

// function handlePreview(item) {
//   window.open(item.url, '_blank');
// }

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
  // 判断文件夹名称不为空
  if (!newDirectory.value.name.trim()) {
    alert('文件夹名称不能为空')
    return
  }
  createattachmentfolder({
    folderName: newDirectory.value.name,
    courseId: localStorage.getItem('courseId'),
    parentId: null,
    type: 'exam'
  }).then(res => {
    console.log(res);
    createDialogVisible.value = false;
    getexamList()
    // console.log(res);
  })
}

function openUploadDialog() {
  if (!selectedDirectory.value || selectedDirectory.value === 'all') {
    // 弹出alert('请选择一个文件夹！');
    alert('请选择一个文件夹！');
    return;
  }
  uploadDialogVisible.value = true;
}

function beforeUpload(file) {
  // 文件上传前的处理逻辑
  return true;
}
const uploadProps = ref({
  id: localStorage.getItem('courseId'),
  attachmentIdList: []
})
function handleUploadSuccess(response, file, fileList) {
  newFile.value.url = file.url;
  console.log(response);
  uploadProps.value.attachmentIdList.push({id: response.id})
  uploadProps.value.id = localStorage.getItem('courseId')

}

// 上传文件逻辑
async function uploadFile() {
  // 封装请求参数， 请求上传历年试卷接口
  uploadProps.value.id = localStorage.getItem('courseId');
  // 遍历附件列表，修改相关字段
  uploadProps.value.attachmentIdList.forEach(ele => {
    ele.allowDownload = newFile.value.allowDownload ? 1 : 0;
    ele.attachmentFolderId = selectedDirectory.value ? selectedDirectory.value : null;
  });

  await resourceexam(uploadProps.value).then(res => {
    uploadProps.value = {
    id: localStorage.getItem('courseId'),
    attachmentIdList: []
  }
    uploadDialogVisible.value = false;
    getexamList()
    updateFile()
    
  })
}

function openEditDialog(directory) {
  currentDirectory.value = { ...directory };
  editDialogVisible.value = true;
  console.log('bianji:'+editDialogVisible.value +"::"+currentDirectory.value);
  
}

async function updateDirectory() {
  //判断不能为空
  if (!currentDirectory.value.name.trim()) {
    alert('文件夹名称不能为空');
    return;
  }
  // 发送请求更新文件夹信息
  console.log('编辑文件夹:' +JSON.stringify(currentDirectory.value));
  if (currentDirectory.value) {
    const { id, name } = currentDirectory.value;
    const response = await editAttachmentfolder({
      id: id,
      folderName: name
    });

    alert('编辑成功');
    uploadFile()
    editDialogVisible.value = false;
  }
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

/**
 * 
 * @param item 删除文件夹
 */
async function handleDeleteDirectory(item) {
  // 显示确认框，提示用户删除文件夹会删除所有文件
  const userConfirmed = window.confirm('此操作将删除文件夹及其所有文件，确定删除吗？');
  if (userConfirmed) {
    // 发送请求删除文件夹
    const response = await deleteForder(item.id)
    console.log('删除文件夹:'+response);
    alert('删除成功')
    //刷新页面
    if (item.id === selectedDirectory.value) {
      selectedDirectory.value = '';
    }
    uploadFile()
  }

}

/**
 * 删除文件
 * @param item 
 */
 async function handleDeleteFile(item) {
  await deleteFile(item.id).then(res => {
    alert('删除成功')
  })
  getexamList()
  updateFile()
}

function handleDirectorySelectionChange(val) {
  selectedDirectories.value = val;
}

function handleFileSelectionChange(val) {
  selectedFiles.value = val;
}

function batchDelete() {
  // 删除选中的文件夹
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

  // 清空选中的文件夹和文件
  selectedDirectories.value = [];
  selectedFiles.value = [];
}

  // 查询文件列表
  async function getexamList() {
    let id = localStorage.getItem('courseId')
    await getexam(id).then(res => {
      // directoryData.value = res.data
      directoryData.value = res
      console.log(res)
    })
    handleSearch()
  }

function togglePublish(item) {
  item.published = !item.published;
}

const sortedFiles = computed(() => {
  if (!filteredFiles.value)return
  let files = [...filteredFiles.value];
  if (sortOrder.value === 'name') {
    files.sort((a, b) => a.name.localeCompare(b.name));
  } else if (sortOrder.value === 'uploadTime') {
    files.sort((a, b) => new Date(b.uploadTime) - new Date(a.uploadTime));
  }
  files.map(ele => {
    if(ele.allowDownload == 1) ele.allowDownload = true
    if(ele.allowDownload == 1) ele.allowDownload = true
  } )
  return files;
});
function getexamlist() {
  let id = localStorage.getItem('courseId')
  getexam(id).then(res => {
    console.log(res)
  })
}
getexamlist()
onMounted(() => {
  getexamList();
});
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