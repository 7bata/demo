<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { ArrowLeft, Document, View } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();
const courseId = route.query.courseId;
const courseName = route.query.courseName;
const teacherId = route.query.teacherId;
const teacherName = route.query.teacherName;

const loading = ref(true);
const courseStudents = ref([]);
const courseClasses = ref([]);
const currentClassDetail = ref(null);
const dialogVisible = ref(false);

// 获取课程学生数据
const fetchCourseStudents = () => {
  loading.value = true;
  
  // 模拟API请求
  setTimeout(() => {
    courseStudents.value = [
      {
        id: 1,
        name: '张三',
        totalHours: 40,
        completedHours: 28,
        remainingHours: 12
      },
      {
        id: 2,
        name: '李四',
        totalHours: 40,
        completedHours: 30,
        remainingHours: 10
      },
      {
        id: 3,
        name: '王五',
        totalHours: 30,
        completedHours: 20,
        remainingHours: 10
      },
      {
        id: 4,
        name: '赵六',
        totalHours: 50,
        completedHours: 35,
        remainingHours: 15
      }
    ];
    
    loading.value = false;
  }, 300);
};

// 获取课时记录
const fetchCourseClasses = () => {
  loading.value = true;
  
  // 模拟API请求
  setTimeout(() => {
    courseClasses.value = [
      {
        id: 1,
        date: '2023-10-15',
        startTime: '08:00',
        endTime: '09:40',
        studentName: '张三',
        studentId: 1,
        hours: 2,
        topic: '函数极限',
        status: 'completed',
        hasReport: true,
        report: {
          content: '本节课讲解了函数极限的基本概念和计算方法',
          progress: '学生掌握了基本的极限计算',
          homework: '习题册第三章1-10题',
          nextGoals: '下节课继续讲解函数连续性'
        }
      },
      {
        id: 2,
        date: '2023-10-16',
        startTime: '10:00',
        endTime: '11:40',
        studentName: '李四',
        studentId: 2,
        hours: 2,
        topic: '函数连续性',
        status: 'incomplete',
        hasReport: true,
        report: {
          content: '讲解了函数连续性的定义和判断方法',
          progress: '学生理解了连续函数的性质',
          homework: '习题册第三章11-20题',
          nextGoals: '下节课开始微分学的内容'
        }
      },
      {
        id: 3,
        date: '2023-10-18',
        startTime: '14:00',
        endTime: '15:40',
        studentName: '王五',
        studentId: 3,
        hours: 2,
        topic: '导数概念',
        status: 'absent',
        hasReport: false
      },
      {
        id: 4,
        date: '2023-10-20',
        startTime: '16:00',
        endTime: '17:40',
        studentName: '赵六',
        studentId: 4,
        hours: 2,
        topic: '导数应用',
        status: 'completed',
        hasReport: true,
        report: {
          content: '讲解了导数的实际应用',
          progress: '学生理解了导数在实际问题中的应用',
          homework: '习题册第四章1-15题',
          nextGoals: '下节课讲解微分中值定理'
        }
      }
    ];
    
    loading.value = false;
  }, 300);
};

// 查看课时详情
const viewClassDetail = (classItem) => {
  currentClassDetail.value = classItem;
  dialogVisible.value = true;
};

// 返回课时管理页面
const goBack = () => {
  router.push('/index/admin/course-hours-management');
};

// 获取状态标签类型
const getStatusType = (status) => {
  const types = {
    completed: 'success',
    incomplete: 'warning',
    absent: 'danger'
  };
  return types[status] || 'info';
};

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    completed: '已完成',
    incomplete: '未上完',
    absent: '缺席'
  };
  return texts[status] || '未知';
};

// 页面加载时获取数据
onMounted(() => {
  if (courseId) {
    fetchCourseStudents();
    fetchCourseClasses();
  } else {
    ElMessage.error('课程信息不存在');
    router.push('/index/admin/course-hours-management');
  }
});
</script>

