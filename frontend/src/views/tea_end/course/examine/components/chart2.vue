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
      data: ['0%-59%', '60%-69%', '70%-79%', '80%-89%', '90%-99%', '100%']
      
    },
    yAxis: {},
    series: [{
      name: '分数',
      type: 'bar',
      data: [5, 20, 36, 10, 10, 20]
    }]
  };
 
  chart.setOption(option);
});
</script>
 
<style>
/* 你的样式 */
</style>