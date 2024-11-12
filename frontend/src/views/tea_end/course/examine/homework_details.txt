<template>
  <div class="homework-details">
    <el-card>
      <h2>{{ homework.title }}</h2>
      <br>
      <el-row>
        <el-col :span="12">
          <el-form label-width="120px">
            <el-form-item label="作业内容">
              <el-input type="textarea" :value="homework.content" disabled rows="10"></el-input>
            </el-form-item>
            <el-form-item label="附件">
              <ul class="uploaded-files">
                <li v-for="file in homework.fileList" :key="file.name">
                  <a :href="file.url" download>{{ file.name }}</a>
                </li>
              </ul>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="3">
        </el-col>
        <el-col :span="9">
          <el-form label-width="120px">
            <el-form-item label="得分：">
              <span>{{ homework.score }}</span>
            </el-form-item>
            <el-form-item label="批改状态：">
              <span>{{ homework.reviewStatus }}</span>
            </el-form-item>
            <el-form-item label="老师评语：">
              <span>{{ homework.teacherComment }}</span>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
    <!-- <br> -->
    <div class="button-container">
      <el-button round type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const homework = ref({
  title: '',
  score: '',
  reviewStatus: '',
  content: '',
  fileList: [],
  teacherComment: '',
});

function fetchHomeworkDetails(id) {
  // 模拟根据作业 ID 获取作业详情
  const assignments = {
    '1': {
      title: '作业标题1',
      score: '90',
      reviewStatus: '已批改',
      content: '作业内容1。',
      fileList: [
        { name: 'file1.pdf', url: 'https://example.com/file1.pdf' },
        { name: 'file2.pdf', url: 'https://example.com/file2.pdf' },
      ],
      teacherComment: '做得很好！',
    },
    '2': {
      title: '作业标题 2',
      score: '85',
      reviewStatus: '已批改',
      content: '作业内容2。',
      fileList: [
        { name: 'file3.pdf', url: 'https://example.com/file3.pdf' },
        { name: 'file4.pdf', url: 'https://example.com/file4.pdf' },
      ],
      teacherComment: '需要改进。',
    },
    // 可以添加更多作业
  };

  // 根据 ID 返回对应的作业详情，若未找到则返回默认信息
  return assignments[id] || {
    title: '未知作业',
    score: '',
    reviewStatus: '',
    content: '没有找到作业内容。',
    fileList: [],
    teacherComment: '无评语。',
  };
}

onMounted(() => {
  const homeworkId = route.params.id; // Get the homework ID from the route parameters
  homework.value = fetchHomeworkDetails(homeworkId);
});

function goBack() {
  router.push({ path: '/tea-end/course/examine/stu-homework' }); // Adjust based on your route setup
}
</script>

<style scoped>
.homework-details {
  padding: 20px;
}

.el-card {
  margin-bottom: 20px;
  height: 500px;
}

.uploaded-files {
  list-style-type: none;
  padding: 0;
}

.uploaded-files li {
  margin: 5px 0;
}

.uploaded-files a {
  color: #409EFF;
  text-decoration: none;
}

.uploaded-files a:hover {
  text-decoration: underline;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
