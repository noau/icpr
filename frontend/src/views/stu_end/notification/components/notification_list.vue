<template>
  <div>
    <!-- 状态选择与筛选 -->
    <div class="filter-bar">
      <div class="status_select">
        <button
            :class="['status_btn', { active: filterStatus === 'all' }]"
            @click="updateFilterStatus('all')"
        >所有</button>
        <button
            :class="['status_btn', { active: filterStatus === 'unread' }]"
            @click="updateFilterStatus('unread')"
        >未读</button>
      </div>
      <div class="search-bar">
        <el-input
            placeholder="搜索"
            :value="localSearchText"
            @input="updateLocalSearchText"
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
      <el-table-column prop="courseId" label="课程ID" width="180"/>
      <el-table-column prop="userName" label="发送者" width="180"/>
      <el-table-column prop="content" label="标题" width="200"/>
      <el-table-column prop="createdAt" label="时间" width="200"/>
      <el-table-column prop="type" label="通知类型" width="200"/>
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

    <el-dialog :visible.sync="dialogVisible" title="通知详情">
      <div v-if="selectedNotification">
        <p><strong>标题:</strong> {{ selectedNotification.content }}</p>
        <p><strong>发送者:</strong> {{ selectedNotification.userName }}</p>
        <p><strong>时间:</strong> {{ selectedNotification.createdAt }}</p>
        <p><strong>通知类型:</strong> {{ selectedNotification.type }}</p>
        <p><strong>内容:</strong> {{ selectedNotification.content }}</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 分页 -->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="notificationsLength"
        class="pagination"
    />
  </div>
</template>


<script lang="ts" setup>
import {
  updateCollectionNotification,
  notificationsGet,
  updateReadNotification,
  deleteSignalNotification
} from '@/api/notification.js';
import {useUserStore} from '@/stores/user.js';
import {ref, computed} from 'vue';
import {CircleCheck, CircleCheckFilled, Star, StarFilled} from "@element-plus/icons-vue";

const userId = useUserStore()?.id;

const notificationList = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const notificationsLength = ref(0);
const filterStatus = ref('all'); // 状态选择
const localSearchText = ref(''); // 本地搜索文本
const sortOption = ref('date'); // 排序选项

const dialogVisible = ref(false);
const selectedNotification = ref(null);

const init = async () => {
  try {
    let res = await notificationsGet({id: userId});
    notificationList.value = res.notifications;
    notificationsLength.value = res.notifications.length;
  } catch (error) {
    console.error("获取通知列表时出错:", error);
  }
};

const filteredNotifications = computed(() => {
  let notifications = notificationList.value;

  // 根据筛选状态过滤通知
  if (filterStatus.value === 'unread') {
    notifications = notifications.filter(notification => !notification.isRead);
  }

  // 根据搜索文本过滤通知
  if (localSearchText.value) {
    notifications = notifications.filter(notification =>
        notification.content.includes(localSearchText.value)
    );
  }

  // 根据排序选项排序通知
  if (sortOption.value === 'date') {
    notifications.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
  } else if (sortOption.value === 'title') {
    notifications.sort((a, b) => a.content.localeCompare(b.content));
  }

  return notifications;
});

// 更新筛选状态
const updateFilterStatus = (status) => {
  filterStatus.value = status;
};

// 更新本地搜索文本
const updateLocalSearchText = (value) => {
  localSearchText.value = value;
};

// 提交搜索
const submitSearch = () => {
  // 同步搜索文本到父组件
};

// 更新排序选项
const updateSortOption = (value) => {
  sortOption.value = value;
};

// 切换收藏状态
const toggleStar = async (notification) => {
  notification.isStar = !notification.isStar;
  notification.isStar = notification.isStar ? 1 : 0;
  try {
    await updateCollectionNotification({...notification});
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

// 删除通知
const deleteNotification = async (notificationId) => {
  try {
    await deleteSignalNotification(notificationId);
    notificationList.value = notificationList.value.filter(notification => notification.id !== notificationId);
    notificationsLength.value -= 1;
  } catch (error) {
    console.error("删除通知时出错:", error);
  }
};

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size;
  init(); // 重新加载通知列表
};

const handleCurrentChange = (page) => {
  currentPage.value = page;
  init(); // 重新加载通知列表
};

const showDetails = (notification) => {
  selectedNotification.value = notification;
  dialogVisible.value = true;
};

init();
</script>

<style scoped>
.filter-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.status_select {
  display: flex;
}

.status_btn {
  padding: 8px 20px;
  cursor: pointer;
  background-color: #f5f7fa;
  color: #909399;
  transition: background-color 0.3s ease, color 0.3s ease;
  border: none;
}

.status_btn:hover {
  background-color: #e0e6ed;
}

.status_btn.active {
  background-color: #07395f;
  color: white;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 20px;
}

.search-input {
  flex: 1;
}

.sort-select {
  width: 150px;
}

.pagination {
  margin-top: 10px;
  text-align: center;
}
.el-icon.active {
  color: #de8f0f !important;
}
.el-icon {
  color: inherit; /* 继承父元素颜色，防止被覆盖 */
}

.el-icon[style*="color: green"] {
  color: green !important; /* 确保已读状态的颜色为绿色 */
}
.details-button {
  background-color: #003366;
  color: white;
  border-color: #003366;
}

.details-button:hover {
  background-color: #002244;
}
</style>
