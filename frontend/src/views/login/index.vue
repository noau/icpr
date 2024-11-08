<template>
  <el-row class="login-page" justify="center" align="middle">
    <el-col :span="6" class="form">
      <el-tabs v-model="selectedRole" type="border-card" class="role-tabs">
        <!-- 学生端 -->
        <el-tab-pane label="学生" name="student">
          <el-form
              :model="formModel"
              :rules="rules"
              ref="form"
              size="large"
              autocomplete="off"
          >
            <el-form-item>
              <h1>学生登录</h1>
            </el-form-item>
            <el-form-item prop="username">
              <el-input
                  v-model="formModel.username"
                  :prefix-icon="User"
                  placeholder="请输入学号"
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
                <el-input
                    placeholder="请输入验证码"
                    v-model="sidentifyMode"
                    clearable
                    style="margin-top: -15px; margin-left: -64px;"
                ></el-input>
                <div class="code" @click="refreshCode">
                  <SIdentify :identifyCode="identifyCode"></SIdentify>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button round @click="login" class="button" type="primary">
                登录
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-link type="primary" :underline="false" class="forgot-password-link" @click="forgotPasswordModal = true">
                找回密码
              </el-link>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 教师端 -->
        <el-tab-pane label="老师" name="teacher">
          <el-form
              :model="formModel"
              :rules="rules"
              ref="form"
              size="large"
              autocomplete="off"
          >
            <el-form-item>
              <h1>教师登录</h1>
            </el-form-item>
            <el-form-item prop="username">
              <el-input
                  v-model="formModel.username"
                  :prefix-icon="User"
                  placeholder="请输入工号"
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
                <el-input
                    placeholder="请输入验证码"
                    v-model="sidentifyMode"
                    clearable
                    style="margin-top: -15px; margin-left: -64px;"
                ></el-input>
                <div class="code" @click="refreshCode">
                  <SIdentify :identifyCode="identifyCode"></SIdentify>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button round @click="login" class="button" type="primary">
                登录
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-link type="primary" :underline="false" class="forgot-password-link" @click="forgotPasswordModal = true">
                找回密码
              </el-link>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 管理员端 -->
        <el-tab-pane label="管理员" name="administrator">
          <el-form
              :model="formModel"
              :rules="rules"
              ref="form"
              size="large"
              autocomplete="off"
          >
            <el-form-item>
              <h1>管理员登录</h1>
            </el-form-item>
            <el-form-item prop="username">
              <el-input
                  v-model="formModel.username"
                  :prefix-icon="User"
                  placeholder="请输入管理员账号"
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
                <el-input
                    placeholder="请输入验证码"
                    v-model="sidentifyMode"
                    clearable
                    style="margin-top: -15px; margin-left: -64px;"
                ></el-input>
                <div class="code" @click="refreshCode">
                  <SIdentify :identifyCode="identifyCode"></SIdentify>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button round @click="login" class="button" type="primary">
                登录
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-link type="primary" :underline="false" class="forgot-password-link" @click="forgotPasswordModal = true">
                找回密码
              </el-link>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <!-- 找回密码对话框 -->
      <el-dialog title="找回密码" align-center v-model="forgotPasswordModal" append-to-body width="350px" :modal-append-to-body="false" center custom-class="forgot-password-dialog">
        <div class="forgot-password-options">
          <el-button round @click="forgotPassword('email')">通过邮箱找回</el-button>
          <el-button round style="margin-left: 0;" @click="forgotPassword('phone')">通过手机找回</el-button>
        </div>
      </el-dialog>
    </el-col>
  </el-row>
</template>

<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { userLoginService } from '@/api/user.js'
import { useUserStore } from '@/stores/user.js'
import { useRouter } from 'vue-router'
import SIdentify from '@/components/Sidentify.vue'
import { ElMessage } from 'element-plus'

const form = ref()
const selectedRole = ref("student")

const formModel = ref({
  username: '',
  password: ''
})
const sidentifyMode = ref('')
const identifyCode = ref('')
const identifyCodes = ref('1234567890abcdefjhijklinopqrsduvwxyz')
const forgotPasswordModal = ref(false) // 新增找回密码对话框的状态

onMounted(() => {
  identifyCode.value = ''
  makeCode(identifyCodes.value, 4)
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const randomNum = (min, max) => Math.floor(Math.random() * (max + 1 - min) + min)
const makeCode = (o, l) => {
  identifyCode.value = ''
  for (let i = 0; i < l; i++) {
    identifyCode.value += o[randomNum(0, o.length)]
  }
}
const refreshCode = () => {
  identifyCode.value = ''
  makeCode(identifyCodes.value, 4)
}

const userStore = useUserStore()
const router = useRouter()

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

  formModel.value.role = selectedRole.value
  await form.value.validate()
  const res = await userLoginService(formModel.value)
  userStore.setToken(res)
  localStorage.setItem('token', res)
  userStore.setId(formModel.value.username)
  localStorage.setItem('userId', formModel.value.username)
  console.log(selectedRole.value)
  if (selectedRole.value === 'student') {
    userStore.setType('student')
    await router.push('/stu-end')
  } else if (selectedRole.value === 'teacher') {
    userStore.setType('teacher')
    await router.push('/tea-end')
  } else if (selectedRole.value === 'administrator') {
    userStore.setType('administrator')
    await router.push('/adm-end')
  }
}

// 找回密码函数
const forgotPassword = (method) => {
  if (method === 'email') {
    router.push('/forgot-password/email') // 跳转到邮箱找回页面
  } else if (method === 'phone') {
    router.push('/forgot-password/phone') // 跳转到手机找回页面
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
  background: url('@/assets/img/bjtu.jpg') no-repeat center / cover;
  display: flex;
  align-items: center;
  justify-content: center;
}
.form {
  background-color: rgba(255, 255, 255, 0.0);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  .title {
    margin: 0 auto;
  }
  .role-tabs {
    display: flex;
    height: 100%;
    width: 100%;
    background-color: rgba(255, 255, 255, 0.9);
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
  display: flex;
  align-items: center;
  gap: 10px;
}
.code {
  cursor: pointer;
}
</style>
