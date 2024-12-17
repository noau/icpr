<template>
  <el-form :model="homeworkForm" label-width="120px">
    <el-form-item label="作业标题">
      <el-input style="margin-right: 100px; width: 400px;" v-model="homeworkForm.title"
                placeholder="请输入作业标题"></el-input>
    </el-form-item>

    <el-form-item label="作业描述">
      <div class="upload-container">
        <el-upload
            class="upload-demo"
            :limit="1"
            action="http://localhost:8080/attachment/upload"
            :headers="headers"
            :before-upload="beforeUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :file-list="fileList"
            :accept="acceptedFileTypes"
            ref="uploadRef"
        >
          <template #trigger>
            <el-button round type="primary">添加附件</el-button>
          </template>
        </el-upload>
        <div class="el-upload__tip">支持格式：docx、doc、pdf、jpg、png</div>
      </div>
      <v-md-editor
          v-model="homeworkForm.description"
          class="markdown-editor"
          style="margin-right: 500px"
          placeholder="请输入作业描述"
      />
    </el-form-item>

    <el-form-item>
      <span style="margin-left: -68px; margin-right: 13px;">作业满分</span>
      <el-input-number v-model="homeworkForm.fullGrade" min="0" label="作业满分" placeholder="作业满分"
                       style="width: 150px; margin-right: 60px;"></el-input-number>
      <!-- <span style="margin-right: 13px;">补交满分</span> -->
      <!-- <el-input-number v-model="homeworkForm.delayedGrade" :min="0" label="补交满分" placeholder="补交满分"
                       style="width: 150px; margin-right: 60px;"></el-input-number> -->
      <!-- <el-checkbox v-model="homeworkForm.multipleSubmission" style="margin-right: 60px;">允许重复提交</el-checkbox> -->
      <!-- <el-checkbox v-model="homeworkForm.publishGrade">公布作业成绩</el-checkbox> -->
    </el-form-item>

    <el-form-item label="开始时间">
      <el-date-picker v-model="homeworkForm.start" type="datetime" placeholder="选择开始时间"></el-date-picker>
    </el-form-item>

    <el-form-item label="截止时间" style="margin-left: 28px;">
      <el-date-picker v-model="homeworkForm.end" type="datetime" placeholder="选择正常截止时间"></el-date-picker>
    </el-form-item>

    <!-- <el-form-item label="补交截止时间" style="margin-left: 28px;">
      <el-date-picker v-model="homeworkForm.latestEnd" type="datetime" placeholder="选择补交截止时间"></el-date-picker>
    </el-form-item> -->

    <el-form-item>
      <el-checkbox v-model="homeworkForm.requirePeerReview" style="margin-left: -67px;">互评任务</el-checkbox>
      <el-date-picker v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.peerReviewStart" type="datetime"
                      placeholder="开始时间" style="margin-left: 20px;"></el-date-picker>
      <el-date-picker v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.peerReviewEnd" type="datetime"
                      placeholder="截止时间" style="margin-left: 20px; margin-right: 30px;"></el-date-picker>
      <span v-if="homeworkForm.requirePeerReview" style="margin-right: 3px;">学生最低互评数量</span>
      <el-input-number v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.minPeerReview" :min="1"
                       label="最少互评数量" style="width: 150px; margin-left: 20px;"></el-input-number>
    </el-form-item>

    <el-form-item>
      <el-button round type="primary" @click="submitHomework" style="margin-left: 400px;">
        {{ isEditing ? '保存修改' : '布置作业' }}
      </el-button>
      <el-button round @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import {ref, onMounted, onBeforeMount} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import {getAssignmentsInfo, getIssue, getChange} from '@/api/assignments.js';
import moment from 'moment';
import {useCourseStore} from '@/stores/course'; // 引入 Pinia store

VMdEditor.use(githubTheme);

const route = useRoute();
const router = useRouter();
const courseStore = useCourseStore(); // 使用 courseStore

