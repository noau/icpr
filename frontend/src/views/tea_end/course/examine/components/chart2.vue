<template>
    <div ref="echartsRef" style="width: 600px; height: 400px;"></div> 
  </template>
   
  <script setup>
  import { onMounted, ref } from 'vue';
  import * as echarts from 'echarts';
   
  const echartsRef = ref(null);
   
  onMounted(() => {
    const chart = echarts.init(echartsRef.value);
    const option = {
      // ECharts 配置项
      title: {
        text: '成绩人数分布'
      },
      tooltip: {},
      xAxis: {
        data: ['10-20', '20-30', '30-40', '40-50', '50-60', '60-80', '80-100']
      },
      yAxis: {},
      series: [{
        name: '分数',
        type: 'bar',
        data: [5, 20, 36, 10, 10, 20,5]
      }]
    };
   
    chart.setOption(option);
  });
  </script>
   
  <style>
  /* 你的样式 */
  </style>