<!-- <template>
  <div class="posts-list">
    <el-card v-for="post in posts" :key="post.id" class="post-card">
      <h3>{{ post.title }}</h3>
      <p>{{ post.contentSnippet }}...</p>
      <div class="post-actions">

    
         <Like 
          :theme="post.liked ? 'filled' : 'outline'" 
          size="15" 
          fill="#333" 
          @click="toggleLike(post)" 
        />
        <span>{{ post.likes }}</span>


        <el-icon :size="15" @click="toggleFavorite(post)">
          <CollectionTag :theme="post.favorited ? 'filled' : 'outline'" />
        </el-icon>
        <span>{{ post.favorites }}</span>
    
        <el-icon :size="15" @click="goToPost(post)">
          <ChatDotRound />
        </el-icon>
        评论
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import {Like} from '@icon-park/vue-next';

const router = useRouter();


// 模拟的帖子数据，可以通过 prop 传入
const posts = ref([
  { id: 1, title: '讨论1标题', contentSnippet: 'Lorem ipsum dolor sit amet...', likes: 10, favorites: 5 },
  { id: 2, title: '讨论2标题', contentSnippet: 'Lorem ipsum dolor sit amet...', likes: 8, favorites: 3 },
]);


const toggleLike = post => {
  post.liked = !post.liked;
  post.likes += post.liked ? 1 : -1;
};


const toggleFavorite = post => {
  post.favorited = !post.favorited;
  post.favorites += post.favorited ? 1 : -1;
};

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
  position: relative; /* 相对定位 */
  margin-bottom: 20px;
  padding-bottom: 40px; /* 为按钮留出空间 */
}

.post-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end; /* 向左对齐按钮 */
  margin-top: 10px; /* 给按钮与内容之间留出空间 */
  margin-right: 30px; /* 添加右边距，控制距离 */
  transform: translateY(30px); /* 向下移动 10px */
}
</style> -->
<template>
  <div class="posts-list">
    <el-card v-for="post in posts" :key="post.id" class="post-card">
      <h3>{{ post.title }}</h3>
      <p>{{ post.contentSnippet }}...</p>
      <div class="post-actions">
        <Like 
          :theme="post.liked ? 'filled' : 'outline'" 
          size="15" 
          fill="#333" 
          @click="toggleLike(post)" 
        />
        <span>{{ post.likes }}</span>

        <el-icon :size="15" @click="toggleFavorite(post)">
          <CollectionTag :theme="post.favorited ? 'filled' : 'outline'" />
        </el-icon>
        <span>{{ post.favorites }}</span>
    
        <el-icon :size="15" @click="goToPost(post)">
          <ChatDotRound />
        </el-icon>
        评论

        <!-- 删除按钮 -->
        <el-icon :size="15" @click="deletePost(post)">
          <Delete />
        </el-icon>
        删除
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { Like, Delete } from '@icon-park/vue-next';

const router = useRouter();

// 模拟的帖子数据，可以通过 prop 传入
const posts = ref([
  { id: 1, title: '讨论1标题', contentSnippet: 'Lorem ipsum dolor sit amet...', likes: 10, favorites: 5 },
  { id: 2, title: '讨论2标题', contentSnippet: 'Lorem ipsum dolor sit amet...', likes: 8, favorites: 3 },
]);

const toggleLike = post => {
  post.liked = !post.liked;
  post.likes += post.liked ? 1 : -1;
};

const toggleFavorite = post => {
  post.favorited = !post.favorited;
  post.favorites += post.favorited ? 1 : -1;
};

const goToPost = post => {
  router.push(`/stu-end/course/discussion/post/${post.id}`);
};

// 删除帖子功能
const deletePost = post => {
  // 这里可以添加确认删除的逻辑
  const index = posts.value.indexOf(post);
  if (index > -1) {
    posts.value.splice(index, 1);
  }
};
</script>

<style scoped>
.posts-list {
  display: flex;
  flex-direction: column;
}

.post-card {
  position: relative; /* 相对定位 */
  margin-bottom: 20px;
  padding-bottom: 40px; /* 为按钮留出空间 */
}

.post-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end; /* 向左对齐按钮 */
  margin-top: 10px; /* 给按钮与内容之间留出空间 */
  margin-right: 30px; /* 添加右边距，控制距离 */
  transform: translateY(30px); /* 向下移动 10px */
}
</style>
