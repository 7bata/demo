import { createRouter, createWebHistory } from 'vue-router'


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
          }
          ]
      },
      //index page
      {
          path: '/index',
          name: 'index',
          component: () => import('@/views/IndexView.vue')
      }

      ]

})

export default router
