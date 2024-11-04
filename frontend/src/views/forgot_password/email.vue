<script setup>
  import { ref, onMounted } from 'vue'
  import { User, Lock} from '@element-plus/icons-vue'
  import SIdentify from '@/components/SIdentify.vue'
  import { ElMessage } from 'element-plus'
  import { useRouter } from 'vue-router'
  import { resetPwdEmail } from '@/api/user.js'    
  const router = useRouter()
  
  const formModel = ref({
    username: '',
    idNumber: '',
    email: ''
  })
  const sidentifyMode = ref('') // 输入框验证码
  const identifyCode = ref('') // 图形验证码
  const identifyCodes = ref('1234567890abcdefjhijklinopqrsduvwxyz') // 验证码出现的数字和字母
  
  // 组件挂载
  onMounted(() => {
    identifyCode.value = ''
    makeCode(identifyCodes.value, 4)
  })
  
  // 生成随机数
  const randomNum = (min, max) => {
    max = max + 1
    return Math.floor(Math.random() * (max - min) + min)
  }
  // 随机生成验证码字符串
  const makeCode = (o, l) => {
    for (let i = 0; i < l; i++) {
      identifyCode.value += o[randomNum(0, o.length)]
    }
  }
  // 更新验证码
  const refreshCode = () => {
    identifyCode.value = ''
    makeCode(identifyCodes.value, 4)
  }
  
  const sendEmail = async() => {
    // 验证用户名不为空
    if (!formModel.value.username) {
      ElMessage({ type: 'error', message: '学/工号不能为空！' })
      return
    }
    // 验证身份证号不为空
    if (!formModel.value.idNumber) {
      ElMessage({ type: 'error', message: '身份证号不能为空！' })
      return
    }
    // 验证校内邮箱不为空
    if (!formModel.value.email) {
      ElMessage({ type: 'error', message: '校内邮箱不能为空！' })
      return
    }
    // 验证验证码不为空
    if (!sidentifyMode.value) {
      ElMessage({ type: 'error', message: '验证码不能为空！' })
      return
    }
    // 验证验证码是否正确
    if (sidentifyMode.value !== identifyCode.value) {
      ElMessage({ type: 'error', message: '验证码错误！' })
      refreshCode()
      return
    }
    // 验证通过，发送邮件
    // const targetEmail = `${formModel.value.username}@bjtu.edu.cn`
 
    const res = await resetPwdEmail({ 
      "id": formModel.value.username,
      "email": formModel.value.email,
      "idCardNumber": formModel.value.idNumber
    })  
    // console.log(`发送找回密码邮件到 ${targetEmail}`)
    alert('已发送找回密码邮件')
  }

  const goBack = () => {
    router.back()
  }
</script>
  
<template>
  <el-row class="forgot-password-page" justify="center" align="middle">
    <el-col :span="8" class="form">
      <el-button round type="primary" @click="goBack" class="back-button">返回</el-button>
      <el-form :model="formModel" ref="form" size="large" autocomplete="off">
        <el-form-item>
          <h1>系统自助找回密码</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            v-model="formModel.username"
            :prefix-icon="User"
            placeholder="请输入学/工号"
          ></el-input>
        </el-form-item>
        <el-form-item prop="idNumber">
          <el-input
            v-model="formModel.idNumber"
            :prefix-icon="Lock"
            placeholder="请输入身份证号"
          ></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input
            v-model="formModel.email"
            :prefix-icon="Lock"
            placeholder="请输入校内邮箱"
          ></el-input>
        </el-form-item>
        
        <el-form-item label-width="85px" style="margin-bottom: 5px;">
          <div class="code-row">
            <el-input placeholder="请输入验证码" v-model="sidentifyMode" clearable style="margin-top: -15px; width: 200px; margin-left: -84px; margin-right: 10px;"></el-input>
            <div class="code" @click="refreshCode">
              <SIdentify :identifyCode="identifyCode"></SIdentify>
            </div>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button round type="primary" @click="sendEmail">发送邮件</el-button>
        </el-form-item>
        <p>请输入您的账号及验证信息，我们将向您相应校内邮箱地址发送一封邮件作为您重新设置密码。</p>
      </el-form>
    </el-col>
  </el-row>
</template>
  
<style scoped>
.forgot-password-page {
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
  background-color: rgba(255, 255, 255, 0.8);
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
 }
  .title {
    margin: 0 auto;
  }
  .button {
    width: 100%;
  }
  .back-button {
    position: absolute;
    top: 20px;
    left: 20px;
  }
  p {
    margin-top: 1rem;
    font-size: 0.9rem;
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