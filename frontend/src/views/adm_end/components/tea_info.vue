<template>
  <div class="teacher-info">
    <!-- 上传按钮和操作按钮的容器 -->
    <UploadFile :uploadPost="uploadteacher"></UploadFile>

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
          :disabled="!teachers.length"
          @click="generateAccounts"
        >
          账号生成
        </el-button>
        <el-button
          class="action-button"
          type="primary"
          :disabled="!accountsGenerated"
          @click="confirmUpload"
        >
          确认上传
        </el-button>
        <el-button
          class="action-button"
          type="warning"
          :disabled="!accountsGenerated"
          @click="exportExcel"
        >
          导出
        </el-button>
      </div> -->
    </div>

    <!-- 预览表格 -->
    <div class="table-container">
      <el-table
        :data="teachers"
        v-if="teachers.length"
        border
        stripe
        size="medium"
        style="width: 100%;"
        class="custom-table"
      >
        <el-table-column
          prop="college"
          label="学院"
          width="150"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="name"
          label="姓名"
          width="150"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="idNumber"
          label="身份证号"
          width="200"
          align="center"
          header-align="center"
        >
          <template #default="scope">
            {{ maskIdNumber(scope.row.idNumber) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="teacherNumber"
          label="工号"
          width="150"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="account"
          label="账号"
          width="150"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="password"
          label="密码"
          width="150"
          align="center"
          header-align="center"
        />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import UploadFile from './UploadFile.vue';
import { ref } from 'vue';
import { ElUpload, ElButton, ElTable, ElTableColumn, ElMessage } from 'element-plus';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';
import {uploadteacher} from '@/api/course';

const fileList = ref([]);
const teachers = ref([]);
const accountsGenerated = ref(false);

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
      const workbook = XLSX.read(data, { type: 'array' });
      const firstSheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[firstSheetName];
      const jsonData = XLSX.utils.sheet_to_json(worksheet);

      teachers.value = jsonData
        .map((item) => {
          if (!item['学院'] || !item['姓名'] || !item['身份证号'] || !item['工号']) {
            console.warn('跳过不完整的数据:', item);
            return null;
          }
          return {
            college: item['学院'],
            name: item['姓名'],
            idNumber: String(item['身份证号']),
            teacherNumber: String(item['工号']),
            account: '',
            password: '',
          };
        })
        .filter((item) => item !== null);

      accountsGenerated.value = false;
      ElMessage.success('文件读取成功');
    } catch (error) {
      console.error('文件解析错误', error);
      ElMessage.error('文件解析失败，请检查文件格式是否正确。');
    }
  };
  reader.onerror = (e) => {
    ElMessage.error('文件读取失败，请重试。');
  };
  reader.readAsArrayBuffer(file.raw);
};

// 生成账号和密码
const generateAccounts = () => {
  teachers.value.forEach((teacher) => {
    teacher.account = teacher.teacherNumber;
    teacher.password = generatePassword();
  });
  accountsGenerated.value = true;
  ElMessage.success('账号生成成功');
};

// 生成随机密码函数
const generatePassword = () => {
  const length = 8;
  const chars = [
    'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
    'abcdefghijklmnopqrstuvwxyz',
    '0123456789',
    '!@#$%^&*',
  ];
  let passwordChars = [];

  // 保证每种字符类型至少出现一次
  chars.forEach((charSet) => {
    passwordChars.push(charSet[Math.floor(Math.random() * charSet.length)]);
  });

  // 剩余字符随机填充
  while (passwordChars.length < length) {
    const charSet = chars[Math.floor(Math.random() * chars.length)];
    passwordChars.push(charSet[Math.floor(Math.random() * charSet.length)]);
  }

  // 打乱顺序
  passwordChars = passwordChars.sort(() => Math.random() - 0.5);
  return passwordChars.join('');
};

// 隐藏身份证号中间部分
const maskIdNumber = (idNumber) => {
  if (typeof idNumber !== 'string') {
    console.warn('idNumber 不是字符串:', idNumber);
    return idNumber;
  }
  return idNumber.replace(/^(.{6})(?:\d+)(.{4})$/, '$1****$2');
};

// 确认上传
const confirmUpload = () => {
  ElMessage.success('教师信息已上传成功');
};

// 导出 Excel 文件
const exportExcel = () => {
  const worksheet = XLSX.utils.json_to_sheet(teachers.value);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, '教师信息');

  const excelBuffer = XLSX.write(workbook, {
    bookType: 'xlsx',
    type: 'array',
  });
  const blob = new Blob([excelBuffer], { type: 'application/octet-stream' });
  const fileName = `教师信息_${new Date().toISOString().slice(0, 10)}.xlsx`;
  saveAs(blob, fileName);
};
</script>

<style scoped>
.teacher-info {
  padding: 30px;
  display: flex;
  flex-direction: column;
  width: 100%;
}

/* 上传按钮和操作按钮的容器 */
.action-container {
  display: flex;
  justify-content: flex-start; /* 按钮左对齐 */
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
  width: 100%;
}

/* 表格容器样式，增加宽度设置使表格水平居中 */
.table-container {
  display: flex;
  justify-content: center; /* 表格居中 */
  width: 100%;
}


.el-table {
  max-width: 1000px;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.el-table th, .el-table td {
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

/* 自定义按钮样式 */
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
