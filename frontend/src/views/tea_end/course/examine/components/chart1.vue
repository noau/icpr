<template>
    <div ref="echartsRef" style="width: 600px; height: 400px;"></div> 
  </template>
   
  <script setup>
  import { onMounted, ref } from 'vue';
  import * as echarts from 'echarts';
import {getgrade} from "@/api/course"

const props = defineProps({
  // 默认选中的部门id
  list: {
    type: Array,
    default: null,
  }
})


  const echartsRef = ref(null);
   
  onMounted(async() => {
  setTimeout(() => {
    console.log(props.list);
  }, 1000);
    
    // let data = await getgrade(1)
    // console.log(data);
    
    const chart = echarts.init(echartsRef.value); 
    const option = {
        title: {
            text: '成绩人数分布',
            subtext: 'Fake Data',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
            name: '分数',
            type: 'pie',
            radius: '50%',
            data: [
                { value: 1048, name: '0%-59%' },
                { value: 23, name: '60%-69%' },
                { value: 735, name: '70%-79%' },
                { value: 580, name: '80%-89%' },
                { value: 484, name: '90%-99%' },
                { value: 300, name: '100%' }
            ],
            emphasis: {
                itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
            }
        ]
        };
   
    chart.setOption(option);
  });
  </script>
   
  <style>
  /* 你的样式 */
  </style>