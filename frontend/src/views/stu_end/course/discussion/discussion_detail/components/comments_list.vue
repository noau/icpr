<template>
  <div class="comments-list">
    <h2>评论</h2>
    <div  v-for="comment in comments" :key="comment.id"> 
      <comment_item :comment="comment"
        @likeComment="likeComment"
        @replyComment="replyComment">
      </comment_item>
    </div>
  </div>
</template>

<script setup>
import comment_item from './comment_item.vue';
import {  getget_thread } from '@/api/discussion';
import { ref } from 'vue';
import { useRoute } from 'vue-router'
const route = useRoute();

const props = defineProps({
  comments: Array,
});

const emits = defineEmits(['likeComment', 'replyComment']);

const likeComment = (comment) => {
  emits('likeComment', comment);
};

const replyComment = (comment, replyContent) => {
  emits('replyComment', comment, replyContent);
};

</script>

<style scoped>
.comments-list {
  margin-top: 20px;
}

.comments-list h2 {
  margin-bottom: 20px;
}
</style>
