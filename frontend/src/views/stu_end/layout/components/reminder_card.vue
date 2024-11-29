<template v-slot:header>
  <el-card class="reminder-card">
    <div>
      <span style="font-size: 18px; font-weight: bold; text-align: center">重要提醒</span>
      <br>
      <br>
    </div>
    <div v-if="reminders.length" class="reminder-container">
      <reminder_item
        v-for="(reminder, index) in reminders"
        :key="index"
        :reminder="reminder"
        @remove="removeReminder(index)"
      />
    </div>
    <div v-else class="no-reminder">暂无提醒</div>
  </el-card>
</template>

<script>
import reminder_item from './reminder_item.vue';

export default {
  props: {
    reminders: {
      type: Array,
      required: true,
    },
  },
  components: {
    reminder_item,
  },
  methods: {
    removeReminder(index) {
      this.$emit('remove', index);
    },
  },
};
</script>

<style scoped>
.reminder-card {
  width: 100%;
  min-height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  box-shadow: none;   
}

.no-reminder {
  color: gray;
  text-align: center;
  font-size: 14px;
  margin-top: 20px;
}

.reminder-container {
  max-height: 400px; /* 设置一个合适的最大高度，视情况而定 */
  overflow-y: auto;  /* 超出高度时显示滚动条 */
  padding-right: 10px; /* 预留空间给滚动条，防止内容被遮挡 */
  border: 1px solid #ddd; /* 可选：为容器添加边框，帮助区分区域 */
  border: none;
}
</style>
