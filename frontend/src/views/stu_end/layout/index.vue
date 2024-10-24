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
import { userCourses } from "@/api/course.js";
import { useUserStore } from "@/stores/user.js";

const user = useUserStore();
if (user.id) {
  localStorage.setItem("id", user.id);

}
console.log(user.id);

let id = localStorage.getItem("id");

const courses = (await userCourses(id, user.token)).courses;
console.log(courses);

export default {
  components: {
    icpr_header,
    search_bar,
    course_list,
    reminder_card,
  },
  data() {
    return {
      courses: courses,
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
  height: 100vh;
  /* 确保应用占满视口高度 */
}

.container {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  /* 设置组件之间的间距 */
}

.search-bar-wrapper {
  margin-top: 20px;
  /* 下移 search_bar */
}

.wide-component {
  width: 100%;
  /* 使这些组件变宽 */
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
  background-color: #fff;
  /* 设置背景色 */
  padding: 20px;

}
</style>
