<template>
  <div class="comment-item">
    <el-avatar :src="comment.author.avatar" @click="queryUserInfo(comment.author.userId)" />
    <div class="comment-content">
      <div class="comment-author">{{ comment?.author?.name }}</div>
      <div class="comment-text">{{ comment.content }}</div>
      <div class="comment-actions">
        <!-- 喜欢按钮 -->
        <div class="action-item" @click="toggleLike">
          <Like :theme="comment.liked ? 'filled' : 'outline'" size="15" fill="#333" />
          <span>{{ comment.likes }}</span>
        </div>
        <!-- 回复按钮 -->
        <el-button type="text" @click="showReplyInput = !showReplyInput">回复</el-button>
      </div>
      <!-- 回复输入框 -->
      <div v-if="showReplyInput" class="reply-input">
        <div class="input-container">
          <el-input type="textarea" v-model="replyContent" placeholder="写下你的回复..." rows="2" class="reply-textarea" />
          <el-button type="primary" @click="submitReply" :disabled="!replyContent" class="submit-button">
            发布回复
          </el-button>
        </div>
      </div>
      <!-- 显示回复列表 -->
      <div v-if="comment.replies && comment.replies.length" class="reply-list">
        <CommentItem v-for="reply in comment.replies" :key="reply.id" :comment="reply" @likeComment="likeComment"
          @replyComment="replyComment" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Like } from '@icon-park/vue-next';
import CommentItem from './comment_item.vue';
import { userUserInfo } from '@/api/user';
import {useRoute, useRouter} from 'vue-router';
const route = useRoute();
const router = useRouter();

const props = defineProps({
  comment: {
    type: Object,
    required: true,
  },
});

const emits = defineEmits(['likeComment', 'replyComment']);

const showReplyInput = ref(false);
const replyContent = ref('');
const isSelf = ref(true); // 是否是自己的评论

const toggleLike = () => {
  emits('likeComment', props.comment);
};

const submitReply = () => {
  emits('replyComment', props.comment, replyContent.value);
  replyContent.value = '';
  showReplyInput.value = false;
};

const likeComment = (comment) => {
  emits('likeComment', comment);
};

const replyComment = (comment, content) => {
  emits('replyComment', comment, content);
};
// 跳转用户信息页面
const queryUserInfo = async(id) => {
  if (!id) {
    return;
  }
  // await getUserInfo(id);
  if (id == localStorage.getItem('userId')) {
    // 路由到自己的个人信息页面
    router.push('/tea-end/profile');
    return
  }
  // 路由到他人的个人信息页面
  router.push({
    path: '/tea-end/user-home',
    query: {
      id: id
    },
  });
  
};

// 查询用户信息 
const getUserInfo = async(id) => {

  const userInfo = await userUserInfo({ id })
  return userInfo;  
};
</script>

<style scoped>
.comment-item {
  display: flex;
  margin-bottom: 20px;
}

.comment-content {
  margin-left: 10px;
  flex: 1;
}

.comment-author {
  font-weight: bold;
  margin-bottom: 5px;
}

.comment-text {
  margin-bottom: 10px;
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.action-item {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.action-item span {
  margin-left: 5px;
}

.reply-input {
  margin-bottom: 10px;
}

.input-container {
  position: relative;
  margin-bottom: 10px;
  /* 增加输入框与下方内容的间距 */
}

.reply-textarea {
  width: 100%;
  margin-bottom: 0;
  /* 移除默认的下外边距 */
}

.submit-button {
  position: absolute;
  bottom: -40px;
  /* 根据需要调整 */
  right: 0;
}

.reply-list {
  margin-left: 40px;
}
</style>
