import { createRouter, createWebHistory } from 'vue-router'
import { studentRoutes } from './student'
import { teacherRoutes } from './teacher'
import { adminRoutes } from './admin'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      //welcome page
      {
          path: '/',
          name: 'welcome',
          component: () => import('@/views/WelcomeView.vue'),
          children: [
          {
              path: '',
              name: 'welcome-login',
              component: () => import('@/components/welcome/LoginPage.vue')
          },
          {
              path: 'forget',
              name: 'welcome-forget',
              component: () => import('@/components/welcome/ForgetPage.vue')
          }
          ]
      },
      //index page
      {
          path: '/index',
          name: 'index',
          component: () => import('@/views/IndexView.vue'),
          children: [
              {
                  path: '',
                  redirect: '/index/student/course-hours'
              },
              {
                  path: 'student',
                  children: [
                      {
                          path: 'course-hours',
                          name: 'student-course-hours',
                          component: () => import('@/components/student/CourseHoursPage.vue')
                      },
                      {
                          path: 'course-schedule',
                          name: 'student-course-schedule',
                          component: () => import('@/components/student/CourseSchedulePage.vue')
                      }
                  ]
              },
              {
                  path: 'teacher',
                  children: [
                      {
                          path: 'dashboard',
                          name: 'teacher-dashboard',
                          component: () => import('@/components/teacher/DashboardPage.vue')
                      },
                      {
                          path: 'course-management',
                          name: 'teacher-course-management',
                          component: () => import('@/components/teacher/CourseManagementPage.vue')
                      }
                  ]
              },
              {
                  path: 'admin',
                  children: [
                      {
                          path: 'dashboard',
                          name: 'admin-dashboard',
                          component: () => import('@/components/admin/DashboardPage.vue')
                      },
                      {
                          path: 'user-management',
                          name: 'admin-user-management',
                          component: () => import('@/components/admin/UserManagementPage.vue')
                      }
                  ]
              }
          ]
      }

      ]

})

export default router
