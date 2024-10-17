<template> 
    <el-dialog 
      :modelValue="dialogVisible"
      title="粉丝"
      width="800" 
    >
        <div class="flex  flex-align-center itemBlock" style="justify-content: flex-start;" v-for="i in 10">
            <el-avatar :size="60" style="margin-right: 20px;"
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            />
            <div>
                <h2>姓名:{{ i?.followingName }}</h2>
                <label class="mg10">学号:22301999</label>
            </div> 
        </div> 
    </el-dialog>
  </template>
  
  <script lang="ts" setup>
  import { ref,defineProps,reactive } from 'vue'  
  import { userFollowers } from '@/api/user.js'  

  const props = defineProps({
      dialogVisible:{
          type:Boolean, 
      }
  }) 
  const getFans = async()=>{
    const res = await userFollowers({id:123}) 
  }
  getFans();
  </script>
  <style scoped>
    .itemBlock{
        padding: 10px 0;
        border-bottom:1px solid #ddd;
    }
 </style>