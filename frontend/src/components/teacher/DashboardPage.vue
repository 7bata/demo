<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

onMounted(() => {
  ElMessage.success('欢迎来到教师管理系统');
});
</script>

<template>
  <div class="dashboard-container">
    <h1>教师工作台</h1>
    <div class="dashboard-content">
      <el-card class="dashboard-card">
        <template #header>
          <div class="card-header">
            <span>待办事项</span>
          </div>
        </template>
        <div class="card-content">
          <p>暂无待办事项</p>
        </div>
      </el-card>
      
      <el-card class="dashboard-card">
        <template #header>
          <div class="card-header">
            <span>最近课程</span>
          </div>
        </template>
        <div class="card-content">
          <p>暂无最近课程</p>
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
</style>