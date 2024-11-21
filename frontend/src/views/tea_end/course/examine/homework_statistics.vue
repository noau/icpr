<template>
  <el-row :gutter="20">
    <el-col v-if="flag" :span="12"><chart1 :list="list1"></chart1></el-col>
    <el-col v-if="flag" :span="12"><chart2 :list="list2"></chart2></el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col v-if="flag" :span="24"><chart3 :list="list3"></chart3></el-col> 
  </el-row>
</template>

<script setup>
import chart1 from './components/chart1.vue'
import chart2 from './components/chart2.vue'
import chart3 from './components/chart3.vue' 
import {getgrade} from "@/api/course"
import { useRoute } from "vue-router"
const route = useRoute()
import { onMounted, ref } from 'vue';

const flag = ref(false)

const list1 = ref([
                { value: 0, name: '0%-59%' },
                { value: 0, name: '60%-69%' },
                { value: 0, name: '70%-79%' },
                { value: 0, name: '80%-89%' },
                { value: 0, name: '90%-99%' },
                { value: 0, name: '100%' }])
const list2 = ref([0, 0, 0, 0, 0, 0])
const list3 = ref([
      {
        name1: 0,
        name2: 0,
        name3: 0,
        name4: 0,
        name5: 0,
        name6: 0
      }
    ])

// onMounted(async() => {

async function getGrade(){
  const res = await getgrade( route.query.id );

  if(res) {
    // res.gradeList = [
    //     {
    //         "submissionId": 42,
    //         "grade": 67
    //     },
    //     {
    //         "submissionId": 76,
    //         "grade": 81
    //     }
    // ]
    res.gradeList.forEach(ele => {
      const grade = (ele.grade / (ele.fullGrade? ele.fullGrade : 100)) * 100;

      // 判断成绩所在的区间并增加对应的value
      if (grade >= 0 && grade <= 59) {
        list1.value[0].value += 1;  // 0%-59%
        list2.value[0] += 1;  // 0%-59%
        list3.value[0].name1 += 1;  // 0%-59%
      } else if (grade >= 60 && grade <= 69) {
        list1.value[1].value += 1;  // 60%-69%
        list2.value[1] += 1;  // 60%-69%
        list3.value[0].name2 += 1;  // 60%-69%
      } else if (grade >= 70 && grade <= 79) {
        list1.value[2].value += 1;  // 70%-79%
        list2.value[2] += 1;  // 70%-79%
        list3.value[0].name3 += 1;  // 70%-79%
      } else if (grade >= 80 && grade <= 89) {
        list1.value[3].value += 1;  // 80%-89%
        list2.value[3] += 1;  // 80%-89%
        list3.value[0].name4 += 1;  // 80%-89%
      } else if (grade >= 90 && grade <= 99) {
        list1.value[4].value += 1;  // 90%-99%
        list2.value[4] += 1;  // 90%-99%
        list3.value[0].name5 += 1;  // 90%-99%
      } else if (grade === 100) {
        list1.value[5].value += 1;  // 100%
        list2.value[5] += 1;  // 100%
        list3.value[0].name6 += 1;  // 100%
      }
    });
  }
  flag.value = true

}
getGrade()
// })
</script>

<style>
/* 你的样式 */
</style>