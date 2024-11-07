<template>
  <div class="post-detail-page">
    <!-- 帖子头部 -->
    <PostHeader :post="post" />

    <!-- 帖子内容 -->
    <PostContent :content="post.content" />

    <!-- 帖子操作（点赞、收藏） -->
    <PostActions :post="post" @like="handleLike" @favorite="handleFavorite" />

    <!-- 评论输入框 -->
    <CommentInput @submitComment="addComment" :isFlag="isFlag" />

    <!-- 评论列表 -->
    <CommentsList
      :comments="comments"
      @likeComment="likeComment"
      @replyComment="replyComment"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import PostHeader from "./components/post_header.vue";
import PostContent from "./components/post_content.vue";
import PostActions from "./components/post_actions.vue";
import CommentInput from "./components/comment_input.vue";
import CommentsList from "./components/comments_list.vue";
import { useRoute, useRouter } from "vue-router";
import { getReplies, getget_thread } from "@/api/discussion";
import { useUserStore } from "@/stores/user.js";

// 获取路由参数中的帖子ID
const route = useRoute();
const router = useRouter();
const postId = route.params.id;
const isFlag = ref(false);

const userId = localStorage.getItem("userId");

// 模拟帖子数据，可以通过 API 获取
const post = ref({
  id: postId,
});

// 模拟评论数据，可以通过 API 获取
const comments = ref([]);

onMounted(() => {
  // 在这里获取帖子详情和评论列表的数据
  getget_threadList();

});


const threadId = route.params.id;

// 处理点赞
const handleLike = () => {
  post.value.liked = !post.value.liked;
  post.value.likes += post.value.liked ? 1 : -1;
};

// 处理收藏
const handleFavorite = () => {
  post.value.favorited = !post.value.favorited;
  post.value.favorites += post.value.favorited ? 1 : -1;
};

// 点赞评论
const likeComment = (comment) => {
  comment.liked = !comment.liked;
  comment.likes += comment.liked ? 1 : -1;
};

// 回复评论
const replyComment = async (comment, replyContent) => {
  console.log(comment,'commentcommentcomment',replyContent,'replyContentreplyContent');

  await getReplies({
    threadId,
    replyId: comment.id,
    userId: userId,
    content: replyContent,
  });
  // isFlag.value = true;
  getget_threadList();

  // comment.replies.push({
  //   id: Date.now(),
  //   author: {
  //     name: "当前用户",
  //     avatar: "https://via.placeholder.com/50",
  //   },
  //   content: replyContent,
  //   likes: 0,
  //   liked: false,
  // });
};

// 添加评论
const addComment = async (content) => {
  isFlag.value = false;

  await getReplies({
    threadId,
    replyId: 0,
    userId: userId,
    content,
  });
  isFlag.value = true;
  getget_threadList();
};
function getget_threadList() {
  getget_thread({
    id: threadId,
  }).then((res) => {
    post.value = res;
    comments.value = res.replyList.map((item) => {
      return {
        likes: item.likes,
        id: item.id,
        liked: item.liked,
        author: {
          name: item.name,
          avatar: item.avatar || "https://via.placeholder.com/50",
        },
        content: item.content,
        replies: item.replies,
      };
      // .push(obj);
    });
  });
}
</script>

<style scoped>
.post-detail-page {
  width: 95%; /* 或者不设置 width，让其默认占满父容器 */
  margin: 0; /* 移除自动水平居中对齐 */
  padding: 20px; /* 增加内边距，防止内容贴边 */
}
</style>
