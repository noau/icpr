<!-- <template>
    <el-button 
    type="primary" 
    @click="submitPost">
    发布文章</el-button>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  const submitPost = () => {
    // 此处为提交逻辑
    console.log('提交文章');
    router.push('/stu-end/course/discussion'); // 跳转到讨论页面
  };
  </script>
  
  <style scoped>
  .el-button {
    margin-top: 20px;
    align-self: flex-end;
  }
  </style>
   -->
   <template>
    <el-button 
      type="primary" 
      @click="$emit('submit')" 
      class="submit-button"
      :class="$attrs.class"
    >
      发布文章
    </el-button>
  </template>
  
  <script setup>
  // 不需要额外的脚本
  </script>
  