const homeworkForm = ref({
  courseId: '', // 将 courseId 加入表单
  title: '',
  description: '',
  start: '',
  end: '',
  isPrivate: 0,
  fullGrade: 100,
  delayedGrade: 100,
  latestEnd: '',
  multipleSubmission: 0,
  publishGrade: 0,
  requirePeerReview: false,
  peerReviewStart: '',
  peerReviewEnd: '',
  minPeerReview: 1,
  attachments: []
});

const fileList = ref([]);
const headers = {
  Authorization: localStorage.getItem('token')
};
const acceptedFileTypes = ".doc,.docx,.pdf,.jpg,.png";
const isEditing = ref(false); // 区分布置作业和修改作业

const beforeUpload = (file) => {
  const isAllowedSize = file.size / 1024 / 1024 < 2048;
  if (!isAllowedSize) {
    alert('文件大小不能超过2G！');
    return false;
  }
  return true;
};

const handleUploadSuccess = (response, file) => {
  homeworkForm.value.attachments.push({
    name: file.name,
    url: response.url,
    id: response.id
  });
};

const handleUploadError = () => {
  alert('上传失败');
};

const handlePreview = (file) => {
  console.log('预览文件:', file);
};

const handleRemove = (file, fileList) => {
  homeworkForm.value.attachments = homeworkForm.value.attachments.filter(
      (attachment) => attachment.id !== file.id
  );
};

const loadAssignmentDetails = async () => {
  // 获取并设置 courseId
  let courseId = courseStore.getCourseId;
  if (!courseId) {
    courseId = localStorage.getItem("courseId");
    if (courseId) {
      console.log(courseId);

      courseStore.setCourseId(courseId); // 将 courseId 存储到 Pinia
    }
  }

  if (courseId) {
    homeworkForm.value.courseId = courseId; // 将 courseId 赋值到表单
  } else {
    console.error("课程 ID 为空，请确保课程 ID 已正确设置");
  }

  const assignmentId = route.query.id;
  if (assignmentId) {
    isEditing.value = true;
    try {
      const res = await getAssignmentsInfo({id: assignmentId});
      console.log(res);

      homeworkForm.value = {
        ...homeworkForm.value,
        ...res, // 展开 API 返回的数据
        requirePeerReview: res.requirePeerReview == 1,
        start: res.start ? moment(res.start).toDate() : '',
        end: res.end ? moment(res.end).toDate() : '',
        peerReviewStart: res.peerReviewStart ? moment(res.peerReviewStart).toDate() : '',
        peerReviewEnd: res.peerReviewEnd ? moment(res.peerReviewEnd).toDate() : '',
        // attachments: res.attachments === null ? [] : res.attachments
        attachments: []

      };
      console.log(homeworkForm.value);
      
      fileList.value = [];
    } catch (error) {
      console.error("加载作业详情失败:", error);
    }
  }
};

const submitHomework = async () => {
  // console.log('Homework Form:', homeworkForm.value);
  //互评的开始时间不能早于作业的截止时间
  // console.log('sdfsdf'+homeworkForm.value.end.getTime());
  if(!homeworkForm.value.title) {
    alert('作业标题不能为空')
    return
  }
  if(!homeworkForm.value.end) {
    alert('结束时间不能为空')
    return
  }
  if(!homeworkForm.value.start) {
    alert('开始时间不能为空')
    return
  }
  if(homeworkForm.value.requirePeerReview) {
    if(!homeworkForm.value.peerReviewStart || !homeworkForm.value.peerReviewEnd) {
      alert('互评开始和结束时间不能为空')
      return
    }
  }
  if(homeworkForm.value.end.getTime() <= homeworkForm.value.start.getTime()) {
    alert('作业的提交截止时间不能早于提交的开始时间')
    return
  }
  if(homeworkForm.value.requirePeerReview && (homeworkForm.value.peerReviewEnd.getTime() <= homeworkForm.value.peerReviewStart.getTime())) {
    alert('互评截止时间不能早于互评开始时间')
    return
  }
  if(homeworkForm.value.peerReviewStart && homeworkForm.value.peerReviewStart.getTime() <= homeworkForm.value.end.getTime()) {
    alert('互评开始时间不能早于作业的截止时间')
    return
  }
  const data = {
    ...homeworkForm.value,
    start: formatDateForSQL(homeworkForm.value.start),
    end: formatDateForSQL(homeworkForm.value.end),
    latestEnd: formatDateForSQL(homeworkForm.value.latestEnd),
    peerReviewStart: formatDateForSQL(homeworkForm.value.peerReviewStart),
    peerReviewEnd: formatDateForSQL(homeworkForm.value.peerReviewEnd),
    attachments: homeworkForm.value.attachments?.map(file => file.id) || []
  };
  data.requirePeerReview = data.requirePeerReview ? 1 : 0;
  data.publishGrade = data.publishGrade ? 1 : 0;
  data.multipleSubmission = data.multipleSubmission ? 1 : 0;

  try {
    if (isEditing.value) {
      await getChange(data);
      alert('作业修改成功');
    } else {
      console.log('fabuzuoye: '+JSON.stringify(data));
      
      await getIssue(data);
      alert('作业发布成功');
    }
    router.push("/tea-end/course/examine/homework"); // 发布/修改后跳转回作业列表
  } catch (error) {
    console.error('提交作业时出错:', error);
  }
};

