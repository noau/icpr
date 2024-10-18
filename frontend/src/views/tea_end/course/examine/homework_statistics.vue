<template>
    <el-row :gutter="20">
      <el-col :span="12"><chart1></chart1></el-col>
      <el-col :span="12"><chart2></chart2></el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24"><chart3></chart3></el-col> 
    </el-row>
</template>
 
<script setup>
 import chart1 from './components/chart1.vue'
 import chart2 from './components/chart2.vue'
 import chart3 from './components/chart3.vue' 

</script>
 
<style>
/* 你的样式 */
</style>