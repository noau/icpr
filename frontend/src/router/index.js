import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 学生端路由
    {
      path: '/stu-end',
      component: () => import('@/views/stu_end/layout/index.vue'),
    },
    {
      path: '/stu-end/login',
      component: () => import('@/views/stu_end/login/index.vue')
    },

    {
      path: '/stu-end/notification',
      component: () => import('@/views/stu_end/notification/index.vue')
    },
    {
      path: '/notification',
      component: () => import('@/views/stu_end/notification/index.vue')
    },

    {
      path: '/stu-end/course', // 课程主页面，默认展示课程介绍
      component: () => import('@/views/stu_end/course/index.vue'),
      redirect: '/stu-end/course/base/course-introduction',
      children: [
        {
          path: '/stu-end/course/base', 
          meta: { breadcrumbName: '基础信息' },
          redirect: '/stu-end/course/base/course-introduction',
          children: [
            {
              path: '/stu-end/course/base/course-introduction', 
              component: () => import('@/views/stu_end/course/base/course_introduction.vue'),
              meta: { breadcrumbName: '课程介绍' }
            },
            {
              path: '/stu-end/course/base/teaching-outline',
              component: () => import('@/views/stu_end/course/base/teaching_outline.vue'),
              meta: { breadcrumbName: '教学大纲' }
            },
            {
              path: '/stu-end/course/base/teaching-calendar',
              component: () => import('@/views/stu_end/course/base/teaching_calendar.vue'),
              meta: { breadcrumbName: '教学日历' }
            },
            {
              path: '/stu-end/course/base/teacher-info',
              component: () => import('@/views/stu_end/course/base/teacher_info.vue'),
              meta: { breadcrumbName: '教师信息' }
            },
          ]
        },
        {
          path: '/stu-end/course/resource', 
          meta: { breadcrumbName: '课程资源' },
          redirect: '/stu-end/course/resource/slides',
          children: [
            {
              path: '/stu-end/course/resource/slides',
              component: () => import('@/views/stu_end/course/resource/slides.vue'),
              meta: { breadcrumbName: '课件' }
            },
            {
              path: '/stu-end/course/resource/old-exam',
              component: () => import('@/views/stu_end/course/resource/old_exam.vue'),
             meta: { breadcrumbName: '历年试题库' }
            },
            {
              path: '/stu-end/course/resource/exercise',
              component: () => import('@/views/stu_end/course/resource/exercise.vue'),
              meta: { breadcrumbName: '习题库' }
            },
          ]
        },
        {
          path: '/stu-end/course/examine', 
          meta: { breadcrumbName: '课程考核' },
          redirect: '/stu-end/course/examine/stu-homework',
          children: [
            {
              path: '/stu-end/course/examine/stu-homework',
              component: () => import('@/views/stu_end/course/examine/stu_homework.vue'),
              meta: { breadcrumbName: '作业'},
            },
            {
              path: '/stu-end/course/examine/homework-details/:id',
              component: () => import('@/views/stu_end/course/examine/homework_details.vue'),
              meta: { breadcrumbName: '作业详情' }
            },
          ]
        },
        {
          path: '/stu-end/course/discussion',
          component: () => import('@/views/stu_end/course/discussion/discussion_main/index.vue')
        },
        {
          path: '/stu-end/course/discussion/create-post',
          component: () => import('@/views/stu_end/course/discussion/discussion_create/index.vue')
        },
        {
          path: '/stu-end/course/discussion/post/:id',
          component: () => import('@/views/stu_end/course/discussion/discussion_detail/index.vue')
        },
      ]
    },

    // 教师端路由
    {
      path: '/tea-end',
      component: () => import('@/views/tea_end/layout/index.vue'),
    },
    {
      path: '/tea-end/login',
      component: () => import('@/views/tea_end/login/index.vue')
    },
    {
      path: '/tea-end/notification',
      component: () => import('@/views/tea_end/notification/index.vue')
    },
    {
      path: '/tea-end/course', // 课程主页面，默认展示课程介绍
      component: () => import('@/views/tea_end/course/index.vue'),
      redirect: '/tea-end/course/base/course-introduction',
      children: [
        {
          path: '/tea-end/course/base', 
          meta: { breadcrumbName: '基础信息' },
          redirect: '/tea-end/course/base/course-introduction',
          children: [
            {
              path: '/tea-end/course/base/course-introduction', 
              component: () => import('@/views/tea_end/course/base/course_introduction.vue'),
              meta: { breadcrumbName: '课程介绍' }
            },
            {
              path: '/tea-end/course/base/teaching-outline',
              component: () => import('@/views/tea_end/course/base/teaching_outline.vue'),
              meta: { breadcrumbName: '教学大纲' }
            },
            {
              path: '/tea-end/course/base/teaching-calendar',
              component: () => import('@/views/tea_end/course/base/teaching_calendar.vue'),
              meta: { breadcrumbName: '教学日历' }
            },
            // {
            //   path: '/tea-end/course/base/teacher-info',
            //   component: () => import('@/views/tea_end/course/base/teacher_info.vue'),
            //   meta: { breadcrumbName: '教师信息' }
            // },
            {
              path: '/tea-end/course/base/student-list',
              component: () => import('@/views/tea_end/course/base/student_list.vue'),
              meta: { breadcrumbName: '学生名单' }
            }
          ]
        },
        {
          path: '/tea-end/course/resource', 
          meta: { breadcrumbName: '课程资源' },
          redirect: '/tea-end/course/resource/slides',
          children: [
            {
              path: '/tea-end/course/resource/slides',
              component: () => import('@/views/tea_end/course/resource/slides.vue'),
              meta: { breadcrumbName: '课件' }
            },
            {
              path: '/tea-end/course/resource/old-exam',
              component: () => import('@/views/tea_end/course/resource/old_exam.vue'),
             meta: { breadcrumbName: '历年试题库' }
            },
            {
              path: '/tea-end/course/resource/exercise',
              component: () => import('@/views/tea_end/course/resource/exercise.vue'),
              meta: { breadcrumbName: '习题库' }
            },
          ]
        },
        {
          path: '/tea-end/course/examine', 
          meta: { breadcrumbName: '课程考核' },
          redirect: '/tea-end/course/examine/homework',
          children: [
            {
              path: '/tea-end/course/examine/homework',
              component: () => import('@/views/tea_end/course/examine/homework.vue'),
              meta: { breadcrumbName: '作业'},
            },
            {
              path: '/tea-end/course/examine/assign-homework',
              component: () => import('@/views/tea_end/course/examine/assign_homework.vue'),
              meta: { breadcrumbName: '布置作业' }
            },
            {
              path: '/tea-end/course/examine/grade-homework',
              component: () => import('@/views/tea_end/course/examine/grade_homework.vue'),
              meta: { breadcrumbName: '批阅作业' }
            },
            {
              path: '/tea-end/course/examine/homework-statistics',
              component: () => import('@/views/tea_end/course/examine/homework_statistics.vue'),
              meta: { breadcrumbName: '成绩统计' }
            },
            
          ]
        },
        {
          path: '/tea-end/course/discussion',
          component: () => import('@/views/tea_end/course/discussion/discussion_main/index.vue')
        },
        {
          path: '/tea-end/course/discussion/create-post',
          component: () => import('@/views/tea_end/course/discussion/discussion_create/index.vue')
        },
        {
          path: '/tea-end/course/discussion/post/:id',
          component: () => import('@/views/tea_end/course/discussion/discussion_detail/index.vue')
        },
      ]
    },
    {
      path: '/tea-end/course/examine/rating-page',
      component: () => import('@/views/tea_end/course/examine/rating_page.vue'),
      meta: { breadcrumbName: '评分页面' }
    }, 

    {
      path: '/adm-end', // 管理员主页面，默认展示学生信息管理
      component: () => import('@/views/adm_end/index.vue'),
      redirect: '/adm-end/stu',
      children: [
        {
          path: '/adm-end/stu',
          component: () => import('@/views/adm_end/components/stu_info.vue'),
          meta: { breadcrumbName: '学生信息' },
        },
        {
          path: '/adm-end/tea',
          component: () => import('@/views/adm_end/components/tea_info.vue'),
          meta: { breadcrumbName: '教师信息' },
        },
        {
          path: '/adm-end/cou',
          component: () => import('@/views/adm_end/components/cou_info.vue'),
          meta: { breadcrumbName: '课程信息' },
        },
        {
          path: '/adm-end/sc',
          component: () => import('@/views/adm_end/components/sc_info.vue'),
          meta: { breadcrumbName: '选课信息' },
        },
      ]
    },





  ]
})

export default router
