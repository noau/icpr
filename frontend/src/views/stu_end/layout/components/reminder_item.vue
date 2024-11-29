<template>
  <el-card class="reminder-item">
    <div class="reminder-header">
      <el-icon v-if="reminder.type === '作业'"><Edit /></el-icon>
      <el-icon v-else><Bell /></el-icon>
      <div class="reminder-title">{{ reminder.title }}</div>
    </div>
    <div class="reminder-content">
      <div v-if="reminder.deadline">截止日期: {{ reminder.deadline }}</div>
      <div v-else>通知内容: {{ reminder.content }}</div>
    </div>
    <!-- 修改关闭按钮，放在右下角 -->
    <!-- <el-button round class="close-button" @click="remove">
      <el-icon><Close /></el-icon>
    </el-button> -->
  </el-card>
</template>

<script>
import { Edit, Bell, Close } from '@element-plus/icons-vue'; // 导入Close图标

export default {
  props: {
    reminder: {
      type: Object,
      required: true,
    },
  },
  components: {
    Edit,
    Bell,
    Close, // 注册Close图标
  },
  methods: {
    remove() {
      this.$emit('remove');
    },
  },
};
</script>

<style scoped>
.reminder-item {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  margin-bottom: 10px;
  /* box-shadow: none;    */
}

.reminder-header {
  display: flex;
  align-items: center;
}

.reminder-title {
  padding-left: 10px;
  font-size: 16px;
  font-weight: bold;
}

.reminder-content {
  padding-top: 5px;
  font-size: 14px;
}

/* 关闭按钮放在右下角 */
.close-button {
  position: absolute;
  right: 10px;
  bottom: 0px;
  background: none;
  border: none;
}
</style>
