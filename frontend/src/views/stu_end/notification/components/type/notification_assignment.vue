<template>
  <div>
    <!-- 状态选择与筛选 -->
    <div class="filter-bar">
      <div class="search-bar">
        <el-input
            placeholder="搜索"
            v-model="localSearchText"
            @keyup.enter="submitSearch"
            class="search-input"
        />
        <el-select
            :value="sortOption"
            @change="updateSortOption"
            placeholder="排序方式"
            class="sort-select"
        >
          <el-option label="日期" value="date"></el-option>
          <el-option label="标题" value="title"></el-option>
        </el-select>
      </div>
    </div>
    <!-- 通知列表显示 -->
    <el-table :data="filteredNotifications" style="width: 100%">
      <el-table-column prop="courseId" label="课程ID" width="300"/>
      <el-table-column prop="userName" label="发送者" width="180"/>
      <el-table-column prop="content" label="标题" width="250"/>
      <el-table-column prop="createdAt" label="时间" width="200"/>

      <!-- 收藏标识 -->
      <el-table-column label="收藏" width="100">
        <template #default="scope">
          <el-icon
              :class="{ 'active': scope.row.isStar }"
              @click="toggleStar(scope.row)">
            <template v-if="scope.row.isStar">
              <StarFilled/>
            </template>
            <template v-else>
              <Star/>
            </template>
          </el-icon>
        </template>
      </el-table-column>

      <!-- 已读/未读标识 -->
      <el-table-column label="已读" width="100">
        <template #default="scope">
          <el-icon
              :class="{ 'active': scope.row.isRead }"
              :style="{ color: scope.row.isRead ? 'green' : 'inherit' }"
              @click="toggleCompleted(scope.row)">
            <template v-if="scope.row.isRead">
              <CircleCheckFilled/>
            </template>
            <template v-else>
              <CircleCheck/>
            </template>
          </el-icon>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200">
        <template #default="scope">
          <div style="display: flex; gap: 10px;">
            <el-button class="details-button" type="info" @click="showDetails(scope.row)">详情</el-button>
            <el-button type="danger" @click="deleteNotification(scope.row.id)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-drawer
        title="通知详情"
        class="drawer"
        v-model="dialogVisible"
        direction="rtl"
        size="30%"
        :before-close="drawerClose"
    >
      <div v-if="selectedNotification" class="notification-details">
        <h2 class="notification-title">{{ selectedNotification.content }}</h2>
        <p><strong>发送人:</strong> {{ selectedNotification.userName }}</p>
        <p><strong>内容:</strong> {{ selectedNotification.content }}</p>
        <p><strong>时间:</strong> {{ new Date(selectedNotification.createdAt).toLocaleString() }}</p>
        <p><strong>通知类型:</strong> {{ selectedNotification.type }}</p>
      </div>
      <div v-else>
        <p>未选择任何通知。</p>
      </div>
    </el-drawer>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="notificationList.length"
        class="pagination"
    />
  </div>
</template>

<script lang="ts" setup>
import {
  updateCollectionNotification,
  typeNotificationsByTypeGet,
  deleteSignalNotification,
  updateReadNotification
} from '@/api/notification.js';
import {useUserStore} from '@/stores/user.js';
import {ref,computed} from 'vue';
import {CircleCheck, CircleCheckFilled, Star, StarFilled} from "@element-plus/icons-vue";

const userId = useUserStore()?.id;
const notificationType = "作业通知";

const notificationList = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);

const init = async () => {
  try {
    let res = await typeNotificationsByTypeGet({id: userId, type: notificationType});
    console.log(res);
    notificationList.value = res.notifications;
  } catch (error) {
    console.error("获取通知列表时出错:", error);
  }
};
//筛选部分
const localSearchText = ref(''); // 本地搜索文本
const sortOption = ref('date'); // 排序选项

const filteredNotifications = computed(() => {
  let notifications = notificationList.value;

  // 使用正则表达式进行模糊搜索（不区分大小写，部分匹配）
  if (localSearchText.value) {
    const searchRegex = new RegExp(localSearchText.value, 'i');
    notifications = notifications.filter(notification => searchRegex.test(notification.content));
  }

  // 根据排序选项排序通知
  if (sortOption.value === 'date') {
    notifications.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
  } else if (sortOption.value === 'title') {
    notifications.sort((a, b) => a.content.localeCompare(b.content));
  }

  return notifications;
});
// 提交搜索
const submitSearch = () => {
  // 同步搜索文本到父组件
};

// 更新排序选项
const updateSortOption = (value) => {
  sortOption.value = value;
};
// 详情页面
const dialogVisible = ref(false);
const selectedNotification = ref(null);

const showDetails = (notification) => {
  selectedNotification.value = notification;
  dialogVisible.value = true;
};
const drawerClose = () =>{
  dialogVisible.value = false;
}

// 切换收藏状态
const toggleStar = async (notification) => {
  notification.isStar = !notification.isStar;
  notification.isStar = notification.isStar ? 1 : 0; // 转换为 1 和 0
  try {
    await updateCollectionNotification({...notification});
    if (!notification.isStar) {
      notificationList.value = notificationList.value.filter(n => n.id !== notification.id);
    }
  } catch (error) {
    console.error("更新收藏状态时出错:", error);
  }
};

// 切换已读状态
const toggleCompleted = async (notification) => {
  notification.isRead = !notification.isRead;
  notification.isRead = notification.isRead ? 1 : 0;
  try {
    await updateReadNotification({...notification});
  } catch (error) {
    console.error("更新已读状态时出错:", error);
  }
};

// 删除通知函数
const deleteNotification = async (notificationId) => {
  try {
    await deleteSignalNotification(notificationId);
    notificationList.value = notificationList.value.filter(notification => notification.id !== notificationId);
  } catch (error) {
    console.error("删除通知时出错:", error);
  }
};

// 分页处理函数
const handleSizeChange = (size) => {
  pageSize.value = size;
  init();
};

const handleCurrentChange = (page) => {
  currentPage.value = page;
  init();
};

const handleClose = (done) => {
  done();
};

init();
</script>

<style scoped>
.filter-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
.search-bar {
  display: flex;
  align-items: center;
  gap: 20px;
  width: 100%;
  .sort-select{
    max-width: 200px;
  }
}
.notification-time {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.icons {
  display: flex;
  align-items: center;
}

.icons el-icon {
  margin-right: 10px;
}

.el-icon.active {
  color: #de8f0f !important;
}

.pagination {
  margin-top: 10px;
  text-align: center;
}

.details-button {
  background-color: #003366;
  color: white;
  border-color: #003366;
}

.details-button:hover {
  background-color: #002244;
}

.el-icon {
  color: inherit; /* 继承父元素颜色，防止被覆盖 */
}

.el-icon[style*="color: green"] {
  color: green !important; /* 确保已读状态的颜色为绿色 */
}

.drawer {
  z-index: 2000 !important;
}
.notification-details {
  padding: 20px;
  font-size: 14px;
  color: #333;
  line-height: 1.6;
}

.notification-title {
  font-size: 20px;
  font-weight: bold;
  color: #003366;
  margin-bottom: 15px;
}

.notification-details p {
  margin: 10px 0;
}

.notification-details p strong {
  color: #555;
}


</style>