const formatDateForSQL = (date) => {
  return date ? moment(date).format('YYYY-MM-DD HH:mm:ss') : null;
};

const resetForm = () => {
  if (isEditing.value) {
    loadAssignmentDetails();
  } else {
    homeworkForm.value = {
      courseId: courseStore.getCourseId || '', // 在重置时确保 courseId 存在
      title: '',
      description: '',
      start: '',
      end: '',
      isPrivate: 0,
      fullGrade: 100,
      delayedGrade: 100,
      latestEnd: '',
      multipleSubmission: 0,
      publishGrade: 0,
      requirePeerReview: 0,
      peerReviewStart: '',
      peerReviewEnd: '',
      minPeerReview: 1,
      attachments: []
    };
    fileList.value = [];
  }
};

onBeforeMount(loadAssignmentDetails);
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
  margin-left: 820px;
}

.upload-container .el-upload__tip {
  margin-left: 5px;
}

.form-tip {
  margin-left: 10px;
  font-size: 12px;
  color: #666;
}
</style>


<!-- <template>
 <el-form :model="homeworkForm" label-width="120px">
   <el-form-item label="作业标题">
     <el-input style="margin-right: 100px; width: 400px;" v-model="homeworkForm.title" placeholder="请输入作业标题"></el-input>
   </el-form-item>
   <el-form-item label="作业描述">
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
       v-model="homeworkForm.description"
       class="markdown-editor" style="margin-right: 500px"
       placeholder="请输入作业描述"
     />
     <div style="width: 500px;"></div>
   </el-form-item>

   <br>
   <el-form-item>
     <span style="margin-left: -68px; margin-right: 13px;">作业满分</span>
     <el-input-number v-model="homeworkForm.fullGrade" :min="0" label="作业满分" placeholder="作业满分" style="width: 150px; margin-right: 60px;"></el-input-number>
     <span style="margin-right: 13px;">补交满分</span>
     <el-input-number v-model="homeworkForm.delayedGrade" :min="0" label="补交满分" placeholder="补交满分" style="width: 150px; margin-right: 60px;"></el-input-number>
     <el-checkbox v-model="homeworkForm.multipleSubmission" style="margin-right: 60px;">允许重复提交</el-checkbox>
     <el-checkbox v-model="homeworkForm.publishGrade">公布作业成绩</el-checkbox>
   </el-form-item>

   <el-form-item label="开始时间">
     <el-date-picker v-model="homeworkForm.start" type="datetime" placeholder="选择开始时间"></el-date-picker>
     <span class="form-tip">（开始时间之前的作业不会显示在学生的列表中）</span>
   </el-form-item>

   <el-form-item label="正常截止时间" style="margin-left: 28px;">
     <el-date-picker v-model="homeworkForm.end" type="datetime" placeholder="选择正常截止时间"></el-date-picker>
     <span class="form-tip">（晚于正常截止时间提交的作业将被标记为“补交”，但学生依然可以提交作业）</span>
   </el-form-item>

   <el-form-item label="补交截止时间" style="margin-left: 28px;">
     <el-date-picker v-model="homeworkForm.latestEnd" type="datetime" placeholder="选择补交截止时间"></el-date-picker>
     <span class="form-tip">（在补交截止时间之后的作业，不可提交）</span>
   </el-form-item>

   <el-form-item>
     <el-checkbox v-model="homeworkForm.requirePeerReview" style="margin-left: -67px;">互评任务</el-checkbox>
     <el-date-picker v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.peerReviewStart" type="datetime" placeholder="开始时间" style="margin-left: 20px;"></el-date-picker>
     <el-date-picker v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.peerReviewEnd" type="datetime" placeholder="截止时间" style="margin-left: 20px; margin-right: 30px;"></el-date-picker>
     <span v-if="homeworkForm.requirePeerReview" style="margin-right: 3px;">学生最低互评数量</span>
     <el-input-number v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.minPeerReview" :min="1" label="最少互评数量" style="width: 150px; margin-left: 20px;"></el-input-number>
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

