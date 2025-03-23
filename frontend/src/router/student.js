import { createRouter } from 'vue-router'

export const studentRoutes = [
    {
        path: '/student',
        name: 'student',
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
    }
]