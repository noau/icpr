<template v-slot:header>
  <el-card class="reminder-card">
    <div>
      <span style="font-size: 18px; font-weight: bold; text-align: center">重要提醒</span>
      <br>
      <br>
    </div>
    <div v-if="reminders.length">
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
</style>
