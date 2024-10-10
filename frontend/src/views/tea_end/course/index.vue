<template>
  <div class="course-layout">
    <!-- Header -->
    <el-header>
      <course_header />
    </el-header>
    <br>
    <!-- 主体布局 -->
    <el-container>
      <el-aside width="200px"><navbar></navbar></el-aside>
      <el-container class="container">
        <el-main>
          <!-- 子路由 -->
          <router-view/>
        </el-main>
      </el-container>  
    </el-container>
  </div>
</template>

<script setup>
import course_header from '@/components/tea_end/course_header.vue';
// import { ElButton, ElInput } from 'element-plus'
// import { ref } from 'vue'
import navbar from './components/nav_bar.vue'
// import breadcrumb from './components/bread_crumb.vue'
</script>

<style scoped>
.course-layout {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
.container{
  padding:5px 0 0 15px;
}
.input {
  display: inline;
  margin: 20px 30px;
}
 
.button {
  width: 90px;
}
</style>
