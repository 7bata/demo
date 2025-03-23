<script setup>
  import {get} from "@/net/index.js";
  import {ElMessage} from "element-plus";
  import router from "@/router/index.js";

  const logout = () => {
    get('api/auth/logout', (message) => {
      ElMessage.success(message);
      router.push('/');
    })
  }
</script>

<template>
  <div class="index-container">
    <div class="header">
      <div class="welcome-text">欢迎来到Osmosis管理后台测试页面</div>
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
        <el-menu-item index="/index/student/course-hours">
          <el-icon><Calendar /></el-icon>
          <span>课时统计</span>
        </el-menu-item>
        <el-menu-item index="/index/student/course-schedule">
          <el-icon><Calendar /></el-icon>
          <span>课程表</span>
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