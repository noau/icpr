<template v-slot:header>
  <div class="box-card">
    <el-card>
      <div class="forum-list" id="data" style="max-height: 600px; overflow-y: auto;">
        <ul>
          <li v-for="(item, index) in paginatedData" :key="index">
            <div class="forum-list-info">
              <img :src="item.avatar" alt="avatar"
                style="width: 30px; height: 0px; margin-right: 10px; margin-left: 20px;  border-radius: 0%;" />
              <h3 class="student-info">
                学号：{{ item.studentId }} <span class="spacing"></span> 姓名: {{ item.name }}
              </h3>
            </div>
            <hr v-if="index < paginatedData.length - 1" />
          </li>
        </ul>
      </div>
      <div class="pagination-container">
        <el-pagination layout="prev, pager, next" :total="tableData.length" :page-size="pageSize"
          :current-page="currentPage" @current-change="handleCurrentChange" />
      </div>
    </el-card>
  </div>
</template>



<script setup>
import { ref, computed } from 'vue';

const tableData = ref([
  { studentId: '22301126', name: 'Tom', avatar: 'https://example.com/avatar2.jpg' },
  { studentId: '22301127', name: 'Tom', avatar: 'https://example.com/avatar2.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },
  { studentId: '22301128', name: 'Tom', avatar: 'https://example.com/avatar3.jpg' },

]);


const pageSize = ref(10);
const currentPage = ref(1);

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return tableData.value.slice(start, start + pageSize.value);
});

function handleCurrentChange(page) {
  currentPage.value = page;
}
</script>

<style scoped>
.box-card {
  margin: 20px;
}

.forum-list {
  padding: 0;
}

.forum-list ul {
  list-style-type: none;
  /* 用更具体的选择器 */
}


.forum-list-info {
  display: flex;
  justify-content: flex-start;
  /* 左对齐 */
  align-items: center;
  margin-top: 10px;
}

.student-info {
  font-size: 16px;
  font-weight: bold;
  margin-left: 10px;
  /* 减少左边距 */
}

img {
  margin-left: 10px;
  /* 如果头像过远，可以减少头像左边距 */
}

.spacing {
  margin: 0 100px;
}

hr {
  margin: 10px 0;
  border: 1px solid rgb(242, 238, 238);
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
