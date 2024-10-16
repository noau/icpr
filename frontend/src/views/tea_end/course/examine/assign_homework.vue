<template>
  <el-form :model="homeworkForm" label-width="120px">
    <el-form-item label="作业标题">
      <el-input style="margin-right: 100px; width: 400px;" v-model="homeworkForm.title" placeholder="请输入作业标题"></el-input>
    </el-form-item>
    
    <el-form-item label="作业要求">
      <div class="upload-container">
        <el-upload
          class="upload-demo"
          action="#"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false"
          multiple>
          <template #trigger>
            <el-button round type="primary">添加附件</el-button>
          </template>
        </el-upload>
        <div class="el-upload__tip">支持所有格式</div>
      </div>
      <v-md-editor 
        v-model="homeworkForm.requirements" 
        class="markdown-editor" 
        placeholder="请输入作业要求" 
      />
    </el-form-item>

    <br>
    <el-form-item>
      <span style="margin-left: -68px; margin-right: 13px;">作业满分</span>
      <el-input-number v-model="homeworkForm.fullScore" :min="0" label="作业满分" placeholder="作业满分" style="width: 150px; margin-right: 60px;"></el-input-number>
      <span style="margin-right: 13px;">补交满分</span>
      <el-input-number v-model="homeworkForm.lateFullScore" :min="0" label="补交满分" placeholder="补交满分" style="width: 150px; margin-right: 60px;"></el-input-number>    
      <el-checkbox v-model="homeworkForm.allowResubmission" style="margin-right: 60px;">允许重复提交</el-checkbox>
      <el-checkbox v-model="homeworkForm.publishGrades">公布作业成绩</el-checkbox>
    </el-form-item>

    <el-form-item label="开始时间">
      <el-date-picker v-model="homeworkForm.startTime" type="datetime" placeholder="选择开始时间"></el-date-picker>
      <span class="form-tip">（开始时间之前的作业不会显示在学生的列表中）</span>
    </el-form-item>

    <el-form-item label="正常截止时间" style="margin-left: 28px;">
      <el-date-picker v-model="homeworkForm.regularDeadline" type="datetime" placeholder="选择正常截止时间"></el-date-picker>
      <span class="form-tip">（晚于正常截止时间提交的作业将被标记为“补交”，但学生依然可以提交作业）</span>
    </el-form-item>

    <el-form-item label="补交截止时间" style="margin-left: 28px;">
      <el-date-picker v-model="homeworkForm.lateDeadline" type="datetime" placeholder="选择补交截止时间"></el-date-picker>
      <span class="form-tip">（在补交截止时间之后的作业，不可提交）</span>
    </el-form-item>

    <el-form-item>
      <el-checkbox v-model="homeworkForm.peerReviewEnabled" style="margin-left: -67px;">互评任务</el-checkbox>
      <el-date-picker v-if="homeworkForm.peerReviewEnabled" v-model="homeworkForm.peerReviewStart" type="datetime" placeholder="开始时间" style="margin-left: 20px;"></el-date-picker>
      <el-date-picker v-if="homeworkForm.peerReviewEnabled" v-model="homeworkForm.peerReviewEnd" type="datetime" placeholder="截止时间" style="margin-left: 20px; margin-right: 30px;"></el-date-picker>
      <span v-if="homeworkForm.peerReviewEnabled" style="margin-right: 3px;">学生最低互评数量</span>
      <el-input-number v-if="homeworkForm.peerReviewEnabled" v-model="homeworkForm.minReviews" :min="1" label="最少互评数量" style="width: 150px; margin-left: 20px;"></el-input-number>
    </el-form-item>

    <br>
    <el-form-item>      
      <el-button round type="primary" @click="submitHomework" style="margin-left: 400px;">发布</el-button>
      <el-button round @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import { getIssue,getChange } from '@/api/assignments.js'   

// 使用 github 主题
VMdEditor.use(githubTheme);

const route = useRoute();

const homeworkForm = ref({
  title: '',
  requirements: '',
  fullScore: 100,
  lateFullScore: 100,
  allowResubmission: false,
  publishGrades: false,
  startTime: '',
  regularDeadline: '',
  lateDeadline: '',
  peerReviewEnabled: false,
  peerReviewStart: '',
  peerReviewEnd: '',
  minReviews: 1
});

const fileList = ref([]);

// 示例作业列表
const homeworkList = ref([
  { id: 1, title: '作业1', requirements: '要求1', fullScore: 100, lateFullScore: 80, allowResubmission: true, publishGrades: true, startTime: '2023-10-01 10:00', regularDeadline: '2023-10-10 23:59', lateDeadline: '2023-10-15 23:59', peerReviewEnabled: true, peerReviewStart: '2023-10-11 00:00', peerReviewEnd: '2023-10-14 23:59', minReviews: 2 },
  { id: 2, title: '作业2', requirements: '要求2', fullScore: 100, lateFullScore: 90, allowResubmission: false, publishGrades: false, startTime: '2023-10-05 10:00', regularDeadline: '2023-10-15 23:59', lateDeadline: '2023-10-20 23:59', peerReviewEnabled: false, peerReviewStart: '', peerReviewEnd: '', minReviews: 1 }
]);

//布置作业

// 


const handlePreview = (file) => {
  console.log('预览文件:', file);
};

const handleRemove = (file, fileList) => {
  console.log('移除文件:', file, fileList);
};

const submitHomework = async() => {
  console.log('发布作业:', homeworkForm.value);
  const res = await getIssue({id:localStorage.getItem('userId')}) 
  // 修改
  // const res = await getChange({id:localStorage.getItem('userId')})  
  // Implement submission logic here
};

const resetForm = () => {
  homeworkForm.value = {
    title: '',
    requirements: '',
    fullScore: 100,
    lateFullScore: 100,
    allowResubmission: false,
    publishGrades: false,
    startTime: '',
    regularDeadline: '',
    lateDeadline: '',
    peerReviewEnabled: false,
    peerReviewStart: '',
    peerReviewEnd: '',
    minReviews: 1
  };
  fileList.value = [];
};

onMounted(() => {
  const homeworkId = route.query.id;
  if (homeworkId) {
    // Fetch homework data by ID and populate the form
    const homework = homeworkList.value.find(h => h.id === parseInt(homeworkId));
    if (homework) {
      homeworkForm.value = { ...homework };
    }
  }
});
</script>

<style scoped>
::v-deep .markdown-editor {
  margin-left: -150px !important;
  height: 700px !important;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-button {
  margin-right: 10px;
}

.upload-demo .el-upload__tip {
  font-size: 12px;
  color: #666;
}

.upload-container {
  display: flex;
  align-items: center;
  margin-left: 820px; /* 向右移动 */
}

.upload-container .el-upload__tip {
  margin-left: 5px; /* 按钮和提示之间的间距 */
}

.form-tip {
  margin-left: 10px;
  font-size: 12px;
  color: #666;
}
</style>