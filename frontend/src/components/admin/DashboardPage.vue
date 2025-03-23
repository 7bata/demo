<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

onMounted(() => {
  ElMessage.success('欢迎来到管理员系统');
});
</script>

<template>
  <div class="dashboard-container">
    <h1>管理员控制台</h1>
    <div class="dashboard-content">
      <el-card class="dashboard-card">
        <template #header>
          <div class="card-header">
            <span>系统概览</span>
          </div>
        </template>
        <div class="card-content">
          <div class="stat-item">
            <div class="stat-label">学生总数</div>
            <div class="stat-value">120</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">教师总数</div>
            <div class="stat-value">15</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">课程总数</div>
            <div class="stat-value">25</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="dashboard-card">
        <template #header>
          <div class="card-header">
            <span>系统通知</span>
          </div>
        </template>
        <div class="card-content">
          <p>暂无系统通知</p>
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
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
}

.dashboard-card {
  width: 100%;
  max-width: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  min-height: 100px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.stat-value {
  font-weight: bold;
  font-size: 18px;
  color: #409EFF;
}
</style>