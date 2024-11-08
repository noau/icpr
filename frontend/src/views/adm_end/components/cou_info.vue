<template>
  <div class="course-info">
    <!-- 上传按钮和操作按钮的容器 -->
    <UploadFile :uploadPost="uploadCourseInfo"></UploadFile>
    <div class="action-container">
      <!-- 文件上传 -->
      <!-- <el-upload
        class="upload"
        action=""
        :before-upload="beforeUpload"
        :on-change="handleFileChange"
        :file-list="fileList"
        :auto-upload="false"
        :show-file-list="false"
      >
        <el-button type="primary">打开文件</el-button>
      </el-upload> -->

      <!-- 操作按钮组 -->
      <!-- <div class="button-group">
        <el-button
          class="action-button"
          type="primary"
          :disabled="!courses.length"
          @click="confirmUpload"
        >
          确认上传
        </el-button>
      </div> -->
    </div>

    <!-- 预览表格 -->
    <div class="table-container">
      <el-table
        :data="courses"
        v-if="courses.length"
        border
        stripe
        size="medium"
        style="width: 100%"
        class="custom-table"
      >
        <el-table-column
          prop="courseName"
          label="课程名称"
          width="180"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="courseNumber"
          label="课程号"
          width="180"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="semester"
          label="所属学期"
          width="150"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="classWeek"
          label="上课周次"
          width="150"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="classTime"
          label="上课时间"
          width="180"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="location"
          label="上课地点"
          width="180"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="teacher"
          label="任课教师"
          width="150"
          align="center"
          header-align="center"
        />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import {
  ElUpload,
  ElButton,
  ElTable,
  ElTableColumn,
  ElMessage,
} from "element-plus";
import * as XLSX from "xlsx";
import { Up } from "@icon-park/vue-next";
import { uploadCourseInfo } from "@/api/course";
import UploadFile from "./UploadFile.vue";

const fileList = ref([]);
const courses = ref([]);

// 拦截上传，防止自动上传到服务器
const beforeUpload = (file) => {
  return false;
};

// 处理文件变化
const handleFileChange = (file, fileList) => {
  const reader = new FileReader();
  reader.onload = (e) => {
    try {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: "array" });
      const firstSheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[firstSheetName];
      const jsonData = XLSX.utils.sheet_to_json(worksheet);

      courses.value = jsonData
        .map((item) => {
          if (
            !item["课程名称"] ||
            !item["课程号"] ||
            !item["所属学期"] ||
            !item["上课周次"] ||
            !item["上课时间"] ||
            !item["上课地点"] ||
            !item["任课教师"]
          ) {
            console.warn("跳过不完整的数据:", item);
            return null;
          }
          return {
            courseName: item["课程名称"],
            courseNumber: item["课程号"],
            semester: item["所属学期"],
            classWeek: item["上课周次"],
            classTime: item["上课时间"],
            location: item["上课地点"],
            teacher: item["任课教师"],
          };
        })
        .filter((item) => item !== null);

      ElMessage.success("文件读取成功");
    } catch (error) {
      console.error("文件解析错误", error);
      ElMessage.error("文件解析失败，请检查文件格式是否正确。");
    }
  };
  reader.onerror = (e) => {
    ElMessage.error("文件读取失败，请重试。");
  };
  reader.readAsArrayBuffer(file.raw);
};

// 确认上传
const confirmUpload = () => {
  ElMessage.success("课程信息已确认上传");
};
</script>

<style scoped>
.course-info {
  padding: 30px;
  display: flex;
  flex-direction: column;
  width: 100%;
}

/* 上传按钮和操作按钮的容器 */
.action-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
  width: 100%;
}

/* 表格容器样式，增加宽度设置使表格水平居中 */
.table-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

.el-table {
  max-width: 1000px;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.el-table th,
.el-table td {
  text-align: left;
  vertical-align: left;
  padding: 12px;
  font-size: 14px;
  word-wrap: break-word;
  word-break: break-all;
}

/* 上传按钮样式 */
.upload .el-button {
  padding: 5px 10px;
  font-size: 15px;
  height: 40px;
  line-height: 1;
}

/* 操作按钮组样式 */
.button-group {
  display: flex;
  gap: 10px;
  flex-wrap: nowrap;
}

/* 操作按钮样式 */
.action-button {
  min-width: 100px;
  height: 30px;
}

/* 响应式布局：在小屏幕下，按钮组和上传按钮垂直排列 */
@media (max-width: 600px) {
  .action-container {
    flex-direction: column;
    align-items: flex-start;
  }

  .button-group {
    flex-direction: column;
    align-items: flex-start;
  }

  .action-button {
    width: 100%;
    min-width: unset;
  }
}

/* 自定义蓝色按钮样式 */
::v-deep .el-button--primary {
  background-color: #07395f;
  border-color: #07395f;
  color: #fff;
}

::v-deep .el-button--primary:hover,
::v-deep .el-button--primary:focus,
::v-deep .el-button--primary:active {
  background-color: #07395f;
  border-color: #07395f;
}
</style>
