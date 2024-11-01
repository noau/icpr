<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { userLoginService } from '@/api/user.js'
import { useUserStore } from '@/stores/user.js'
import { useRouter } from 'vue-router'
import SIdentify from '@/components/SIdentify.vue'
import { ElMessage } from 'element-plus' 

const form = ref()
const selectedRole = ref("student"); // Default role set to "student"

// Form data for submission
const formModel = ref({
  username: '',
  password: ''
})
const sidentifyMode = ref('')
const identifyCode = ref('')
const identifyCodes = ref('1234567890abcdefjhijklinopqrsduvwxyz')

// Component mounted function
onMounted(() => {
  identifyCode.value = ''
  makeCode(identifyCodes.value, 4)
})

// Generate random numbers
const randomNum = (min, max) => {
  max = max + 1
  return Math.floor(Math.random() * (max - min) + min)
}

// Generate verification code string
const makeCode = (o, l) => {
  for (let i = 0; i < l; i++) {
    identifyCode.value += o[randomNum(0, o.length)]
  }
}

// Refresh verification code
const refreshCode = () => {
  identifyCode.value = ''
  makeCode(identifyCodes.value, 4)
}

const userStore = useUserStore()
const router = useRouter()

// Login function
const login = async () => {
  if (!sidentifyMode.value) {
    ElMessage({ type: 'error', message: '验证码不能为空！' })
    return
  }
  if (sidentifyMode.value !== identifyCode.value) {
    ElMessage({ type: 'error', message: '验证码错误' })
    refreshCode()
    return
  }
  
  // Set API endpoint based on selected role
  let loginEndpoint;
  if (selectedRole.value === 'student') {
    loginEndpoint = 'student';
  } else if (selectedRole.value === 'teacher') {
    loginEndpoint = 'teacher';
  } else if (selectedRole.value === 'administrator') {
    loginEndpoint = 'administrator';
  }

  await form.value.validate()
  console.log(loginEndpoint)

  let loginFM = formModel.value
  loginFM.role = loginEndpoint
  const res = await userLoginService(formModel.value) // Pass the endpoint as a parameter
  userStore.setToken(res);
  localStorage.setItem('token', res);  
  userStore.setId(formModel.value.username);
  localStorage.setItem('userId', formModel.value.username);  

  if (selectedRole.value === 'student') {
    await router.push('/stu-end')
  } else if (selectedRole.value === 'teacher') {
    await router.push('/tea-end')
  } else if (selectedRole.value === 'administrator') {
    await router.push('/adm-end')
  }
  
}

// Forgot password functions
const forgotPassword = (method) => {
  if (method === 'email') {
    router.push('/forgot-password/email')
  } else if (method === 'phone') {
    router.push('/forgot-password/phone')
  }
}

const forgotPasswordModal = ref(false)
</script>

<template>
  <el-row class="login-page" justify="center" align="middle">
    <el-col :span="6" class="form">
      <el-form
        :model="formModel"
        :rules="rules"
        ref="form"
        size="large"
        autocomplete="off"
      >
        <!-- Role Selection Dropdown -->
        <el-form-item>
          <el-select v-model="selectedRole" placeholder="选择身份">
            <el-option label="学生" value="student"></el-option>
            <el-option label="老师" value="teacher"></el-option>
            <el-option label="管理员" value="administrator"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        
        <el-form-item prop="username">
          <el-input
            v-model="formModel.username"
            :prefix-icon="User"
            placeholder="请输入学/工号"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="formModel.password"
            name="password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        
        <el-form-item label-width="65px" style="margin-bottom: 5px;">
          <div class="code-row">
            <el-input placeholder="请输入验证码" v-model="sidentifyMode" clearable style="margin-top: -15px; margin-left: -64px;"></el-input>
            <div class="code" @click="refreshCode">
              <SIdentify :identifyCode="identifyCode"></SIdentify>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item class="flex">
          <el-link type="primary" :underline="false" class="forgot-password-link" @click="forgotPasswordModal = true">
            找回密码
          </el-link>
        </el-form-item>
        
        <el-form-item>
          <el-button round
            @click="login"
            class="button"
            type="primary"
            auto-insert-space
          >登录</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>

  <!-- Forgot Password Dialog -->
  <el-dialog title="找回密码" align-center v-model="forgotPasswordModal" append-to-body width="350px" :modal-append-to-body="false" center custom-class="forgot-password-dialog" style="height: 200px;">
    <div class="forgot-password-options">
      <el-button round @click="forgotPassword('email')">通过邮箱找回</el-button>
      <el-button round style="margin-left: 0px;" @click="forgotPassword('phone')">通过手机找回</el-button>
    </div>
  </el-dialog>
</template>


<style lang="scss" scoped>
.login-page {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100vh;
  background: url('@/assets/img/bjtu.jpg') no-repeat center / cover;
  display: flex;
  align-items: center;
  justify-content: center;
}
.form {
  height: 450px;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
  .title {
    margin: 0 auto;
  }
  .button {
    width: 100%;
  }
  .flex {
    width: 100%;
    display: flex;
    justify-content: space-between;
  }
  .forgot-password-link {
    &:hover {
      background-color: transparent;
      text-decoration: underline;
    }
  }
}
.forgot-password-options {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 1rem;
  .el-button {
    width: 100%
  }
}
.forgot-password-dialog {
  display: flex;
  align-items: center;
  justify-content: center;
}
.code-row {
  // 水平对齐
  display: flex;
  align-items: center;
  gap: 10px;
}
.code {
  cursor: pointer;
}
</style> 

