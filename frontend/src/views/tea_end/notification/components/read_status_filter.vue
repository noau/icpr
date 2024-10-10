<template>
  <div class="status_select">
    <button 
      :class="['status_btn', { active: modelValue === 'all' }]" 
      @click="handleClick('all')"
    >所有</button>
    <button 
      :class="['status_btn', { active: modelValue === 'unread' }]" 
      @click="handleClick('unread')"
    >未读</button>
  </div>
  <!-- <p>子组件接收到的状态: {{ modelValue }}</p> 显示传入的状态 -->
</template>

<script>
export default {
  props: {
    modelValue: {
      type: String,
      required: true
    }
  },
  methods: {
    handleClick(newStatus) {
      // 按钮点击时，向父组件发出 update:modelValue 事件
      this.$emit('update:modelValue', newStatus); 
    }
  }
};
</script>

<style scoped>
.status_select {
  display: flex;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.status_btn {
  padding: 8px 20px;
  cursor: pointer;
  background-color: #f5f7fa;
  color: #909399;
  transition: background-color 0.3s ease, color 0.3s ease;
  border: none; /* 去掉按钮边框 */
}

.status_btn:hover {
  background-color: #e0e6ed;
}

.status_btn.active {
  background-color: #07395f; /* 激活按钮的背景色 */
  color: white;
}
</style>