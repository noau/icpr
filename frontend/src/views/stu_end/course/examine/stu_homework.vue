<template>
  <div>
    <el-row :gutter="20" class="header" style="margin-top: -25px;">
      <el-col :span="8">
        <el-input v-model="searchQuery" placeholder="输入作业名称进行搜索" class="search-input" />
      </el-col>
      <el-col :span="4">
        <el-button round style="margin-right: 100px; margin-left: -80px; padding: 10px;" type="primary" @click="handleSearch">搜索</el-button>
      </el-col>
    </el-row>
    <div class="box-card">
      <el-card>
        <el-table :data="paginatedData" style="width: 100%">
          <!-- 作业标题 -->
          <el-table-column prop="title" label="作业标题" width="200"></el-table-column>

          <!-- 作业开始 -->
          <el-table-column prop="start" label="作业开始" width="180"></el-table-column>

          <!-- 作业截止 -->
          <el-table-column prop="end" label="作业截止" width="180"></el-table-column>

          <!-- 提交人数 -->
          <el-table-column prop="submitted" label="提交人数" width="100"></el-table-column>

          <!-- 提交时间 -->
          <el-table-column prop="submitTime" label="提交时间" width="150"></el-table-column>

          <!-- 得分 -->
          <el-table-column prop="score" label="得分" width="100"></el-table-column>

          <!-- 批改状态 -->
          <el-table-column prop="reviewStatus" label="批改状态" width="100"></el-table-column>

          <!-- 操作 -->
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button round type="text" @click="handleSubmit(scope.row)">提交</el-button>
              <el-button round type="text" @click="viewDetails(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
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

    <!-- 提交作业的弹窗 -->
    <el-dialog title="文件内容" v-model="dialogVisible" width="500px">
      <el-form ref="submitForm" :model="formData">
        <el-form-item label="作业内容">
          <el-input
            type="textarea"
            v-model="formData.content"
            placeholder="请输入3000字以内的作业内容！"
            rows="5"
          ></el-input>
        </el-form-item>

        <el-form-item label="上传文件">
          <el-upload
            class="upload-demo"
            action="#"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :file-list="fileList"
          >
            <template v-slot:trigger>
              <el-button round style="padding: 10px;" size="small" type="primary">点击上传</el-button>
            </template>
            <template v-slot:tip>
              <div class="el-upload__tip">
                允许上传任意格式的文件
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>

      <!-- 按钮 -->
      <template v-slot:footer>
        <span class="dialog-footer">
          <el-button round style="padding: 10px;" type="primary" @click="submitAssignment">确定</el-button>
          <el-button round style="padding: 10px;" @click="dialogVisible = false">取消</el-button>      
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router'; // Import Vue Router

const router = useRouter(); // Use the router instance

const tableData = ref([
  { id: '1', title: '班级', start: '2024-01-13', end: '2024-02-06', submitted: '0/27', submitTime: '未提交', score: '未公布成绩', reviewStatus: '未批改' },
  { id: '2', title: '00', start: '2024-01-03', end: '2024-02-23', submitted: '0/27', submitTime: '未提交', score: '未公布成绩', reviewStatus: '未批改' },
  // More data...
]);

const dialogVisible = ref(false);
const formData = ref({
  content: '',
  file: null
});
const fileList = ref([]);
const searchQuery = ref('');
const currentPage = ref(1);
const pageSize = ref(8);
const searchResult = ref([]); // 用于存储搜索结果

const filteredData = computed(() => {
  return searchResult.value;
});

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredData.value.slice(start, start + pageSize.value);
});

function handleSubmit(row) {
  console.log('Submit clicked for:', row);
  dialogVisible.value = true;
}

function viewDetails(row) { 
  console.log('Viewing details for:', row);
  // 假设 row 中有一个 id 或 title，作为作业的唯一标识
  const assignmentId = row.id; // 或者用 row.title 作为标识
  // 跳转到作业详情页面，传递作业 ID 作为路由参数
  router.push({ 
    path: `/stu-end/course/examine/homework-details/${assignmentId}` 
  });
}

function handlePreview(file) {
  console.log('Previewing file:', file);
}

function handleRemove(file) {
  console.log('Removing file:', file);
}

function submitAssignment() {
  console.log('Submitting assignment:', formData.value);
  dialogVisible.value = false;
}

function handleCurrentChange(page) {
  currentPage.value = page;
}

function handleSearch() {
  currentPage.value = 1;
  if (!searchQuery.value) {
    searchResult.value = tableData.value;
  } else {
    searchResult.value = tableData.value.filter(item =>
      item.title.includes(searchQuery.value)
    );
  }
}

// 初始化时显示所有数据
searchResult.value = tableData.value;
</script>

<style scoped>
.el-table th {
  font-weight: bold;
  text-align: center;
}

.el-table td {
  text-align: center;
}

.el-button {
  padding: 0;
}

.dialog-footer {
  text-align: right;
}

.box-card {
  margin: 10px 20px 20px;
}

.search-input {
  flex: 1;
  width: 300px;
  margin-left: 20px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>