<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { userLoginService } from '@/api/user.js'
// 登录表单数据模型
const loginData = ref({
  username: '',
  password: ''
})

// 登录表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入学/工号', trigger: 'blur' },
    { min: 3, max: 10, message: '学/工号长度应在3到10个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 12, message: '密码长度应在6到12个字符之间', trigger: 'blur' }
  ]
}
// 登录函数
const handleLogin = async () => {
  // 调用接口，完成登录
  let result = await userLoginService(loginData.value);
  if (result.code === 0) {
    alert('登录成功')
  } else {
    alert('登录失败')
  }
}

</script>

<template>
  <div class="login-page">
    <div class="bg"></div>
    <div class="form-container">
      <!-- 登录表单 -->
      <el-form ref="form" size="large" autocomplete="off" :model="loginData" :rules="loginRules">
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input :prefix-icon="User" placeholder="请输入学/工号" v-model="loginData.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            name="password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
            v-model="loginData.password"
          ></el-input>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox>记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button round class="button" type="primary" auto-insert-space @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-page {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100vh;
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  .bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: url('@/assets/img/bjtu.jpg') no-repeat 60% center / 240px auto,
      url('@/assets/img/bjtu.jpg') no-repeat center / cover;
    z-index: -1;
  }
  .form-container {
    background-color: rgba(255, 255, 255, 0.9);
    padding: 2rem;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    .form {
      display: flex;
      flex-direction: column;
      justify-content: center;
      user-select: none;
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
    }
  }
}
</style>