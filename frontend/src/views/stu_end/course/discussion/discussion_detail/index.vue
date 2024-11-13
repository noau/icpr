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
import {getReplies, getget_thread, getdiscussionlike, getdelete_like} from "@/api/discussion";
import { useUserStore } from "@/stores/user.js";

// 获取路由参数中的帖子ID
const route = useRoute();
const router = useRouter();
const postId = route.params.id;
const isFlag = ref(false);

const userId = localStorage.getItem("userId");

// 帖子数据，通过 API 获取
const post = ref({
  id: postId,
});

// 评论数据，通过 API 获取
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

  if (comment.liked) {
    let obj = {
      userId: localStorage.getItem("userId"),
      threadId: 0,
      replyId: comment.id
    };
    getdiscussionlike(obj).then(res => {
      console.log(res);
    }).catch(err => {
      console.error("点赞失败:", err);
    });
  } else {
    let obj = {
      userId: localStorage.getItem("userId"),
      id: comment.id,
      isThread: 0
    };
    getdelete_like(obj).then(res => {
      console.log(res);
    })
  }
};

// 回复评论
const replyComment = async (comment, replyContent) => {
  console.log(
    comment,
    "commentcommentcomment",
    replyContent,
    "replyContentreplyContent"
  );

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
    userId
  }).then((res) => {
    post.value = res;
    // comments.value =
    console.log(123, res.replyList, "123123");
    const list = [];

    getComments(res.replyList, list);

    // comments.value = res.replyList;
    console.log(comments.value, "comments.valuecomments.value");
  });
}

function getComments(array, list) {
  // 创建一个映射来快速查找评论
  const commentMap = new Map();

  // 遍历每个评论
  array.forEach((item) => {
    const comment = {
      likes: item.likes,
      id: item.id,
      liked: item.liked,
      author: {
        name: item.name,
        avatar: item.avatar || "https://via.placeholder.com/50",
      },
      content: item.content,
      replies: [],
    };

    // 将评论添加到映射中
    commentMap.set(item.id, comment);

    if (item.replyId === 0) {
      // 如果是顶级评论，直接添加到列表中
      list.push(comment);
    } else {
      // 如果是回复，找到父评论
      const parentComment = commentMap.get(item.replyId);
      if (parentComment) {
        // 检查父评论是否是顶级评论
        if (list.includes(parentComment)) {
          // 如果是顶级评论，将回复添加为二级评论
          parentComment.replies.push(comment);
        } else {
          const topLevelComment = list.find(c => c.replies.includes(parentComment));
          if (topLevelComment) {
            topLevelComment.replies.push({
              ...comment,
              content: `@${parentComment.author?.name}：` + item.content,
            });
          }
        }
      }
    }
  });

  comments.value = list;
}
</script>

<style scoped>
.post-detail-page {
  width: 95%; /* 或者不设置 width，让其默认占满父容器 */
  margin: 0; /* 移除自动水平居中对齐 */
  padding: 20px; /* 增加内边距，防止内容贴边 */
}
</style>
