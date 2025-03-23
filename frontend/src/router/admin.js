import { createRouter } from 'vue-router'

export const adminRoutes = [
    {
        path: '/admin',
        name: 'admin',
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