<template>
  <el-table :data="props.list" style="width: 100%">
    <!-- <el-table-column prop="date" label="成绩项" width="180" /> -->
    <el-table-column prop="name1" label="0%-59%" width="180" />
    <el-table-column prop="name2" label="60%-69%" width="180" />
    <el-table-column prop="name3" label="70%-79%" width="180" />
    <el-table-column prop="name4" label="80%-89%" width="180" />
    <el-table-column prop="name5" label="90%-99%" width="180" />
    <el-table-column prop="name6" label="100%" width="180" />
  </el-table>
</template>

<script lang="ts" setup>
const props = defineProps({
  list: {
    type: Array,
    default: () => [] // 默认是空数组

  }
})
// const tableData = [
//   {
//     name1: '5',
//     name2: '5',
//     name3: '5',
//     name4: '5',
//     name5: '5',
//     name6: '5'
//   }
// ]
</script>