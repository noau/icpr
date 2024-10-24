<template v-slot:header>
  <el-row :gutter="20" class="header" style="margin-top: -25px;">
    <el-col :span="8">
      <el-input v-model="searchQuery" placeholder="输入资源名称进行搜索" class="search-input" />
    </el-col>
    <el-col :span="4">
      <el-button round style="margin-right: 100px; margin-left: -80px; padding: 10px;" type="primary" @click="handleSearch">搜索</el-button>
    </el-col>
  </el-row>
  <div class="box-card">
    <el-card>
      <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto;">
        <el-table :data="paginatedData" style="width: 100%;" class="resource-table">
          <el-table-column prop="name" label="资源名称" width="600" align="center" header-align="center">
            <template #default="scope">
              <el-icon class="custom-icon-document"><Document /></el-icon>
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
import {getexam} from '@/api/course'

const tableData = ref([
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  { name: '历年试题11.pdf', url: 'https://example.com/file1.pdf' },
  { name: '历年试题12.pdf', url: 'https://example.com/file2.pdf' },
  
]);

const pageSize = ref(8);
const currentPage = ref(1);
const searchQuery = ref('');
const filteredData = ref([]);

// Filtered and paginated data
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredData.value.slice(start, start + pageSize.value);
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
    filteredData.value = tableData.value;
  } else {
    filteredData.value = tableData.value.filter(item =>
      item.name.includes(searchQuery.value)
    );
  }
}

filteredData.value = tableData.value;
function getexamList() {
  let id=localStorage.getItem('kcid')
  getexam(id).then(res => {
    console.log(res.attachmentIdList);
    
  }).catch(err => {
    console.log(err);
  });
}

getexamList();
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

