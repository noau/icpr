<template>
  <div> <!-- Root element added here -->
    <el-row :gutter="20" class="header" style="margin-top: -25px;">
      <el-col :span="6">
        <el-select v-model="selectedFolder" placeholder="选择文件夹" @change="getpptlist(false)" style="width: 200px;">
          <el-option
            v-for="folder in folderOptions"
            :key="folder.name"
            :label="folder.name"
            :value="folder.id"
          />
        </el-select>
      </el-col>
      <el-col :span="8">
        <el-input v-model="searchQuery" placeholder="输入资源名称进行搜索" class="search-input" />
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" @click="handleSearch">搜索</el-button>
      </el-col>
    </el-row>
    
    <div class="box-card">
      <el-card>
        <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto;">
          <el-table :data="filteredData" style="width: 100%;" class="resource-table">
            <el-table-column prop="name" label="资源名称" width="600" align="center" header-align="center">
              <template #default="scope">
                <el-icon class="custom-icon-document">
                  <Document />
                </el-icon>
                <span>{{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="500" align="center" header-align="center">
              <template #default="scope">
                <el-button round type="text" icon="el-icon-view" @click="handlePreview(scope.row)">预览</el-button>
                <el-button round type="text" icon="el-icon-download" @click="handleDownload(scope.row)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <div class="pagination-container">
          <el-pagination layout="prev, pager, next" :total="filteredData.length" :page-size="pageSize"
            :current-page="currentPage" @current-change="handleCurrentChange" />
        </div>
      </el-card>
    </div>
  </div> <!-- Closing root element -->
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { getppt } from '@/api/course';
import axios from 'axios';

const tableData = ref([]);
const allFiles = ref([]);
const folderOptions = ref([]);
const selectedFolder = ref('');
const searchQuery = ref('');
const pageSize = ref(5);
const currentPage = ref(1);

// 过滤和分页数据
const filteredData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return allFiles.value.slice(start, start + pageSize.value);
});

// 搜索
function handleSearch() {
  currentPage.value = 1;
  if (!searchQuery.value) {
    allFiles.value = tableData.value;
  } else {
    allFiles.value = tableData.value.filter(file =>
      file.name.includes(searchQuery.value)
    );
  }
}

function handleCurrentChange(page) {
  currentPage.value = page;
}
/**
 * 预览资源
 * @param row 
 */
function handlePreview(row) {
  // 获取 URL
  const url = row.url;
    if (!url) {
      this.$message.error('该资源没有预览链接');
      return;
    }
    // 判断文件类型
    const fileExtension = url.split('.').pop().toLowerCase();
    if (['jpg', 'jpeg', 'png', 'gif', 'bmp'].includes(fileExtension)) {
      // 如果是图片文件，直接在新窗口打开
      window.open(url, '_blank');
    } else if (['pdf'].includes(fileExtension)) {
      // 如果是 PDF 文件，直接在新窗口打开
      window.open(url, '_blank');
    } else if (['doc', 'docx', 'ppt', 'pptx'].includes(fileExtension)) {
      // 如果是 Office 文件，可以通过 Google Docs Viewer 打开
      const viewerUrl = `https://docs.google.com/viewer?embedded=true&url=${encodeURIComponent(url)}`;
      window.open(viewerUrl, '_blank');
    } else {
      // 对于其他不支持的文件类型，显示警告信息
      alert('不支持预览该文件类型');
    }
}

/**
 * 通过链接下载文件
 * @param row 
 */
function handleDownload(row) {
  // 获取文件 URL 和文件名
  const url = row.url; // 假设 row 中包含一个文件的 URL
  const name = row.name || '下载文件'; // 默认文件名，如果没有提供 name 属性，使用默认值

  // 检查 URL 是否有效
  if (url) {
    downloadFile(url,name)
  } else {
   alert('文件 URL 不存在或无效!');
  }
}
/**
 * 根据路径下载文件
 * @param pdfUrl 
 * @param name 
 */
async function downloadFile(url,name) {
  const response = await axios({
    url,
    method: 'GET',
    responseType: 'blob', // 必须指定为blob类型才能下载
  });
  url = window.URL.createObjectURL(new Blob([response.data]));
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download',  name);
  document.body.appendChild(link);
  link.click();
}

/**
 * 获取文件夹下拉列表包含表格列表数据
 * @param flag true:初始化 false:切换文件夹
 */
async function getpptlist(flag) {
  if(!flag){
    const folder = folderOptions.value.find(ele => ele.id == selectedFolder.value)
    if(folder.files.length > 0){
     tableData.value = folder.files
     allFiles.value = folder.files
    }else{
      tableData.value = []
      allFiles.value = []
    }
  }else{
    const id = localStorage.getItem('courseId');
    await getppt(id).then(res => {
        console.log(res)
        folderOptions.value = res
        tableData.value = res[0].files
        allFiles.value = res[0].files
        selectedFolder.value = res[0].id
    })
  }
}

getpptlist(true)
</script>

<style scoped>
.box-card {
  margin: 20px;
  margin-top: 10px;
}

.search-input {
  flex: 1;
  width: 300px;
  margin-left: 20px;
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
</style>
