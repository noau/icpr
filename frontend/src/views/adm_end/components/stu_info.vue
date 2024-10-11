<template>
  <div class="student-info">
    <!-- 上传按钮和操作按钮的容器 -->
    <div class="action-container">
      <!-- 文件上传 -->
      <el-upload
        class="upload"
        action=""
        :before-upload="beforeUpload"
        :on-change="handleFileChange"
        :file-list="fileList"
        :auto-upload="false"
        :show-file-list="false"
      >
        <el-button type="primary" >打开文件</el-button>
      </el-upload>

      <!-- 操作按钮组 -->
      <div class="button-group">
        <el-button
          class="action-button"
          type="primary"
          :disabled="!students.length"
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
      </div>
    </div>

    <!-- 预览表格 -->
    <div class="table-container">
      <el-table
        :data="students"
        v-if="students.length"
        border
        stripe
        size="medium"
        style="width: 100%;" 
        class="custom-table"
      >
        <el-table-column
          prop="college"
          label="学院"
          width="200"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="name"
          label="姓名"
          width="200"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="idNumber"
          label="身份证号"
          width="250"
          align="center"
          header-align="center"
        >
          <template #default="scope">
            {{ maskIdNumber(scope.row.idNumber) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="studentNumber"
          label="学号"
          width="200"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="account"
          label="账号"
          width="200"
          align="center"
          header-align="center"
        />
        <el-table-column
          prop="password"
          label="密码"
          width="200"
          align="center"
          header-align="center"
        />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElUpload, ElButton, ElTable, ElTableColumn, ElMessage } from 'element-plus';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';

const fileList = ref([]);
const students = ref([]);
const accountsGenerated = ref(false);

// 拦截上传，防止自动上传到服务器
const beforeUpload = (file) => {
  return false;
};

// 处理文件变化
const handleFileChange = (file) => {
  const reader = new FileReader();
  reader.onload = (e) => {
    try {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: 'array' });
      const firstSheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[firstSheetName];
      const jsonData = XLSX.utils.sheet_to_json(worksheet);

      // 输出读取到的数据，方便检查
      console.log("读取到的数据：", jsonData);

      // 假设列名为 学院、姓名、身份证号、学号，转换到 students 数组
      students.value = jsonData
        .map((item) => {
          if (!item['学院'] || !item['姓名'] || !item['身份证号'] || !item['学号']) {
            console.warn('跳过不完整的数据:', item);
            return null;
          }
          return {
            college: item['学院'],
            name: item['姓名'],
            idNumber: String(item['身份证号']), // 转换为字符串
            studentNumber: String(item['学号']), // 转换为字符串
            account: '',
            password: '',
          };
        })
        .filter((item) => item !== null);

      // 检查读取到的数据是否正确
      console.log("转换后的学生数据：", students.value);

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
  students.value.forEach((student) => {
    student.account = student.studentNumber;
    student.password = generatePassword();
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
  ElMessage.success('学生信息已上传成功');
};

// 导出 Excel 文件
const exportExcel = () => {
  const worksheet = XLSX.utils.json_to_sheet(students.value);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, '学生信息');

  const excelBuffer = XLSX.write(workbook, {
    bookType: 'xlsx',
    type: 'array',
  });
  const blob = new Blob([excelBuffer], { type: 'application/octet-stream' });
  const fileName = `学生信息_${new Date().toISOString().slice(0, 10)}.xlsx`;
  saveAs(blob, fileName);
};
</script>

<style scoped>
.student-info {
  padding: 50px;
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
  width: auto; /* 表格宽度根据内容自适应，避免出现水平滚动条 */
  border: 2px solid #ccc;
  border-radius: 10px;
}

.el-table th, .el-table td {
  text-align: left;
  vertical-align: middle;
  padding: 16px;
  font-size: 16px;
  word-wrap: break-word;
  word-break: break-all;
}

/* 上传按钮样式 */
.upload .el-button {
  padding: 10px 15px;
  font-size: 16px;
  height: 45px;
  line-height: 1;
}

/* 操作按钮组样式 */
.button-group {
  display: flex;
  gap: 15px;
  flex-wrap: nowrap;
}

/* 操作按钮样式 */
.action-button {
  min-width: 150px;
  height: 40px;
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

::v-deep .el-button.is-round {
  border-radius: 20px;
}

/* 隐藏 el-upload 默认生成的 input[type="file"] */
::v-deep .el-upload__input {
  display: none !important;
}
</style>
