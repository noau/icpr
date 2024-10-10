 <template>
    <div class="post-actions">
    <!-- 喜欢按钮为 Like 图标 -->
    <div class="action-item" @click="toggleLike">
    <Like 
      :theme="post.liked ? 'filled' : 'outline'" 
      size="15" 
      fill="#333" 
      @click="toggleLike" 
    />
    <span>{{ post.likes }}</span>
  </div>

    <!-- 收藏按钮为 CollectionTag 图标 -->
    <div class="action-item" @click="toggleFavorite">
    <el-icon :size="15" @click="toggleFavorite(post)">
          <CollectionTag 
          :theme="post.favorited ? 'filled' : 'outline'" />
        </el-icon>
        <span>{{ post.favorites }}</span>
    </div>
  </div>
</template>
  
  <script setup>
  import {Like} from '@icon-park/vue-next';

  const props = defineProps({
  post: {
    type: Object,
    required: true,
  },
});
  
  const emits = defineEmits(['like', 'favorite']);
  
  const toggleLike = () => {
    emits('like');
  };
  
  const toggleFavorite = () => {
    emits('favorite');
  };
  </script>
  
  <style scoped>
  .post-actions {
    display: flex;
    gap: 20px; /* 调整两个操作项之间的间距 */
    margin-bottom: 20px;
    align-items: center; /* 垂直居中对齐 */
  }

  .action-item {
    display: flex;
    align-items: center; /* 使图标和数字垂直居中 */
    cursor: pointer;
  }

  .action-item span {
    margin-left: 5px; /* 调整图标和数字之间的间距 */
  }


</style>

 