<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';

const router = useRouter();
const courseList = ref([]);
const loading = ref(true);

onMounted(() => {
  // 模拟获取课程数据
  setTimeout(() => {
    courseList.value = [
      { id: 1, name: '高等数学', studentName: '张三', status: '进行中', totalHours: 40, completedHours: 28 },
      { id: 2, name: '大学英语', studentName: '李四', status: '进行中', totalHours: 30, completedHours: 15 },
      { id: 3, name: '物理学', studentName: '王五', status: '已结束', totalHours: 20, completedHours: 20 },
      { id: 4, name: '计算机编程', studentName: '赵六', status: '进行中', totalHours: 60, completedHours: 42 }
    ];
    loading.value = false;
  }, 500);
});

// 查看课时信息
const viewCourseHours = (course) => {
  router.push({
    path: '/index/teacher/student-course-detail',
    query: { courseId: course.id, studentName: course.studentName }
  });
};

// 生成课时报告
const generateReport = (course) => {
  ElMessageBox.confirm(
    `确定要为${course.studentName}的${course.name}课程生成并导出课时报告吗？`,
    '生成并导出课时报告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }
  )
    .then(() => {
      ElMessage.success(`已为${course.studentName}的${course.name}课程生成并导出课时报告`);
    })
    .catch(() => {
      // 取消生成
    });
};
</script>

<template>
  <div class="course-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span class="title">课程管理</span>
          <div class="controls">
            <el-button type="primary" @click="router.push('/index/teacher/course-schedule')">查看课程表</el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="courseList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="课程名称" />
        <el-table-column prop="studentName" label="学生姓名" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === '进行中' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="课时进度">
          <template #default="scope">
            <div>{{ scope.row.completedHours }}/{{ scope.row.totalHours }}</div>
            <el-progress 
              :percentage="Math.round((scope.row.completedHours / scope.row.totalHours) * 100)" 
              :status="scope.row.completedHours >= scope.row.totalHours ? 'success' : ''"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="viewCourseHours(scope.row)"
            >
              查看课时信息
            </el-button>
            <el-button 
              size="small" 
              type="warning" 
              @click="generateReport(scope.row)"
            >
              生成并导出学习报告
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.course-management-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.controls {
  display: flex;
  gap: 10px;
}
</style>