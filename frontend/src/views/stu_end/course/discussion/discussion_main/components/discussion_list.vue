<template>
  <div class="posts-list">
    <el-card v-for="post in filteredPosts" :key="post.id" class="post-card" @click="goToPost(post)" >
      <h3 class="post-title">{{ post.title }}</h3>
      <div class="vHtml" v-html="post.content"></div>
      <div class="post-actions">

        <Like :theme="post.liked ? 'filled' : 'outline'" size="15" fill="#333" @click.stop="toggleLike(post)"/>
        <span>{{ post.likes }}</span>

        <!-- <el-icon :size="15" @click.stop="toggleFavorite(post)">
              <CollectionTag :theme="post.favorite ? 'filled' : 'outline'" />
        </el-icon> -->
        
        <Star :theme="post.favorite ? 'filled' : 'outline'" size="17" fill="#333" @click.stop="toggleFavorite(post)" />
        <span>{{ post.favorites }}</span>
        <el-icon :size="15">
          <ChatDotRound/>
        </el-icon>
        评论
      </div>
    </el-card>

    <el-drawer title="选择收藏夹" v-model="drawer">
      <div v-for="item in collectList" class="blocktag" style="margin-top: 10px;">
        <el-button @click.stop="collect(item)" type="warning">{{ item.name }}</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import {ref, defineProps, computed } from 'vue';
import {useRouter} from 'vue-router';
import {Like, Star} from '@icon-park/vue-next';
import {getcourse, getdiscussionlike, getdelete_like} from '@/api/discussion';
import {userFolders, userCreate_favorite} from '@/api/user.js';
// import { CollectionTag } from '@element-plus/icons-vue/dist/types';

const router = useRouter();
const drawer = ref(false);
// 使用 defineProps 来直接接收父组件传递的 search 属性
const props = defineProps({
  search: {
    type: String,
    required: true
  }
});

// 从后端获取的帖子数据
const posts = ref([]);

// 计算属性: 根据 search 过滤 posts
const filteredPosts = computed(() => {
  const query = props.search
  return posts.value.filter(post => post.title.includes(query))
})

// 获取课程的讨论帖子
function getcourseList() {
  let courseId = localStorage.getItem("courseId"); // 获取课程ID
  const userId = localStorage.getItem("userId");
  getcourse({id: courseId, userId}).then(res => {
    // posts.value = res.data.posts; // 将后端返回的数据绑定到posts
    // console.log(res.discussionThreads, '');
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
  console.log(post);


  if (post.liked) {
    let obj = {
      userId: localStorage.getItem("userId"),
      threadId: post.id
    };
    getdiscussionlike(obj).then(res => {
      console.log('fdsf'+obj);
    }).catch(err => {
      console.error("点赞失败:", err);
    });
  } else {
    let obj = {
      id: post.id,
      userId: localStorage.getItem("userId"),
      isThread: 1
    };
    getdelete_like(obj).then(res => {
      console.log(res);
    })
  }

};


// 获取收藏夹
const collectList = ref();
const getUserFolders = async () => {
  const userId = localStorage.getItem("userId");
  const res = await userFolders({id: userId});
  collectList.value = res.folders;
};

// 收藏帖子
const threadId = ref();
const toggleFavorite = post => {
  threadId.value = post.id;
  // post.favorited = !post.favorited;
  // post.favorites += post.favorited ? 1 : -1;
  getUserFolders();
  drawer.value = true;
};

// 收藏到特定文件夹
function collect(item) {
  drawer.value = true;
  userCreate_favorite({
    userId: localStorage.getItem("userId"),
    threadId: threadId.value,
    folderId: item.id
  }).then(res => {
    // 关闭抽屉
    drawer.value = false;
    // 刷新帖子列表
    getcourseList();
    // 提示用户收藏成功
    alert("收藏成功");
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
  cursor: pointer;
}
.post-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}
.vHtml {
  max-height: 180px;
  /* height: 200px; */
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
  margin-bottom: 10px;
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
