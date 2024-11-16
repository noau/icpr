<template>
    <div ref="echartsRef" style="width: 600px; height: 400px;"></div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import * as echarts from 'echarts';


// 获取父组件传递的数据
const props = defineProps({
  list: {
    type: Array,
    default: () => [] // 默认是空数组
  }
});

const echartsRef = ref(null);
   
onMounted(async() => {
    const chart = echarts.init(echartsRef.value); 
    const option = {
        title: {
            text: '成绩人数分布',
            subtext: '占比人数',
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
            data: props.list,
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