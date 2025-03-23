import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import 'element-plus/dist/index.css'
import axios from "axios";
import setupMockAdapter from './mock/mockAdapter'

const app = createApp(App)

axios.defaults.baseURL = "http://localhost:8080"//默认地址

// 设置mock拦截器
setupMockAdapter()

app.use(createPinia())
app.use(router)

app.mount('#app')