// 使用 github 主题
VMdEditor.use(githubTheme);

import { useCourseStore } from '@/stores/course';
import { getIssue } from '@/api/assignments.js';

const courseStore = useCourseStore();

const homeworkForm = ref({
 courseId: '',  // 填入需要的课程ID
 title: '',
 description: '',
 start: '',
 end: '',
 isPrivate: 0,
 fullGrade: 100,
 delayedGrade: 100,
 latestEnd: '',
 multipleSubmission: 0,
 publishGrade: 0,
 requirePeerReview: 0,
 peerReviewStart: '',
 peerReviewEnd: '',
 minPeerReview: 1,
 attachments: []
});

const fileList = ref([]);

const handlePreview = (file) => {
 console.log('预览文件:', file);
};

const handleRemove = (file, fileList) => {
 console.log('移除文件:', file, fileList);
};

import moment from 'moment';

const formatDateForSQL = (date) => {
 if (!date) return null;
 return moment(date).isValid() ? moment(date).format('YYYY-MM-DD HH:mm:ss') : null;
};

const submitHomework = async () => {
 const data = {
   courseId: homeworkForm.value.courseId,
   title: homeworkForm.value.title,
   description: homeworkForm.value.description,
   start: formatDateForSQL(homeworkForm.value.start),
   end: formatDateForSQL(homeworkForm.value.end),
   isPrivate: homeworkForm.value.isPrivate ? 1 : 0,
   fullGrade: homeworkForm.value.fullGrade,
   delayedGrade: homeworkForm.value.delayedGrade,
   latestEnd: formatDateForSQL(homeworkForm.value.latestEnd),
   multipleSubmission: homeworkForm.value.multipleSubmission ? 1 : 0,
   publishGrade: homeworkForm.value.publishGrade ? 1 : 0,
   requirePeerReview: homeworkForm.value.requirePeerReview ? 1 : 0,
   peerReviewStart: formatDateForSQL(homeworkForm.value.peerReviewStart),
   peerReviewEnd: formatDateForSQL(homeworkForm.value.peerReviewEnd),
   minPeerReview: homeworkForm.value.minPeerReview,
   attachments: fileList.value.map(file => file.id)
 };

 try {
   const response = await getIssue(data);
   console.log('公开作业:', response.data);
   // 在成功后执行操作，例如提示成功或跳转页面
 } catch (error) {
   console.error('公开作业时出错:', error);
   // 在出错时执行操作，例如提示错误
 }
};



const resetForm = () => {
 homeworkForm.value = {
   courseId: courseStore.courseId || '',  // 重置时保留课程 ID
   title: '',
   description: '',
   start: '',
   end: '',
   isPrivate: 0,
   fullGrade: 100,
   delayedGrade: 100,
   latestEnd: '',
   multipleSubmission: 0,
   publishGrade: 0,
   requirePeerReview: 0,
   peerReviewStart: '',
   peerReviewEnd: '',
   minPeerReview: 1,
   attachments: []
 };
 fileList.value = [];
};

