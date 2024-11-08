<template>
    <div class="student-info">
      <UploadFile :uploadPost="uploadselection"></UploadFile>
      <!-- 文件上传 -->
      <!-- <el-upload
        class="upload"
        action=""
        :before-upload="beforeUpload"
        :on-change="handleFileChange"
        :file-list="fileList"
        :auto-upload="false"
      >
        <el-button type="primary">上传文件</el-button>
      </el-upload> -->
  
      <!-- 按钮组 -->
      <!-- <div class="button-group">
        <el-button type="success" :disabled="!students.length" @click="generateAccounts">
          账号生成
        </el-button>
        <el-button type="primary" :disabled="!accountsGenerated" @click="confirmUpload">
          确认上传
        </el-button>
        <el-button type="warning" :disabled="!accountsGenerated" @click="exportExcel">
          导出
        </el-button> -->
      <!-- </div> -->
  
      <!-- 预览表格 -->
      <el-table :data="students" v-if="students.length" style="margin-top: 20px">
        <el-table-column prop="college" label="学院" width="120" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="idNumber" label="身份证号" width="180">
          <template #default="scope">
            {{ maskIdNumber(scope.row.idNumber) }}
          </template>
        </el-table-column>
        <el-table-column prop="studentNumber" label="学号" width="120" />
        <el-table-column prop="account" label="账号" width="120" />
        <el-table-column prop="password" label="密码" width="120" />
      </el-table>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import * as XLSX from 'xlsx';
  import { saveAs } from 'file-saver';
  import { ElMessage } from 'element-plus';
  import {uploadselection} from '@/api/course';
  import UploadFile from './UploadFile.vue';
  
  const fileList = ref([]);
  const students = ref([]);
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
  
        // 假设 Excel 中的列名为 "学院"、"姓名"、"身份证号"、"学号"
        students.value = jsonData.map((item) => {
          if (!item['学院'] || !item['姓名'] || !item['身份证号'] || !item['学号']) {
            // 数据不完整，跳过此条记录
            return null;
          }
          return {
            college: item['学院'],
            name: item['姓名'],
            idNumber: item['身份证号'],
            studentNumber: item['学号'],
            account: '',
            password: '',
          };
        }).filter(item => item !== null);
  
        accountsGenerated.value = false;
        ElMessage.success('文件读取成功！');
      } catch (error) {
        console.error('文件解析错误', error);
        ElMessage.error('文件解析失败，请检查文件格式是否正确。');
      }
    };
    reader.onerror = (e) => {
      console.error('文件读取错误', e);
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
    ElMessage.success('账号生成成功！');
  };
  
  // 生成随机密码函数
  const generatePassword = () => {
    const length = 8;
    const chars = [
      'ABCDEFGHIJKLMNOPQRSTUVWXYZ', // 大写字母
      'abcdefghijklmnopqrstuvwxyz', // 小写字母
      '0123456789', // 数字
      '!@#$%^&*', // 特殊符号
    ];
    let passwordChars = [];
  
    // 保证每种字符类型至少出现一次
    chars.forEach(charSet => {
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
    return idNumber.replace(/^(.{6})(?:\d+)(.{4})$/, '$1****$2');
  };
  
  // 确认上传
  const confirmUpload = () => {
    // 这里可以调用 API，将数据上传到服务器
    // 示例：
    // axios.post('/api/uploadStudents', students.value).then(response => {
    //   // 处理响应
    // });
  
    // 模拟上传成功
    ElMessage.success('学生信息已上传成功！');
  };
  
  // 导出 Excel 文件
  const exportExcel = () => {
    // 将数据转换为工作表
    const worksheet = XLSX.utils.json_to_sheet(students.value);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, '学生信息');
  
    // 导出 Excel 文件
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
    padding: 20px;
  }
  
  .upload {
    margin-bottom: 20px;
  }
  
  .button-group {
    margin-top: 20px;
    display: flex;
    gap: 10px;
  }
  </style>
  