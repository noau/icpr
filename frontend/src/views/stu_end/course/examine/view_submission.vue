<template>
  <div class="view-submission">
    <el-card>
      <h2>{{ homework.title }}</h2>
      <br/>
      <el-row>
        <el-col :span="12">
          <el-form label-width="120px">
            <el-form-item label="作业内容">
              <el-input
                  type="textarea"
                  :value="homework.content"
                  disabled
                  rows="10"
              ></el-input>
            </el-form-item>
            <el-form-item label="附件">
              <ul class="uploaded-files">
                <li v-for="file in homework.attachments" :key="file.id">
                  <a :href="file.url" download>{{ file.name }}</a>
                </li>
              </ul>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="3"></el-col>
        <el-col :span="9">
          <el-form label-width="120px">
            <el-form-item label="得分：">
              <span>{{ homework.grade }}</span>
            </el-form-item>
            <el-form-item label="批改状态：">
              <span>{{ homework.reviewStatus }}</span>
            </el-form-item>
            <!-- <el-form-item label="老师评语：">
              <span>{{ homework.feedback }}</span>
            </el-form-item> -->
          </el-form>
        </el-col>
      </el-row>
    </el-card>
    <div class="button-container">
      <el-button round type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {viewSubmission} from '@/api/assignments.js';
import {getAttachmentUrl} from "@/api/common.js";

const route = useRoute();
const router = useRouter();
const homework = ref({
  title: '',
  grade: '',
  reviewStatus: '',
  content: '',
  attachments: [],
  feedback: '',
});

onMounted(async () => {
  const assignmentId = route.query.assignmentId;
  const studentId = route.query.studentId;

  try {
    const response = await viewSubmission({assignmentId, studentId});
    console.log(response);
    const {grade, feedback, gradedAt, content, attachments} = response;

    for (const attachment of attachments) {
      let response = await getAttachmentUrl(attachment.id);
      console.log(response);
      if (!response || !response.url) {
        alert(`附件${attachment.name}没有下载连接`)
        continue
      }
      attachment.url = response.url
    }

    console.log(attachments)

    homework.value = {
      title: route.query.title,
      grade: grade || '未评分',
      reviewStatus: gradedAt ? '已批改' : '未批改',
      content,
      attachments: attachments.map((file) => ({
        id: file.id,
        name: file.name,
        url: file.url, // Modify URL as per your backend setup
      })),
      feedback: feedback || '暂无评语',
    };
  } catch (error) {
    console.error('Failed to load submission:', error);
  }
});

function goBack() {
  router.push({path: '/stu-end/course/examine/stu-homework'});
}
</script>

<style scoped>
.view-submission {
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
  