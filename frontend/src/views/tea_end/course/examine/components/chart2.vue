<template>
  <div ref="echartsRef" style="width: 600px; height: 400px;"></div> 
</template>
 
<script setup>
import { onMounted, ref } from 'vue';
import * as echarts from 'echarts';
 
const echartsRef = ref(null);

 // 获取父组件传递的数据
const props = defineProps({
  list: {
    type: Array,
    default: () => [] // 默认是空数组
  }
});

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
      data: props.list
    }]
  };
 
  chart.setOption(option);
});

</script>
 
<style>
/* 你的样式 */
</style>