onMounted(() => {
 // 从 Pinia store 获取课程 ID，并设置到 homeworkForm 中
 if (courseStore.courseId) {
   homeworkForm.value.courseId = courseStore.courseId;
 } else {
   console.warn("课程ID未设置！");
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
 margin-left: 820px;
}

.upload-container .el-upload__tip {
 margin-left: 5px;
}

.form-tip {
 margin-left: 10px;
 font-size: 12px;
 color: #666;
}
</style> -->


<!--
<template>
  <el-form :model="homeworkForm" label-width="120px">
    <el-form-item label="作业标题">
      <el-input style="margin-right: 100px; width: 400px;" v-model="homeworkForm.title" placeholder="请输入作业标题"></el-input>
    </el-form-item>
    <el-form-item label="作业描述">
      <div class="upload-container">
        <el-upload
          class="upload-demo"
          :limit="1"
          action="http://localhost:8080/attachment/upload"
          :headers="headers"
          :before-upload="beforeUpload"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :accept="acceptedFileTypes"
          ref="uploadRef"
          multiple
        >
          <template #trigger>
            <el-button round type="primary">添加附件</el-button>
          </template>
        </el-upload>
        <div class="el-upload__tip">支持格式：docx、doc、pdf、jpg、png</div>
      </div>
      <v-md-editor 
        v-model="homeworkForm.description" 
        class="markdown-editor" style="margin-right: 500px"
        placeholder="请输入作业描述" 
      />
      <div style="width: 500px;"></div>
    </el-form-item>

    <br>
    <el-form-item>
      <span style="margin-left: -68px; margin-right: 13px;">作业满分</span>
      <el-input-number v-model="homeworkForm.fullGrade" :min="0" label="作业满分" placeholder="作业满分" style="width: 150px; margin-right: 60px;"></el-input-number>
      <span style="margin-right: 13px;">补交满分</span>
      <el-input-number v-model="homeworkForm.delayedGrade" :min="0" label="补交满分" placeholder="补交满分" style="width: 150px; margin-right: 60px;"></el-input-number>    
      <el-checkbox v-model="homeworkForm.multipleSubmission" style="margin-right: 60px;">允许重复提交</el-checkbox>
      <el-checkbox v-model="homeworkForm.publishGrade">公布作业成绩</el-checkbox>
    </el-form-item>

    <el-form-item label="开始时间">
      <el-date-picker v-model="homeworkForm.start" type="datetime" placeholder="选择开始时间"></el-date-picker>
      <span class="form-tip">（开始时间之前的作业不会显示在学生的列表中）</span>
    </el-form-item>

    <el-form-item label="正常截止时间" style="margin-left: 28px;">
      <el-date-picker v-model="homeworkForm.end" type="datetime" placeholder="选择正常截止时间"></el-date-picker>
      <span class="form-tip">（晚于正常截止时间提交的作业将被标记为“补交”，但学生依然可以提交作业）</span>
    </el-form-item>

    <el-form-item label="补交截止时间" style="margin-left: 28px;">
      <el-date-picker v-model="homeworkForm.latestEnd" type="datetime" placeholder="选择补交截止时间"></el-date-picker>
      <span class="form-tip">（在补交截止时间之后的作业，不可提交）</span>
    </el-form-item>

    <el-form-item>
      <el-checkbox v-model="homeworkForm.requirePeerReview" style="margin-left: -67px;">互评任务</el-checkbox>
      <el-date-picker v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.peerReviewStart" type="datetime" placeholder="开始时间" style="margin-left: 20px;"></el-date-picker>
      <el-date-picker v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.peerReviewEnd" type="datetime" placeholder="截止时间" style="margin-left: 20px; margin-right: 30px;"></el-date-picker>
      <span v-if="homeworkForm.requirePeerReview" style="margin-right: 3px;">学生最低互评数量</span>
      <el-input-number v-if="homeworkForm.requirePeerReview" v-model="homeworkForm.minPeerReview" :min="1" label="最少互评数量" style="width: 150px; margin-left: 20px;"></el-input-number>
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
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import { useCourseStore } from '@/stores/course';
import { getIssue } from '@/api/assignments.js';
import moment from 'moment';

// 使用 github 主题
VMdEditor.use(githubTheme);

const courseStore = useCourseStore();
const homeworkForm = ref({
  courseId: '',
  title: '',
  description: '',
  start: '',
  end: '',
  isPrivate: 0,
  fullGrade: 100,
  delayedGrade: 100,
  latestEnd: '',
  multipleSubmission: 0,
  publishGrade: 0,
  requirePeerReview: 0,
  peerReviewStart: '',
  peerReviewEnd: '',
  minPeerReview: 1,
  attachments: []
});

const fileList = ref([]);
const headers = {
  Authorization: localStorage.getItem('token')
};
const acceptedFileTypes = ".doc,.docx,.pdf,.jpg,.png"; // 支持的文件类型

// 上传文件前的检查
const beforeUpload = (file) => {
  const isAllowedSize = file.size / 1024 / 1024 < 2048;  // 限制文件大小为 2GB
  if (!isAllowedSize) {
    alert('文件大小不能超过2G！');
    return false;
  }
  return true;
};

// 上传成功的回调，将文件添加到 attachments 中
const handleUploadSuccess = (response, file) => {
  console.log('文件上传成功:', file);
  homeworkForm.value.attachments.push({
    name: file.name,
    url: response.url,  // 假设 response 包含文件的 url
    id: response.id     // 假设 response 返回文件的唯一 id
  });
};

// 上传失败的回调
const handleUploadError = () => {
  alert('上传失败');
};

// 文件预览
const handlePreview = (file) => {
  console.log('预览文件:', file);
};

// 文件移除
const handleRemove = (file, fileList) => {
  console.log('移除文件:', file, fileList);
  homeworkForm.value.attachments = homeworkForm.value.attachments.filter(
    (attachment) => attachment.id !== file.id
  );
};

const formatDateForSQL = (date) => {
  if (!date) return null;
  return moment(date).isValid() ? moment(date).format('YYYY-MM-DD HH:mm:ss') : null;
};

const submitHomework = async () => {
  const data = {
    courseId: homeworkForm.value.courseId,
    title: homeworkForm.value.title,
    description: homeworkForm.value.description,
    start: formatDateForSQL(homeworkForm.value.start),
    end: formatDateForSQL(homeworkForm.value.end),
    isPrivate: homeworkForm.value.isPrivate ? 1 : 0,
    fullGrade: homeworkForm.value.fullGrade,
    delayedGrade: homeworkForm.value.delayedGrade,
    latestEnd: formatDateForSQL(homeworkForm.value.latestEnd),
    multipleSubmission: homeworkForm.value.multipleSubmission ? 1 : 0,
    publishGrade: homeworkForm.value.publishGrade ? 1 : 0,
    requirePeerReview: homeworkForm.value.requirePeerReview ? 1 : 0,
    peerReviewStart: formatDateForSQL(homeworkForm.value.peerReviewStart),
    peerReviewEnd: formatDateForSQL(homeworkForm.value.peerReviewEnd),
    minPeerReview: homeworkForm.value.minPeerReview,
    attachments: homeworkForm.value.attachments.map(file => file.id)
  };

  try {
    const response = await getIssue(data);
    console.log('公开作业:', response.data);
  } catch (error) {
    console.error('公开作业时出错:', error);
  }
};

const resetForm = () => {
  homeworkForm.value = {
    courseId: courseStore.courseId || '',
    title: '',
    description: '',
    start: '',
    end: '',
    isPrivate: 0,
    fullGrade: 100,
    delayedGrade: 100,
    latestEnd: '',
    multipleSubmission: 0,
    publishGrade: 0,
    requirePeerReview: 0,
    peerReviewStart: '',
    peerReviewEnd: '',
    minPeerReview: 1,
    attachments: []
  };
  fileList.value = [];
};

onMounted(() => {
  if (courseStore.courseId) {
    homeworkForm.value.courseId = courseStore.courseId;
  } else {
    console.warn("课程ID未设置！");
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
  margin-left: 820px;
}

.upload-container .el-upload__tip {
  margin-left: 5px;
}

.form-tip {
  margin-left: 10px;
  font-size: 12px;
  color: #666;
}
</style> -->
