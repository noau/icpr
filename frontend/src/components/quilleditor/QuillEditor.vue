<template>
  <div ref="editorContainer"></div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import Quill from 'quill';
import 'quill-css'; // 使用你在 vite.config.js 中定义的 CSS 别名

export default {
  props: ['modelValue'],
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    const editorContainer = ref(null);
    let quill;

    onMounted(() => {
      quill = new Quill(editorContainer.value, {
        theme: 'snow',
        modules: {
          toolbar: [
            // 字体样式
            [{ 'font': [] }], // 字体类型
            [{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小

            // 标题
            [{ 'header': [1, 2, 3, 4, 5, 6, false] }],

            // 强调
            ['bold', 'italic', 'underline', 'strike'], // 加粗、斜体、下划线、删除线

            // 颜色和背景
            [{ 'color': [] }, { 'background': [] }], // 字体颜色、背景颜色

            // 脚注
            [{ 'script': 'super' }, { 'script': 'sub' }], // 上标、下标

            // 列表和缩进
            [{ 'list': 'ordered' }, { 'list': 'bullet' }], // 有序、无序列表
            [{ 'indent': '-1' }, { 'indent': '+1' }], // 缩进

            // 对齐方式
            [{ 'align': [] }],

            // 方向
            [{ 'direction': 'rtl' }], // 文字方向

            // 链接和媒体
            ['link', 'image', 'video'], // 超链接、图片、视频

            // 引用和代码块
            ['blockquote', 'code-block'], // 引用、代码块

            // 清除格式
            ['clean'] // 清除格式
          ],
        },
      });

      quill.on('text-change', () => {
        emit('update:modelValue', quill.root.innerHTML);
      });

      // 设置初始值
      if (props.modelValue) {
        quill.root.innerHTML = props.modelValue;
      }
    });

    onBeforeUnmount(() => {
      quill = null; // 清理
    });

    return { editorContainer };
  },
};
</script>
