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
                  redirect: to => {
                      const userRole = localStorage.getItem('userRole')
                      switch(userRole) {
                          case 'admin':
                              return '/index/admin/dashboard'
                          case 'teacher':
                              return '/index/teacher/dashboard'
                          case 'student':
                              return '/index/student/dashboard'
                          default:
                              return '/index/student/dashboard'
                      }
                  }
              },
              {
                  path: 'student',
                  children: [
                      {
                          path: 'dashboard',
                          name: 'student-dashboard',
                          component: () => import('@/components/student/DashboardPage.vue'),
                          meta: { requiresAuth: true, role: 'student' }
                      },
                      {
                          path: 'course-hours',
                          name: 'student-course-hours',
                          component: () => import('@/components/student/CourseHoursPage.vue'),
                          meta: { requiresAuth: true, role: 'student' }
                      },
                      {
                          path: 'course-schedule',
                          name: 'student-course-schedule',
                          component: () => import('@/components/student/CourseSchedulePage.vue'),
                          meta: { requiresAuth: true, role: 'student' }
                      }
                  ]
              },
              {
                  path: 'teacher',
                  children: [
                      {
                          path: 'dashboard',
                          name: 'teacher-dashboard',
                          component: () => import('@/components/teacher/DashboardPage.vue'),
                          meta: { requiresAuth: true, role: 'teacher' }
                      },
                      {
                          path: 'course-management',
                          name: 'teacher-course-management',
                          component: () => import('@/components/teacher/CourseManagementPage.vue'),
                          meta: { requiresAuth: true, role: 'teacher' }
                      },
                      {
                          path: 'course-schedule',
                          name: 'teacher-course-schedule',
                          component: () => import('@/components/teacher/CourseSchedulePage.vue'),
                          meta: { requiresAuth: true, role: 'teacher' }
                      },
                      {
                          path: 'student-course-detail',
                          name: 'student-course-detail',
                          component: () => import('@/components/teacher/StudentCourseDetailPage.vue'),
                          meta: { requiresAuth: true, role: 'teacher' }
                      }
                  ]
              },
              {
                  path: 'admin',
                  children: [
                      {
                          path: 'dashboard',
                          name: 'admin-dashboard',
                          component: () => import('@/components/admin/DashboardPage.vue'),
                          meta: { requiresAuth: true, role: 'admin' }
                      },
                      {
                          path: 'user-management',
                          name: 'admin-user-management',
                          component: () => import('@/components/admin/UserManagementPage.vue'),
                          meta: { requiresAuth: true, role: 'admin' }
                      }
                  ]
              }
          ]
      }
  ]
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 检查路由是否需要认证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 从localStorage获取用户角色
    const userRole = localStorage.getItem('userRole')
    
    // 如果没有角色信息，重定向到登录页
    if (!userRole) {
      next({ path: '/' })
    } else {
      // 检查路由角色需求
      const requiredRole = to.matched.find(record => record.meta.role)?.meta.role
      
      // 严格的角色控制，确保每个角色只能访问自己的路由
      if (requiredRole) {
        if (
          (requiredRole === 'admin' && userRole === 'admin') ||
          (requiredRole === 'teacher' && userRole === 'teacher') ||
          (requiredRole === 'student' && userRole === 'student')
        ) {
          next()
        } else {
          // 如果角色不匹配，重定向到对应角色的默认页面
          switch (userRole) {
            case 'admin':
              next({ path: '/index/admin/dashboard' })
              break
            case 'teacher':
              next({ path: '/index/teacher/dashboard' })
              break
            case 'student':
              next({ path: '/index/student/dashboard' })
              break
            default:
              next({ path: '/' })
          }
        }
      } else {
        next()
      }
    }
  } else {
    // 不需要认证的路由直接放行
    next()
  }
})

export default router
