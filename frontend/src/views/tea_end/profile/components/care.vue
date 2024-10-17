<template> 
    <el-dialog 
      :modelValue="dialogVisible"
      title="关注"
      width="800" 
    >
        <div class="flex  itemBlock" v-for="i in 10">
            <div class="flex  flex-align-center">
                <el-avatar :size="60" style="margin-right: 20px;"
                    src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                />
                <div>
                    <h2>姓名:{{ i?.subscriptionName }}</h2>
                    <label class="mg10">学号:22301999</label>
                </div> 
            </div> 
            <div >
                <el-button type="primary" plain>取消关注</el-button>
            </div>
        </div> 
    </el-dialog>
  </template>
  
  <script lang="ts" setup>
  import { ref,defineProps,reactive } from 'vue' 
  import { userSubscriptions } from '@/api/user.js'  

  const props = defineProps({
      dialogVisible:{
          type:Boolean, 
      }
  }) 
  const getCare = async()=>{
    const res = await userSubscriptions({id:123}) 
  }
  getCare();
  </script>
  <style scoped>
    .itemBlock{
        padding: 10px 0;
        width: 100%;
        border-bottom:1px solid #ddd;
    }
 </style>