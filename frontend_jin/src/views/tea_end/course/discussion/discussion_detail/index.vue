<template>
    <div class="post-detail-page">
      <!-- 帖子头部 -->
      <PostHeader :post="post" />
  
      <!-- 帖子内容 -->
      <PostContent :content="post.content" />
  
      <!-- 帖子操作（点赞、收藏） -->
      <PostActions :post="post" @like="handleLike" @favorite="handleFavorite" />
  
      <!-- 评论输入框 -->
      <CommentInput @submitComment="addComment" />
  
      <!-- 评论列表 -->
      <CommentsList :comments="comments" @likeComment="likeComment" @replyComment="replyComment" />
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import PostHeader from './components/post_header.vue';
  import PostContent from './components/post_content.vue';
  import PostActions from './components/post_actions.vue';
  import CommentInput from './components/comment_input.vue';
  import CommentsList from './components/comments_list.vue';
  import { useRoute } from 'vue-router';
  
  // 获取路由参数中的帖子ID
  const route = useRoute();
  const postId = route.params.id;
  
  // 模拟帖子数据，可以通过 API 获取
  const post = ref({
    id: postId,
    title: '帖子标题',
    author: {
      name: '帖主姓名',
      avatar: 'https://via.placeholder.com/50',
    },
    content: '这是帖子的完整内容...',
    likes: 10,
    favorites: 5,
    liked: false,
    favorited: false,
  });
  
  // 模拟评论数据，可以通过 API 获取
  const comments = ref([]);
  
  onMounted(() => {
    // 在这里获取帖子详情和评论列表的数据
  });
  
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
  
  // 添加评论
  const addComment = (content) => {
    comments.value.push({
      id: Date.now(),
      author: {
        name: '当前用户',
        avatar: 'https://via.placeholder.com/50',
      },
      content: content,
      likes: 0,
      liked: false,
      replies: [],
    });
  };
  
  // 点赞评论
  const likeComment = (comment) => {
    comment.liked = !comment.liked;
    comment.likes += comment.liked ? 1 : -1;
  };
  
  // 回复评论
  const replyComment = (comment, replyContent) => {
    comment.replies.push({
      id: Date.now(),
      author: {
        name: '当前用户',
        avatar: 'https://via.placeholder.com/50',
      },
      content: replyContent,
      likes: 0,
      liked: false,
    });
  };
  </script>
  
  <style scoped>
  .post-detail-page {
  width: 95%;      /* 或者不设置 width，让其默认占满父容器 */
  margin: 0;        /* 移除自动水平居中对齐 */
  padding: 20px;    /* 增加内边距，防止内容贴边 */
}

  </style>
  