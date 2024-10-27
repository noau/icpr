<template>
  <div class="post-actions">
    <!-- 喜欢按钮为 Like 图标 -->
    <div class="action-item" >
      <Like :theme="post.liked ? 'filled' : 'outline'" size="15" fill="#333" @click="toggleLike" />
      <span>{{ post.likes }}</span>
    </div>

    <!-- 收藏按钮为 CollectionTag 图标 -->
    <!-- <div class="action-item" @click="toggleFavorite">
      <el-icon :size="15" @click="toggleFavorite(post)">
        <CollectionTag :theme="post.favorited ? 'filled' : 'outline'" />
      </el-icon>
      <span>{{ post.favorites }}</span>
    </div> -->
  </div>
</template>

<script setup>
import { Like } from '@icon-park/vue-next';
import { getcourse, getdiscussionlike,getdelete_like } from '@/api/discussion';
import { ref, defineProps, defineEmits } from 'vue';

const props = defineProps({
  post: {
    type: Object,
    required: true,
  },
});
// 切换点赞
const toggleLike = () => {
  console.log(props.post);
  const post=props.post;
  post.liked = !post.liked;
  post.likes += post.liked ? 1 : -1;
  // console.log(post);

  let obj = {
    userId: localStorage.getItem("userId"),
    threadId: post.id
  };
  if (post.liked) {
    getdiscussionlike(obj).then(res => {
      console.log(res);
    }).catch(err => {
      console.error("点赞失败:", err);
    });
  }else{
    getdelete_like(obj).then(res => {
      console.log(res);
    })
  }

};
const threadId = ref();
const toggleFavorite = () => {
  const post=props.post;
  threadId.value = post.id;
  post.favorited = !post.favorited;
  post.favorites += post.favorited ? 1 : -1;
  // getUserFolders();
  // drawer.value = true;
};
const emits = defineEmits(['like', 'favorite']);

// const toggleLike = () => {
//   emits('like');
// };

// const toggleFavorite = () => {
//   emits('favorite');
// };
</script>

<style scoped>
.post-actions {
  display: flex;
  gap: 20px;
  /* 调整两个操作项之间的间距 */
  margin-bottom: 20px;
  align-items: center;
  /* 垂直居中对齐 */
}

.action-item {
  display: flex;
  align-items: center;
  /* 使图标和数字垂直居中 */
  cursor: pointer;
}

.action-item span {
  margin-left: 5px;
  /* 调整图标和数字之间的间距 */
}
</style>