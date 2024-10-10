<template>
  <el-header>
    <div class="title">重生的智慧课程平台</div>
  </el-header>
</template>

<script>
export default {
  name: 'adm_header',
};
</script>

<style scoped>
.el-header {
  background-color: #07395f; /* 自定义头部背景色 */
  display: flex;
  align-items: center;
  height: 70px;
  margin-left: -20px;
  margin-right: -20px;
  margin-top: -20px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #ffffff; /* 设置标题的颜色 */
  margin-left: 20px; /* 添加左边距 */
}
</style>
