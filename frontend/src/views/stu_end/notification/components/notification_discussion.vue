<template>
  <div>
    <!-- 通知列表显示 -->
    <el-table :data="notificationList" style="width: 100%">
      <el-table-column prop="courseId" label="课程ID" width="180"/>
      <el-table-column prop="userName" label="发送者" width="180"/>
      <el-table-column prop="content" label="标题" width="200"/>
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
        :visible.sync="drawerVisible"
        direction="rtl"
        :before-close="handleClose">
      <div v-if="selectedNotification">
        <p><strong>标题:</strong> {{ selectedNotification.content }}</p>
        <p><strong>发送者:</strong> {{ selectedNotification.userName }}</p>
        <p><strong>时间:</strong> {{ selectedNotification.createdAt }}</p>
        <p><strong>通知类型:</strong> {{ selectedNotification.type }}</p>
        <p><strong>内容:</strong> {{ selectedNotification.content }}</p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="drawerVisible = false">关闭</el-button>
      </span>
    </el-drawer>

    <!-- 分页 -->
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
import {ref} from 'vue';
import {CircleCheck, CircleCheckFilled, Star, StarFilled} from "@element-plus/icons-vue";

const userId = useUserStore()?.id;
const notificationType = "讨论区通知";

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

// 详情页面
const drawerVisible = ref(false);
const selectedNotification = ref(null);

const showDetails = (notification) => {
  selectedNotification.value = notification;
  drawerVisible.value = true;
};

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
</style>
