<template>
  <div class="common-layout">
    <el-container>
      <!-- Header 占满整个页面宽度 -->
      <el-header>
        <icpr_header />
      </el-header>
      
      <!-- Main 布局 -->
      <el-main>
        <!-- 使用 el-row 布局 search_bar, course_list 和 reminder_card -->
        <el-row :gutter="20">
          <!-- 左侧 search_bar 和 course_list，占 18/24 -->
          <el-col :span="18">
            <div class="container">
              <div class="search-bar-wrapper">
                <search_bar @search="handleSearch" />
              </div>
              <course_list :courses="filteredCourses" />
            </div>
          </el-col>
          
          <!-- 右侧 reminder_card，占 6/24 -->
          <el-col :span="6">
            <reminder_card :reminders="reminders" @remove="removeReminder" />
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import icpr_header from '@/components/icpr_header.vue';
import search_bar from './components/search_bar.vue';
import course_list from './components/course_list.vue';
import reminder_card from './components/reminder_card.vue'

export default {
  components: {
    icpr_header,
    search_bar,
    course_list,
    reminder_card,
  },
  data() {
    return {
      courses: [
        { name: '课程1', courseId: '001', classId: 'A01' },
        { name: '课程2', courseId: '002', classId: 'A02' },
        { name: '课程3', courseId: '003', classId: 'A03' },
        { name: '课程4', courseId: '004', classId: 'A03' },
        { name: '课程5', courseId: '005', classId: 'A03' },
        { name: '课程6', courseId: '006', classId: 'A03' },
        { name: '课程7', courseId: '007', classId: 'A03' },
        { name: '课程8', courseId: '008', classId: 'A03' },
        { name: '课程9', courseId: '009', classId: 'A03' },
        { name: '课程10', courseId: '010', classId: 'A03' },
        { name: '课程11', courseId: '011', classId: 'A03' },
        { name: '课程12', courseId: '012', classId: 'A03' },
      ],
      searchQuery: '',
      reminders: [
        { type: '作业', title: '作业标题', deadline: '2024-09-15' },
        { type: '通知', title: '通知标题', content: '通知内容通知内容通知内容通知内容通知内容' },
        // 更多提醒...
      ],
    };
  },
  computed: {
    filteredCourses() {
      if (this.searchQuery) {
        return this.courses.filter(course =>
          course.name.includes(this.searchQuery)
        );
      }
      return this.courses;
    },
  },
  methods: {
    handleSearch(query) {
      this.searchQuery = query;
    },
    removeReminder(index) {
      this.reminders.splice(index, 1);
    },
  },
};
</script>

<style>
.common-layout {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  /* padding: 20px;  */
}

.app {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 确保应用占满视口高度 */
}

.container {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px; /* 设置组件之间的间距 */
}

.search-bar-wrapper {
  margin-top: 20px; /* 下移 search_bar */
}

.wide-component {
  width: 100%; /* 使这些组件变宽 */
}

.narrow-component {
  width: 300px; 
}


.el-header {
  /* background-color: #f5f5f5;  */
  padding: 20px;
}

.el-main {
  padding: 20px;
}

.el-col {
  background-color: #fff; /* 设置背景色 */
  padding: 20px;
  
}
</style>
