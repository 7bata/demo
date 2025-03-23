<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { ArrowRight } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const upcomingCourses = ref([
  { id: 1, name: '高等数学', time: '周一 08:00-09:40', teacher: '张教授', location: '教学楼A-101' },
  { id: 2, name: '大学英语', time: '周二 10:00-11:40', teacher: '李教授', location: '教学楼B-203' }
]);

const recentAnnouncements = ref([
  { id: 1, title: '关于期中考试安排的通知', date: '2023-10-15' },
  { id: 2, title: '图书馆开放时间调整', date: '2023-10-12' }
]);

onMounted(() => {
  // 检查是否是首次登录
  const justLoggedIn = sessionStorage.getItem('justLoggedIn');
  if (justLoggedIn === 'true') {
    ElMessage.success('欢迎来到学生系统');
    // 清除登录标记，防止再次显示
    sessionStorage.removeItem('justLoggedIn');
  }
});

const goToCourseSchedule = () => {
  router.push('/index/student/course-schedule');
};

const goToCourseHours = () => {
  router.push('/index/student/course-hours');
};
</script>

<template>
  <div class="dashboard-container">
    <h1>学生主页</h1>
    
    <div class="dashboard-content">
      <el-card class="dashboard-card">
        <template #header>
          <div class="card-header">
            <span>我的课程</span>
            <el-button text type="primary" @click="goToCourseSchedule">
              查看全部课程
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </template>
        <div class="upcoming-courses">
          <div v-if="upcomingCourses.length > 0">
            <div v-for="course in upcomingCourses" :key="course.id" class="course-item">
              <div class="course-name">{{ course.name }}</div>
              <div class="course-details">
                <span><i class="el-icon-time"></i> {{ course.time }}</span>
                <span><i class="el-icon-user"></i> {{ course.teacher }}</span>
                <span><i class="el-icon-location"></i> {{ course.location }}</span>
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无课程信息"></el-empty>
        </div>
      </el-card>
      
      <el-card class="dashboard-card">
        <template #header>
          <div class="card-header">
            <span>课时统计</span>
            <el-button text type="primary" @click="goToCourseHours">
              查看详情
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </template>
        <div class="card-content">
          <div class="stats-summary">
            <div class="stat-item">
              <div class="stat-value">42</div>
              <div class="stat-label">已完成课时</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">18</div>
              <div class="stat-label">剩余课时</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">5</div>
              <div class="stat-label">本周课程</div>
            </div>
          </div>
        </div>
      </el-card>
      
      <el-card class="dashboard-card">
        <template #header>
          <div class="card-header">
            <span>最新通知</span>
          </div>
        </template>
        <div class="announcements">
          <div v-if="recentAnnouncements.length > 0">
            <div v-for="announcement in recentAnnouncements" :key="announcement.id" class="announcement-item">
              <div class="announcement-title">{{ announcement.title }}</div>
              <div class="announcement-date">{{ announcement.date }}</div>
            </div>
          </div>
          <el-empty v-else description="暂无通知"></el-empty>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-content {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.dashboard-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upcoming-courses, .announcements {
  min-height: 100px;
}

.course-item {
  padding: 10px;
  border-bottom: 1px solid #ebeef5;
}

.course-item:last-child {
  border-bottom: none;
}

.course-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.course-details {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #606266;
  flex-wrap: wrap;
}

.course-details span {
  margin-right: 10px;
}

.announcement-item {
  padding: 10px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  flex: 1;
}

.announcement-date {
  color: #909399;
  font-size: 14px;
}

.stats-summary {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-top: 5px;
}
</style> 