import { createRouter } from 'vue-router'

export const teacherRoutes = [
    {
        path: '/teacher',
        name: 'teacher',
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
    }
]