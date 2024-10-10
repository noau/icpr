<template v-slot:header>
  <el-row :gutter="20" class="header" style="margin-top: -25px; margin-left: 10px; margin-right: 10px;">
    <el-col :span="12" style="display: flex; align-items: center;">
      <el-select v-model="sortOrder" placeholder="选择排序方式" style="width: 200px; margin-right: 15px;" @change="handleSortChange">
        <el-option label="按文件名称排序" value="name" />
        <el-option label="按上传时间排序" value="uploadTime" />
      </el-select>
      <el-button round type="primary" @click="openUploadDialog" style="padding:10px;">上传文件</el-button>
      <el-button circle type="primary" @click="batchDelete" style="padding:10px;">
        <el-icon><Delete /></el-icon>
      </el-button>
    </el-col>
    <el-col :span="12" style="display: flex; justify-content: flex-end; align-items: center;">
      <el-input v-model="searchQuery" placeholder="输入历年试题名称进行搜索" class="search-input" style="margin-right: 15px;" />
      <el-button circle type="primary" @click="handleSearch" style="padding: 10px;">
        <el-icon><Search /></el-icon>
      </el-button>
    </el-col>
  </el-row>
  <div class="box-card">
    <el-card>
      <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto;">
        <el-table :data="paginatedData" style="width: 100%;" class="resource-table" @selection-change="handleFileSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="资源名称" width="350" align="center" header-align="center">
            <template #default="scope">
              <el-icon class="custom-icon-document"><Document /></el-icon>
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
              <el-button type="text" @click="handleDownload(scope.row)">下载</el-button>
              <el-button type="text" @click="handleDeleteFile(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pagination-container">
        <el-pagination
          layout="prev, pager, next"
          :total="filteredData.length"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { Document, Share, Delete, Search } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

const handleShare = (file) => {
  if (navigator.share) {
    navigator.share({
      title: '分享文件',
      text: `我想分享文件: ${file.name}`,
      url: file.url,
    })
    .then(() => console.log('分享成功'))
    .catch(error => console.error('分享失败:', error));
  } else {
    const shareLink = document.createElement('textarea');
    shareLink.value = `我想分享文件: ${file.name}\n链接: ${file.url}`;
    document.body.appendChild(shareLink);
    shareLink.select();
    document.execCommand('copy');
    document.body.removeChild(shareLink);
    ElMessage.success('分享链接已复制到剪贴板');
  }
};

const tableData = ref([
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf', allowDownload: true, published: true },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf', allowDownload: true, published: true },
  // ... 其他数据 ...
]);

const pageSize = ref(8);
const currentPage = ref(1);
const searchQuery = ref('');
const filteredData = ref([]);
const selectedFiles = ref([]);
const sortOrder = ref('name'); // 默认按文件名称排序

// 过滤和分页数据
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredData.value.slice(start, start + pageSize.value);
});

const sortedFiles = computed(() => {
  let files = [...filteredData.value];
  if (sortOrder.value === 'name') {
    files.sort((a, b) => a.name.localeCompare(b.name));
  } else if (sortOrder.value === 'uploadTime') {
    files.sort((a, b) => new Date(b.uploadTime) - new Date(a.uploadTime));
  }
  return files;
});

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
    filteredData.value = tableData.value;
  } else {
    filteredData.value = tableData.value.filter(item =>
      item.name.includes(searchQuery.value)
    );
  }
}

function handleFileSelectionChange(val) {
  selectedFiles.value = val;
}

function handleDeleteFile(item) {
  const index = tableData.value.indexOf(item);
  if (index !== -1) {
    tableData.value.splice(index, 1);
  }
}

function openUploadDialog() {
  // 打开上传文件对话框的逻辑
  alert('上传文件对话框打开');
}

function batchDelete() {
  // 批量删除选中的文件
  selectedFiles.value.forEach(file => {
    const index = tableData.value.indexOf(file);
    if (index !== -1) {
      tableData.value.splice(index, 1);
    }
  });
  selectedFiles.value = [];
}

function handleSortChange() {
  currentPage.value = 1;
  // 触发排序逻辑
}

filteredData.value = tableData.value;
</script>

<style scoped>
.box-card {
  margin: 20px;
  margin-top: 10px;
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
</style>