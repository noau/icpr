<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="date" label="成绩项" width="180" />
    <el-table-column prop="name" label="0%-59%" width="180" />
    <el-table-column prop="name" label="60%-69%" width="180" />
    <el-table-column prop="name" label="70%-79%" width="180" />
    <el-table-column prop="name" label="80%-89%" width="180" />
    <el-table-column prop="name" label="90%-99%" width="180" />
    <el-table-column prop="name" label="100%" width="180" />
  </el-table>
</template>

<script lang="ts" setup>
const tableData = [
  {
    date: '2016-05-03',
    name: '5',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-02',
    name: '6',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-04',
    name: '6',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-01',
    name: '7',
    address: 'No. 189, Grove St, Los Angeles',
  },
]
</script>