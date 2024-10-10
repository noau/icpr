<template>
  <div class="comments-list">
    <h2>评论</h2>
    <CommentItem
      v-for="comment in comments"
      :key="comment.id"
      :comment="comment"
      @likeComment="likeComment"
      @replyComment="replyComment"
    />
  </div>
</template>

<script setup>
import CommentItem from './comment_item.vue';

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
