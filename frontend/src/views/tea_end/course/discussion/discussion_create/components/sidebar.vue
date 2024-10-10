<template>
  <div class="sidebar">
    <!-- 笔记摘要 -->
    <el-card class="sidebar-section">
      <p>笔记摘要</p>
      <el-input v-model="localSummary" type="textarea" rows="4" />
    </el-card>

    <!-- 添加话题 -->
    <el-card class="sidebar-section">
      <p>#添加话题</p>
      <el-input v-model="localTags" placeholder="请输入话题标签" />
    </el-card>

    <!-- 可见范围 -->
    <el-card class="sidebar-section">
      <p>可见范围</p>
      <el-radio-group v-model="localVisibility">
        <el-radio label="全部可见">全部可见</el-radio>
        <el-radio label="仅我可见">仅我可见</el-radio>
        <el-radio label="老师可见">老师可见</el-radio>
      </el-radio-group>
    </el-card>

    <!-- 匿名选项 -->
    <el-card class="sidebar-section">
      <p>匿名</p>
      <el-checkbox v-model="localIsAnonymous">我要匿名</el-checkbox>
    </el-card>

    <!-- 通知选项 -->
    <el-card class="sidebar-section">
      <p>通知</p>
      <el-checkbox v-model="localIsNotification">作为通知推送</el-checkbox>
    </el-card>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  summary: String,
  tags: String,
  visibility: String,
  isAnonymous: Boolean,
  isNotification: Boolean, // 新增通知选项的 prop
});

const emits = defineEmits([
  'update:summary',
  'update:tags',
  'update:visibility',
  'update:isAnonymous',
  'update:isNotification', // 新增通知选项的 emit
]);

// 为每个 prop 创建对应的计算属性
const localSummary = computed({
  get: () => props.summary,
  set: (value) => emits('update:summary', value),
});

const localTags = computed({
  get: () => props.tags,
  set: (value) => emits('update:tags', value),
});

const localVisibility = computed({
  get: () => props.visibility,
  set: (value) => emits('update:visibility', value),
});

const localIsAnonymous = computed({
  get: () => props.isAnonymous,
  set: (value) => emits('update:isAnonymous', value),
});

// 新增通知选项的计算属性
const localIsNotification = computed({
  get: () => props.isNotification,
  set: (value) => emits('update:isNotification', value),
});
</script>

<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-section {
  padding: 10px;
}

.sidebar-section p {
  margin-top: 0;       /* 移除上外边距 */
  margin-bottom: 10px; /* 可根据需要调整下外边距 */
}

/* 针对不同的输入组件，增加上外边距 */
.sidebar-section .el-input,
.sidebar-section .el-radio-group,
.sidebar-section .el-checkbox {
  margin-top: 5px; /* 根据需要调整 */
}
</style>
