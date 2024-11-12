<template>
  <div class="container">
    <el-card class="teacher-info-card">
      <div class="teacher-info">
        <img class="avatar" src="@/assets/img/bjtu.jpg" alt="教师头像" />
        <div class="info">
          <h3>{{ teacherInfo.name }}</h3>
          <br>
          <p>职称：{{ teacherInfo.title }}</p>
          <el-button type="text" style="margin-right: 20px;">
            <span class="svg-container">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 128 96" class="mail-icon">
                <path d="M64.125 56.975L120.188.912A12.476 12.476 0 0 0 115.5 0h-103c-1.588 0-3.113.3-4.513.838l56.138 56.137z"/>
                <path d="M64.125 68.287l-62.3-62.3A12.42 12.42 0 0 0 0 12.5v71C0 90.4 5.6 96 12.5 96h103c6.9 0 12.5-5.6 12.5-12.5v-71a12.47 12.47 0 0 0-1.737-6.35L64.125 68.287z"/>
              </svg>
            </span>
            邮箱：{{ teacherInfo.email }}
          </el-button>

          <el-button type="text" style="margin-right: 20px;">
            <el-icon class="custom-icon">
              <Phone />
            </el-icon>
            办公电话：{{ teacherInfo.phoneNumber }}
          </el-button>
          <el-button type="text" style="margin-right: 20px;">
            <el-icon class="custom-icon">
              <LocationFilled />
            </el-icon>
            办公地址：{{ teacherInfo.address }}
          </el-button>
          <br>
          <el-button type="text" style="margin-right: 20px;">
            职称：{{ teacherInfo.title }}
          </el-button>
          <!-- <el-button type="text" style="margin-right: 20px;">
            工号：{{ teacherInfo.academy }}
          </el-button> -->
          <el-button type="text" style="margin-right: 20px;">
            教学单位：{{ teacherInfo.address }}
          </el-button>
          <br>
          <br>
        </div>
      </div>
    </el-card>

    <br>
    <span>教师简介</span>
    <el-card class="teacher-intro-card">
      <p>{{ teacherInfo.brief }}</p>
    </el-card>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { getTeacherInfo } from '@/api/course.js'; 
import { ElMessage } from 'element-plus';

let courseId = localStorage.getItem('courseId') // 获取课程ID
const token = ref('您的授权Token'); // 真实环境中应该动态获取
const teacherInfo = ref({
  name: '',
  title: '',
  email: '',
  phone: '',
  officeAddress: '',
  position: '',
  employeeNumber: '',
  teachingUnit: '',
  introduction: '',
});

const fetchTeacherInfo = async () => {
  try {
    const response = await getTeacherInfo(courseId, token.value);
    console.log(response);
    
    if (response || response.data) {
      teacherInfo.value=response
      // teacherInfo.value = {
      //   name: response.name,
      //   title: response.title,
      //   email: response.email,
      //   phone: response.phone,
      //   officeAddress: response.officeAddress,
      //   position: response.position,
      //   employeeNumber: response.employeeNumber,
      //   teachingUnit: response.teachingUnit,
      //   introduction: response.introduction,
      // };
    }
  } catch (error) {
    ElMessage.error('获取教师信息失败，请检查网络或联系管理员');
    console.error(error);
  }
};

// 页面加载时获取教师信息
onMounted(() => {
  fetchTeacherInfo();
});

  </script>
  
  <style scoped>
  .container {
    display: flex;
    flex-direction: column;
    gap: 20px;
    width: 140%;
    max-width: 600px;
  }
    .avatar {
    width: 100px; /* 调整头像大小 */
    height: 100px;
    border-radius: 50%;
    margin-right: 30px;
    margin-left: -5px;
    }
  
  .teacher-info-card {
    width: 200%;
    margin-right: 30px;
    padding: 20px;
    box-sizing: border-box;
  }
  
  .teacher-intro-card {
    width: 200%;
    margin-right: 30px;
    padding: 20px;
    box-sizing: border-box;
    /* box-shadow: none;  */
  }
  
  .teacher-info {
    display: flex;
    align-items: center;
  }
  
  .svg-container {
    width: 16px;  /* 设定合适的宽度 */
    height: auto;  /* 自动调整高度，保持宽高比 */
    margin-right: 8px;
  }
  
  
  .mail-icon {
    fill: #07395f; /* 墨绿色 */
  }
  
  .info h2 {
    margin: 0;
    font-size: 20px;
  }
  
  .info p {
    margin: 5px 0;
    color: #666;
  }
  

  .custom-icon {
    width: 18px;  
    height: 18px; 
    margin-right: 3px; 
  }
  </style>
  