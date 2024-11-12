<template>
  <el-row :gutter="20">
    <el-col :span="12"><chart1 :list="list"></chart1></el-col>
    <el-col :span="12"><chart2 :list="list"></chart2></el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="24"><chart3 :list="list"></chart3></el-col> 
  </el-row>
</template>

<script setup>
import chart1 from './components/chart1.vue'
import chart2 from './components/chart2.vue'
import chart3 from './components/chart3.vue' 
import {getgrade,getAllgrade} from "@/api/course"
import { useRoute } from "vue-router"
const route = useRoute()
import { onMounted, ref } from 'vue';
let list1 = ref([])
let list2 = ref([])
let list3 = ref([])

onMounted(async() => {
    let data = await getgrade(route.query.id)
    console.log(data);
    list1.value=data.gradeList.map(async(item)=>{
      let obj = {}
      obj.value = item.name
      obj.name = item.num
      let data = await getAllgrade(item.submissionId)
      console.log(data);
       
    })
    console.log(route.query.id);
    
})
</script>

<style>
/* 你的样式 */
</style>