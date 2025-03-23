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
      
      // 如果没有特定角色要求或是管理员，则允许访问
      if (!requiredRole || userRole === 'admin') {
        next()
      } 
      // 如果是教师且访问的是教师或学生路由，允许访问
      else if (userRole === 'teacher' && (requiredRole === 'teacher' || requiredRole === 'student')) {
        next()
      }
      // 如果是学生且访问的是学生路由，允许访问
      else if (userRole === 'student' && requiredRole === 'student') {
        next()
      }
      // 其他情况，重定向到对应角色的默认页面
      else {
        switch (userRole) {
          case 'admin':
            next({ path: '/index/admin/dashboard' })
            break
          case 'teacher':
            next({ path: '/index/teacher/dashboard' })
            break
          case 'student':
            next({ path: '/index/student/course-hours' })
            break
          default:
            next({ path: '/' })
        }
      }
    }
  } else {
    // 不需要认证的路由直接放行
    next()
  }
})

export default router
