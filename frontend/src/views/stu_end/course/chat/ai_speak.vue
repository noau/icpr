<template>
  <div class="chat-container">
    <div class="chat-box">
      <div v-for="(message, index) in messages" :key="index" class="message">
        <div :class="['message-bubble', message.type]">
          <span>{{ message.text }}</span>
        </div>
      </div>
    </div>

    <!-- 输入框区域，固定在底部 -->
    <div class="input-area">
      <el-input
        v-model="userInput"
        placeholder="请输入你的问题"
        clearable
        @keyup.enter="sendMessage"
        class="input-field"
      />
      <el-button
        type="primary"
        @click="sendMessage"
        class="send-button"
      >
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElInput, ElButton } from 'element-plus';

// 引入你的API函数
import { chatCompletions } from '@/api/chat.js';

// 存储消息列表，包含写死的数据
const messages = ref([
  { text: '你好！我可以为你提供帮助。', type: 'ai' }
]);

// 用户输入的内容
const userInput = ref('');

// 发送消息
async function sendMessage() {
  if (!userInput.value.trim()) return;

  // 添加用户提问
  messages.value.push({ text: userInput.value, type: 'user' });

  // 清空输入框
  const question = userInput.value;
  userInput.value = '';

  // 返回ai的回答
  const res = await chatCompletions({    
    "stream": false,
    "detail": false,
    "messages": [
        {
            "role": "user",
            "content": question
        }
    ]
});
  res.choices[0].message.content?messages.value.push({ text: `这是AI的回答：${res.choices[0].message.content}`,type: 'ai' }) : messages.value.push({ text: 'AI回答：' + '抱歉！AI挂掉了', type: 'ai' });

 
  
  // setTimeout(() => {
  //   // 在这里可以调用你的API获取AI的回答
  //   const answer = `这是AI的回答：${question}`; // 这个是一个示例
  //   messages.value.push({ text: answer, type: 'ai' });
  // }, 500);
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 容器高度占满整个视口 */
  width: 100%;   /* 宽度占满父容器 */
  padding: 10px;
  background-color: #f9f9f9; /* 统一背景色 */
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding-bottom: 60px; /* 为输入框留出空间 */
}

.chat-box {
  flex-grow: 1; /* 让聊天框占满剩余空间 */
  overflow-y: auto;
  margin-bottom: 10px;
}

.message {
  margin-bottom: 15px;
  display: flex;
  justify-content: flex-start; /* 默认对齐到左边 */
}

.message-bubble {
  display: inline-block;
  padding: 10px 15px;
  border-radius: 20px;
  word-wrap: break-word; /* 长单词自动换行 */
  max-width: 50%; /* 最大宽度为父容器的一半 */
  min-width: 20px; /* 设置最小宽度 */
}

.message-bubble.user {
  background-color: #08395f;
  color: #fff;
  /* text-align: right; */
  margin-left: auto; /* 向右对齐 */
  margin-right: 10px;
  direction: ltr; /* 用户消息的换行后从左到右 */
}

.message-bubble.ai {
  background-color: #e2e3e5; /* AI回答：灰色背景 */
  color: #383d41;
  text-align: left;
  margin-left: 10px;
  margin-right: auto; /* 向左对齐 */
  user-select: text; /* 允许文本选择以便复制 */
}

.input-area {
  position: fixed;
  bottom: 0;
  left: 0;
  width: calc(100% - 20px); /* 设置输入框宽度为容器的宽度减去左右的边距 */
  background-color: white;
  padding: 10px;
  display: flex;
  align-items: center;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  z-index: 100;
  margin-left: 0 10px; /* 让输入框宽度与聊天框一致 */
}

.input-field {
  flex-grow: 1;
  margin-right: 10px;
}

.send-button {
  align-self: center;
}
</style>
