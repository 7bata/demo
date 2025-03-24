<script setup>
  import {get} from "@/net/index.js";
  import {ElMessage} from "element-plus";
  import router from "@/router/index.js";
  import { ref, onMounted, computed } from 'vue';
  import { 
    Calendar, 
    HomeFilled, 
    User, 
    Setting, 
    Document
  } from '@element-plus/icons-vue';

  const userRole = ref('');
  
  onMounted(() => {
    // 获取用户角色
    userRole.value = localStorage.getItem('userRole') || '';
  });
  
  // 根据角色计算显示的菜单项
  const menuItems = computed(() => {
    if (userRole.value === 'admin') {
      return [
        { path: '/index/admin/dashboard', icon: HomeFilled, label: '首页' },
        { path: '/index/admin/user-management', icon: User, label: '用户管理' },
        { path: '/index/admin/course-hours-management', icon: Calendar, label: '课时管理' }
      ];
    } else if (userRole.value === 'teacher') {
      return [
        { path: '/index/teacher/dashboard', icon: HomeFilled, label: '教师工作台' },
        { path: '/index/teacher/course-management', icon: Document, label: '课程管理' },
        { path: '/index/teacher/course-schedule', icon: Calendar, label: '课程表' }
      ];
    } else {
      // 默认为学生角色
      return [
        { path: '/index/student/dashboard', icon: HomeFilled, label: '学生主页' },
        { path: '/index/student/course-hours', icon: Calendar, label: '课时统计' },
        { path: '/index/student/course-schedule', icon: Calendar, label: '课程表' }
      ];
    }
  });

  const logout = () => {
    get('api/auth/logout', (message) => {
      ElMessage.success(message);
      // 清除存储的角色信息
      localStorage.removeItem('userRole');
      // 清除登录标记，确保下次登录会显示欢迎信息
      sessionStorage.removeItem('justLoggedIn');
      router.push('/');
    })
  }
</script>

<template>
  <div class="index-container">
    <div class="header">
      <div class="welcome-text">欢迎来到Osmosis管理后台</div>
      <div class="logout-btn">
        <el-button @click="logout()" type="danger" plain>退出登录</el-button>
      </div>
    </div>
    
    <!-- 添加导航菜单 -->
    <div class="nav-menu">
      <el-menu 
        mode="horizontal" 
        :router="true" 
        :default-active="$route.path">
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
    </div>
    
    <!-- 添加router-view以显示子路由内容 -->
    <div class="content">
      <router-view/>
    </div>
  </div>
</template>

<style scoped>
.index-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.welcome-text {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.nav-menu {
  border-bottom: 1px solid #e4e7ed;
}

.content {
  flex: 1;
  overflow-y: auto;
  padding: 0;
}
</style>