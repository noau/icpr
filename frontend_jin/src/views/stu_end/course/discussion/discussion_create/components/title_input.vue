<template>
  <el-input 
    v-model="inputValue" 
    placeholder="请输入文章标题"
    :class="['title-input', $attrs.class]"
  />
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  modelValue: String,
});
const emits = defineEmits(['update:modelValue']);

// 使用计算属性来代理 modelValue
const inputValue = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emits('update:modelValue', value);
  },
});
</script>

<style scoped>
.title-input {
  width: 100%;
  margin-bottom: 20px;
}
</style>
