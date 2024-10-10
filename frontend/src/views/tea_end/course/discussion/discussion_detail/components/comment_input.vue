 <template>
    <div class="comment-input">
      <el-input
        type="textarea"
        v-model="content"
        placeholder="写下你的评论..."
        rows="3"
        class="comment-textarea"
      />
      <div class="button-container">
        <el-button
          type="primary"
          @click="submitComment"
          :disabled="!content"
          class="submit-button"
        >
          发布评论
        </el-button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const emits = defineEmits(['submitComment']);
  
  const content = ref('');
  
  const submitComment = () => {
    emits('submitComment', content.value);
    content.value = '';
  };
  </script>
  
  <style scoped>
  .comment-input {
    margin-bottom: 20px;
  }
  
  .comment-textarea {
    margin-bottom: 10px; /* 控制输入框与按钮之间的距离 */
  }
  
  .button-container {
    text-align: right; /* 将按钮右对齐 */
  }
  
  .submit-button {
    /* 如需调整按钮样式，可在此处添加 */
  }
  </style>
  