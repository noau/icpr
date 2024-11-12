<template>
  <div>
    <el-card>
      <h2>作业标题：{{ assignmentDetail.title }}</h2>
      <!-- <br /> -->
      <p>作业描述：{{ assignmentDetail.description }}</p>
      <p>互评数量：{{ assignmentDetail.minPeerReview }}</p>

      <br />
      
      <div class="attachment-header">
        <h3>附件</h3>
        <el-button round type="text" icon="el-icon-download" @click="handleDownload()">下载附件</el-button>
      </div>

      <!-- 使用 VueOfficePdf 组件来显示 PDF 文件 -->
      <vue-office-pdf
        v-if="urlType === 1"
        :src="pdfUrl"
        style="height: 100vh"
        @rendered="renderedHandler"
        @error="errorHandler"
      />

      <!-- 展示图片 -->
      <div v-else-if="urlType === 2" style="text-align:center; padding-top:20px;">
        <img :src="attachmentUrl" alt="Attachment Image" style="max-width: 100%; height: auto;" />
      </div>

      <!-- 展示 .doc 或 .docx 文件 -->
      <vue-office-docx
        v-else-if="urlType === 3"
        :src="pdfUrl"
        style="height: 100vh"
        @rendered="renderedHandler"
        @error="errorHandler"
      />
      <div v-else style="text-align:center; padding-top:20px;">
        <p>暂无</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import VueOfficePdf from '@vue-office/pdf';
import VueOfficeDocx from '@vue-office/docx';
import '@vue-office/docx/lib/index.css';
import { useRoute } from 'vue-router';
import { getAssignmentsInfo } from '@/api/assignments.js';
import { getAttachmentUrl } from '@/api/common.js';
import { getFileTypeFromUrl } from '@/utils/base.js';

const route = useRoute();

const assignmentDetail = ref({});
const pdfUrl = ref('');
const urlType = ref(0);
const fileName = ref('');

const loadAssignmentDetail = async () => {
  const assignmentId = route.query.assignmentId;
  try {
    const response = await getAssignmentsInfo({ id: assignmentId });
    if (response) {
      assignmentDetail.value = response;
      if (response.attachments && response.attachments.length > 0) {
        const firstAttachmentId = response.attachments[0];
        await loadFileUrl(firstAttachmentId);
      }
    }
  } catch (error) {
    console.error('获取作业详情失败:', error);
  }
};

const loadFileUrl = async (attachmentId) => {
  try {
    const response = await getAttachmentUrl(attachmentId);
    if (response && response.url) {
      pdfUrl.value = response.url;
      fileName.value = response.name;
      urlType.value = getFileTypeFromUrl(response.url);
      console.log(pdfUrl.value + ':  ' + urlType.value);
    } else {
      console.log('未获取到文件 URL');
    }
  } catch (error) {
    console.error('获取文件 URL 失败:', error);
  }
};

/**
 * 通过链接下载文件
 */
 function handleDownload() {
  // 获取文件 URL 和文件名
  const url = pdfUrl.value; // 假设 row 中包含一个文件的 URL
  const name = fileName.value; // 默认文件名，如果没有提供 name 属性，使用默认值

  // 检查 URL 是否有效
  if (url && name) {
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
  console.log(url);
  
  const response = await axios({
    url,
    method: 'GET',
    responseType: 'blob', // 必须指定为blob类型才能下载
  });
  url = window.URL.createObjectURL(new Blob([response.data]));
  console.log(url);
  
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download',  name);
  document.body.appendChild(link);
  link.click();
}

const renderedHandler = () => {
  console.log('PDF 渲染完成');
};

const errorHandler = () => {
  console.log('PDF 渲染失败');
};

onMounted(loadAssignmentDetail);
</script>

<style scoped>
.attachment-header {
  display: flex;
  align-items: center;
  gap: 10px; /* 控制标题与按钮之间的间距 */
}
</style>