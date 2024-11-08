import { defineStore } from 'pinia';

export const useCourseStore = defineStore('course', {
  state: () => ({
    name: 'Your Course Name',
    courseId: null  // 用于存储当前课程的 ID
  }),
  getters: {
    name: (state) => state.name,
    getCourseId: (state) => state.courseId
  },
  actions: {
    setCourseId(id) {
      this.courseId = id;  // 设置当前课程 ID
    },
    clearCourseId() {
      this.courseId = null;  // 清除课程 ID
    }
  }
});
