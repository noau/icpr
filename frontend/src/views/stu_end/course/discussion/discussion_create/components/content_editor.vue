<template>
  <div>
    <QuillEditor v-model="editorContent" class="custom-quill-editor"></QuillEditor>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import QuillEditor from '@/components/quilleditor/QuillEditor.vue';

const props = defineProps({
  modelValue: String,
});
const emits = defineEmits(['update:modelValue']);

const editorContent = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emits('update:modelValue', value);
  },
});
</script>

<style scoped>
::v-deep .custom-quill-editor .ql-editor {
  min-height: 650px;
}
</style>
