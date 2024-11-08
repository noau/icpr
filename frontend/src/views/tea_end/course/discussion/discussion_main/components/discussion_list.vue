<template>
  <div class="posts-list">
    <el-card v-for="post in posts" :key="post.id" class="post-card">
      <h3>{{ post.title }}</h3>
      <p>{{ post.contentSnippet }}...</p>
      <div class="post-actions">

        <Like :theme="post.liked ? 'filled' : 'outline'" size="15" fill="#333" @click="toggleLike(post)" />
        <span>{{ post.likes }}</span>

        <el-icon :size="15" @click="toggleFavorite(post)">
          <CollectionTag :theme="post.favorited ? 'filled' : 'outline'" />
        </el-icon>
        <span>{{ post.favorites }}</span>
        <el-icon :size="15" @click="goToPost(post)">
          <ChatDotRound />
        </el-icon>
        评论
        <el-icon :size="15" @click="deletePost(post)">
          <Delete />
        </el-icon>

      </div>
    </el-card>

    <el-drawer title="选择收藏夹" v-model="drawer">
      <div v-for="item in collectList" class="blocktag" style="margin-top: 10px;">
        <el-button @click="collect(item)" type="warning">{{ item.name }}</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { Like } from '@icon-park/vue-next';
import { getcourse, getdiscussionlike,getdelete_like } from '@/api/discussion';
import { userFolders, userCreate_favorite } from '@/api/user.js';

const router = useRouter();
const drawer = ref(false);

// 从后端获取的帖子数据
const posts = ref([]);

// 获取课程的讨论帖子
function getcourseList() {
  let courseId = localStorage.getItem("courseId"); // 获取课程ID
  getcourse({ id: courseId }).then(res => {
    // posts.value = res.data.posts; // 将后端返回的数据绑定到posts
    console.log(res);
    posts.value = res.discussionThreads;
  }).catch(err => {
    console.error("获取帖子数据失败:", err);
  });
}

// 初始化调用，获取课程帖子
getcourseList();

// 切换点赞
const toggleLike = post => {
  post.liked = !post.liked;
  post.likes += post.liked ? 1 : -1;
  let obj = {
    userId: localStorage.getItem("userId"),
    threadId: post.id
  };
  if (!post.liked) {
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

// 获取收藏夹
const collectList = ref();
const getUserFolders = async () => {
  const userId = localStorage.getItem("userId");
  const res = await userFolders({ id: userId });
  collectList.value = res.folders;
};

// 收藏帖子
const threadId = ref();
const toggleFavorite = post => {
  threadId.value = post.id;
  post.favorited = !post.favorited;
  post.favorites += post.favorited ? 1 : -1;
  getUserFolders();
  drawer.value = true;
};

// 收藏到特定文件夹
function collect(item) {
  drawer.value = true;
  userCreate_favorite({ userId: localStorage.getItem("userId"), threadId: threadId.value, folderId: item.id }).then(res => {
    console.log(res);
  }).catch(err => {
    console.error("收藏失败:", err);
  });
}

// 跳转到帖子详情页
const goToPost = post => {
  router.push(`/stu-end/course/discussion/post/${post.id}`);
};
</script>

<style scoped>
.posts-list {
  display: flex;
  flex-direction: column;
}

.post-card {
  position: relative;
  margin-bottom: 20px;
  padding-bottom: 40px;
}

.post-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  align-items: center;
  margin-top: 10px;
  margin-right: 30px;
  transform: translateY(30px);
}
</style>