<template>
  <div class="course-detail-container" v-loading="loading">
    <div class="page-header">
      <div class="back-button">
        <el-button @click="goBack" icon="arrow-left" class="back-btn">返回</el-button>
      </div>
      <div class="page-title">
        <h2>{{ courseName }} - {{ teacherName }}</h2>
      </div>
    </div>
    
    <!-- 学生列表 -->
    <el-card class="student-list">
      <template #header>
        <div class="card-header">
          <span>上课学生</span>
        </div>
      </template>
      
      <el-table :data="courseStudents" style="width: 100%" border>
        <el-table-column prop="name" label="学生姓名" width="180" />
        <el-table-column prop="totalHours" label="总课时" width="100" align="center" />
        <el-table-column prop="completedHours" label="已上课时" width="100" align="center" />
        <el-table-column prop="remainingHours" label="未上课时" width="100" align="center" />
        <el-table-column label="完成进度" min-width="180">
          <template #default="scope">
            <el-progress 
              :percentage="Math.round((scope.row.completedHours / scope.row.totalHours) * 100)"
              :format="format => `${scope.row.completedHours}/${scope.row.totalHours}`"
            />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 课时记录 -->
    <el-card class="class-records">
      <template #header>
        <div class="card-header">
          <span>课时记录</span>
        </div>
      </template>
      
      <el-table :data="courseClasses" style="width: 100%" border>
        <el-table-column prop="date" label="上课日期" width="120" />
        <el-table-column label="上课时间" width="160">
          <template #default="scope">
            {{ scope.row.startTime }} - {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="hours" label="课时" width="80" align="center" />
        <el-table-column prop="topic" label="课程主题" min-width="150" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="课时报告" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.hasReport ? 'success' : 'info'">
              {{ scope.row.hasReport ? '已完成' : '未完成' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              :disabled="!scope.row.hasReport"
              @click="viewClassDetail(scope.row)"
            >
              <el-icon><View /></el-icon>
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 课时详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="`${currentClassDetail?.studentName} 的课时报告`"
      width="60%"
    >
      <div v-if="currentClassDetail" class="class-detail">
        <div class="detail-header">
          <div class="detail-item">
            <span class="label">上课日期:</span>
            <span class="value">{{ currentClassDetail.date }}</span>
          </div>
          <div class="detail-item">
            <span class="label">上课时间:</span>
            <span class="value">{{ currentClassDetail.startTime }} - {{ currentClassDetail.endTime }}</span>
          </div>
          <div class="detail-item">
            <span class="label">课时:</span>
            <span class="value">{{ currentClassDetail.hours }}</span>
          </div>
          <div class="detail-item">
            <span class="label">状态:</span>
            <span class="value">
              <el-tag :type="getStatusType(currentClassDetail.status)">
                {{ getStatusText(currentClassDetail.status) }}
              </el-tag>
            </span>
          </div>
        </div>
        
        <el-divider />
        
        <div v-if="currentClassDetail.hasReport && currentClassDetail.report" class="report-content">
          <div class="report-item">
            <div class="report-label">课程主题</div>
            <div class="report-value">{{ currentClassDetail.topic }}</div>
          </div>
          
          <div class="report-item">
            <div class="report-label">课程内容</div>
            <div class="report-value">{{ currentClassDetail.report.content }}</div>
          </div>
          
          <div class="report-item">
            <div class="report-label">学习进度</div>
            <div class="report-value">{{ currentClassDetail.report.progress }}</div>
          </div>
          
          <div class="report-item">
            <div class="report-label">作业布置</div>
            <div class="report-value">{{ currentClassDetail.report.homework }}</div>
          </div>
          
          <div class="report-item">
            <div class="report-label">下次课目标</div>
            <div class="report-value">{{ currentClassDetail.report.nextGoals }}</div>
          </div>
        </div>
        
        <div v-else class="no-report">
          <el-empty description="未提交课时报告" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.course-detail-container {
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.back-button {
  margin-right: 20px;
}

.back-btn {
  width: 80px;
}

.page-title h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.card-header {
  font-weight: bold;
}

.student-list {
  margin-bottom: 20px;
}

.class-records {
  margin-top: 20px;
}

/* 课时详情样式 */
.class-detail {
  padding: 10px;
}

.detail-header {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
}

.detail-item {
  min-width: 200px;
}

.detail-item .label {
  font-weight: bold;
  margin-right: 10px;
  color: #606266;
}

.detail-item .value {
  color: #303133;
}

.report-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.report-item {
  border-bottom: 1px solid #EBEEF5;
  padding-bottom: 15px;
}

.report-label {
  font-weight: bold;
  color: #606266;
  margin-bottom: 8px;
}

.report-value {
  color: #303133;
  line-height: 1.6;
}

.no-report {
  padding: 30px;
  text-align: center;
}
</style> 