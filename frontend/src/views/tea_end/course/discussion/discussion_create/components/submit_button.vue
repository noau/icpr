
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
  