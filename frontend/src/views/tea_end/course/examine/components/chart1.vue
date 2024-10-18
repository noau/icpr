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
                { value: 23, name: '10-20' },
                { value: 735, name: '20-30' },
                { value: 580, name: '20-30' },
                { value: 484, name: '60-80' },
                { value: 300, name: '80-100' }
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