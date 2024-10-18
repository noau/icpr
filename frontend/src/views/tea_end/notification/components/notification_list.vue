<template>
    <div>
 <!-- 通知列表显示 -->
 <el-table :data="paginatedNotifications" style="width: 100%">
      <el-table-column prop="course" label="课程" width="180" />
      <el-table-column prop="sender" label="发送者" width="180" />
      <el-table-column prop="title" label="标题" width="200" />
      <el-table-column label="收藏" width="100">
        <template #default="scope">
            <el-icon 
            :class="{ 'active': scope.row.star }" 
            @click="toggleStar(scope.row)">
            <template v-if="scope.row.star">
                <StarFilled />
            </template>
            <template v-else>
                <Star />
            </template>
            </el-icon>
        </template>
        </el-table-column>
      <el-table-column label="已完成" width="100">
        <template #default="scope">
          <el-icon 
            :class="{ 'active': scope.row.completed }" 
            @click="toggleCompleted(scope.row)">
            <CircleCheck />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="时间" width="180" />
    </el-table>
  
      <!-- 分页 -->
      <el-pagination 
        @size-change="handleSizeChange" 
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="totalNotifications"
        class="pagination"
      />
    </div>
  </template>
  
  <script>
  export default {
    props: {
    filterStatus: {
      type: String,
      required: true
    },
    searchText: {
      type: String,
      default: ''
    },
    sortOption: {
      type: String,
      default: 'date'
    }
  },
    data() {
      return {
        currentPage: 1,  // 当前页码
        pageSize: 10,    // 每页显示的记录数
        notifications: [
          { course: '操作系统', sender: '王建国', title: '期末考试安排', time: '2024-09-22 15:00', read: false, star: false, completed: false },
          { course: '计算机网络', sender: '陈芳', title: '作业提交截止提醒', time: '2024-09-22 16:30', read: true, star: true, completed: true },
          { course: '人工智能基础', sender: '赵伟', title: '实验报告提交通知', time: '2024-09-23 09:00', read: false, star: true, completed: false },
          { course: '数据挖掘', sender: '刘小东', title: '课程内容更新', time: '2024-09-23 11:00', read: true, star: false, completed: true },
          { course: '云计算与大数据', sender: '张翠兰', title: '课后练习发布', time: '2024-09-23 14:00', read: false, star: false, completed: false },
          { course: '编译原理', sender: '赵强', title: '实验课注意事项', time: '2024-09-24 08:00', read: true, star: false, completed: true },
          { course: '数据库系统概论', sender: '王晓梅', title: '课件更新提醒', time: '2024-09-24 10:30', read: false, star: true, completed: false },
          { course: '软件工程', sender: '吴峰', title: '团队作业提交通知', time: '2024-09-24 13:00', read: true, star: true, completed: true },
          { course: '计算机组成原理', sender: '孙悦', title: '期中考试复习范围', time: '2024-09-25 09:30', read: false, star: false, completed: false },
          { course: '操作系统', sender: '李云', title: '项目报告说明', time: '2024-09-25 10:00', read: true, star: true, completed: true },
          { course: '数据结构', sender: '陈丽', title: '第三次作业发布', time: '2024-09-25 11:30', read: false, star: false, completed: false },
          { course: '面向对象程序设计', sender: '张明', title: '实验课材料准备', time: '2024-09-26 08:30', read: true, star: true, completed: true },
          { course: '离散数学', sender: '周杰', title: '课程项目提交提醒', time: '2024-09-26 09:00', read: false, star: true, completed: false },
          { course: '概率论与数理统计', sender: '宋华', title: '期末复习资料上传', time: '2024-09-26 10:30', read: true, star: false, completed: true },
          { course: '信息安全', sender: '刘颖', title: '期末考试形式说明', time: '2024-09-27 08:00', read: false, star: false, completed: false },
          { course: '高等数学', sender: '马亮', title: '补课通知', time: '2024-09-27 14:00', read: true, star: true, completed: true },
          { course: '计算机图形学', sender: '王海', title: '实验课准备材料', time: '2024-09-28 09:00', read: false, star: true, completed: false },
          { course: '机器学习', sender: '孙宏', title: '课程问卷调查', time: '2024-09-28 13:30', read: true, star: false, completed: true },
          { course: '信息检索', sender: '杨军', title: '课后作业说明', time: '2024-09-28 16:00', read: false, star: false, completed: false },
          { course: '人机交互', sender: '陈超', title: '项目演示安排', time: '2024-09-29 10:00', read: true, star: true, completed: true },
        ]
      };
    },
    computed: {

    filteredNotifications() {
      let notifications = this.notifications;

      console.log('Current filterStatus:', this.filterStatus);
      console.log('Notifications before filtering:', notifications);


      // 筛选已读、未读、收藏或已完成
    if (this.filterStatus === 'unread') {
      notifications = notifications.filter(notification => !notification.read);
    } else if (this.filterStatus === 'starred') {
      notifications = notifications.filter(notification => notification.star);
    } else if (this.filterStatus === 'completed') {
      notifications = notifications.filter(notification => notification.completed);
    }else if (this.filterStatus === 'all') {
      // 显示所有通知，实际上可以不做任何处理
    }

       // 模糊搜索
       if (this.searchText) {
        notifications = notifications.filter(notification => 
          notification.title.includes(this.searchText) || 
          notification.course.includes(this.searchText)
        );
      }

      // 根据选定的排序选项排序
      if (this.sortOption === 'date') {
        notifications.sort((a, b) => new Date(b.time) - new Date(a.time));
      } else if (this.sortOption === 'title') {
        notifications.sort((a, b) => a.title.localeCompare(b.title));
      }

      return notifications;
    },
    paginatedNotifications() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.filteredNotifications.slice(start, start + this.pageSize);
    },
    totalNotifications() {
      return this.filteredNotifications.length; // 返回筛选后的总数
    }
      
    },
    methods: {
      toggleStar(notification) {
        console.log('Star toggled for:', notification);
        notification.star = !notification.star;
    },
      toggleCompleted(notification) {
        console.log('Completed toggled for:', notification);
        notification.completed = !notification.completed;
    },
      handleSizeChange(size) {
        this.pageSize = size;
      },
      handleCurrentChange(page) {
        this.currentPage = page;
      }
    }
  };
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
  margin-right: 10px; /* 图标之间的间距 */
}


.el-icon.active {
  color:#de8f0f!important;
}

.el-icon.active i.el-icon-check-circle-outline {
  color: green; /* 圆圈完成状态的颜色 */
}

.pagination {
  margin-top: 10px;
  text-align: center;
} 
</style>