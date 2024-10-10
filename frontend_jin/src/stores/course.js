import { defineStore } from 'pinia'

export const useCourseStore = defineStore('course', {
  state: () => ({
    name: 'Your Course Name'
  }),
  getters: {
    name: (state) => state.name
  }
})