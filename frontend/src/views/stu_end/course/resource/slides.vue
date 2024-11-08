<template>
  <div> <!-- Root element added here -->
    <el-row :gutter="20" class="header" style="margin-top: -25px;">
      <el-col :span="6">
        <el-select v-model="selectedFolder" placeholder="选择文件夹" @change="getpptlist" style="width: 200px;">
          <el-option
            v-for="folder in folderOptions"
            :key="folder.id"
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
          <el-table :data="paginatedData" style="width: 100%;" class="resource-table">
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

const tableData = ref([]);
const folderOptions = ref([
  { id: 'folder1', name: 'Folder 1' },
  { id: 'folder2', name: 'Folder 2' },
  // Add more folder options here
]);
const selectedFolder = ref('');
const searchQuery = ref('');
const pageSize = ref(8);
const currentPage = ref(1);

const filteredData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return tableData.value.slice(start, start + pageSize.value);
});

function handleSearch() {
  currentPage.value = 1;
  tableData.value = tableData.value.filter(item =>
    item.name.includes(searchQuery.value)
  );
}

async function getpptlist() {
  const id = selectedFolder.value;
  const response = await getppt(id);
  tableData.value = response.data; // Assuming the API returns an array of PPT data in response.data
}

// Fetch PPT list when a folder is selected
watch(selectedFolder, getpptlist);

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
