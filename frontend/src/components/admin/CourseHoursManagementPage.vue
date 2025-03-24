<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Document, View, More, ArrowLeft, Edit } from '@element-plus/icons-vue';

const router = useRouter();
const loading = ref(false);
const coursesList = ref([]);

// 视图状态控制
const viewState = ref('courseList'); // 可能的值: courseList, courseDetail, studentRecords
const currentCourse = ref(null);
const courseStudents = ref([]);
const studentClassRecords = ref([]);
const selectedStudent = ref(null);
const reportDialogVisible = ref(false);
const currentReport = ref(null);

// 报告编辑表单
const reportForm = reactive({
  topic: '',
  content: '',
  progress: '',
  homework: '',
  nextGoals: '',
  status: 'completed'
});

// 课时状态选项
const statusOptions = [
  { label: '已完成', value: 'completed' },
  { label: '未上完', value: 'incomplete' },
  { label: '缺席', value: 'absent' }
];

// 获取所有课程列表
const fetchCourses = () => {
  loading.value = true;
  // 模拟API请求
  setTimeout(() => {
    coursesList.value = [
      { id: 1, name: '高等数学', teacher: '张教授', totalHours: 40, completedHours: 28, remainingHours: 12 },
      { id: 2, name: '大学英语', teacher: '李教授', totalHours: 30, completedHours: 15, remainingHours: 15 },
      { id: 3, name: '物理学', teacher: '王教授', totalHours: 20, completedHours: 20, remainingHours: 0 },
      { id: 4, name: '计算机编程', teacher: '赵教授', totalHours: 60, completedHours: 42, remainingHours: 18 }
    ];
    loading.value = false;
  }, 500);
};

// 查看课程详情
const viewCourseDetail = (course) => {
  currentCourse.value = course;
  viewState.value = 'courseDetail';
  fetchCourseStudents(course.id);
};

// 返回课程列表
const backToCourseList = () => {
  viewState.value = 'courseList';
  currentCourse.value = null;
  selectedStudent.value = null;
  studentClassRecords.value = [];
};

// 获取课程的学生列表
const fetchCourseStudents = (courseId) => {
  loading.value = true;
  // 模拟API请求
  setTimeout(() => {
    courseStudents.value = [
      { id: 1, name: '张三', completedHours: 28, remainingHours: 12, totalHours: 40 },
      { id: 2, name: '李四', completedHours: 25, remainingHours: 15, totalHours: 40 },
      { id: 3, name: '王五', completedHours: 32, remainingHours: 8, totalHours: 40 },
      { id: 4, name: '赵六', completedHours: 20, remainingHours: 20, totalHours: 40 }
    ];
    loading.value = false;
  }, 500);
};

// 查看学生的课时记录
const viewStudentRecords = (student) => {
  selectedStudent.value = student;
  viewState.value = 'studentRecords';
  fetchStudentClassRecords(currentCourse.value.id, student.id);
};

// 返回课程详情
const backToCourseDetail = () => {
  viewState.value = 'courseDetail';
};

// 获取学生的课时记录
const fetchStudentClassRecords = (courseId, studentId) => {
  loading.value = true;
  // 模拟API请求
  setTimeout(() => {
    studentClassRecords.value = [
      {
        id: 1,
        date: '2023-10-15',
        startTime: '08:00',
        endTime: '09:40',
        hours: 2,
        topic: '函数极限',
        status: 'completed',
        hasReport: true,
        report: {
          topic: '函数极限',
          content: '本节课讲解了函数极限的基本概念和计算方法',
          progress: '学生掌握了基本的极限计算',
          homework: '习题册第三章1-10题',
          nextGoals: '下节课继续讲解函数连续性'
        }
      },
      {
        id: 2,
        date: '2023-10-18',
        startTime: '10:00',
        endTime: '11:40',
        hours: 2,
        topic: '函数连续性',
        status: 'incomplete',
        hasReport: true,
        report: {
          topic: '函数连续性',
          content: '讲解了函数连续性的定义和判断方法',
          progress: '学生理解了连续函数的性质',
          homework: '习题册第三章11-20题',
          nextGoals: '下节课开始微分学的内容'
        }
      },
      {
        id: 3,
        date: '2023-10-22',
        startTime: '14:00',
        endTime: '15:40',
        hours: 2,
        topic: '导数概念',
        status: 'absent',
        hasReport: false
      }
    ];
    loading.value = false;
  }, 500);
};

// 打开编辑课时报告对话框
const editClassReport = (record) => {
  currentReport.value = record;
  
  if (record.hasReport) {
    reportForm.topic = record.report.topic || record.topic;
    reportForm.content = record.report.content || '';
    reportForm.progress = record.report.progress || '';
    reportForm.homework = record.report.homework || '';
    reportForm.nextGoals = record.report.nextGoals || '';
    reportForm.status = record.status || 'completed';
  } else {
    reportForm.topic = record.topic || '';
    reportForm.content = '';
    reportForm.progress = '';
    reportForm.homework = '';
    reportForm.nextGoals = '';
    reportForm.status = record.status || 'completed';
  }
  
  reportDialogVisible.value = true;
};

// 保存课时报告
const saveReport = () => {
  if (!reportForm.topic.trim()) {
    ElMessage.warning('课程主题不能为空');
    return;
  }
  
  if (!reportForm.content.trim() && reportForm.status === 'completed') {
    ElMessage.warning('课程内容不能为空');
    return;
  }
  
  // 更新报告数据
  if (currentReport.value) {
    currentReport.value.topic = reportForm.topic;
    currentReport.value.status = reportForm.status;
    currentReport.value.hasReport = reportForm.status !== 'absent';
    
    if (reportForm.status !== 'absent') {
      currentReport.value.report = {
        topic: reportForm.topic,
        content: reportForm.content,
        progress: reportForm.progress,
        homework: reportForm.homework,
        nextGoals: reportForm.nextGoals
      };
    } else {
      // 如果是缺席状态，清空报告内容
      currentReport.value.report = null;
    }
  }
  
  ElMessage.success('课时报告保存成功');
  reportDialogVisible.value = false;
};

// 关闭报告对话框
const closeReportDialog = () => {
  reportDialogVisible.value = false;
  currentReport.value = null;
};

// 获取课时状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case 'completed':
      return 'success';
    case 'incomplete':
      return 'warning';
    case 'absent':
      return 'danger';
    default:
      return 'info';
  }
};

// 获取课时状态显示文本
const getStatusText = (status) => {
  switch (status) {
    case 'completed':
      return '已完成';
    case 'incomplete':
      return '未上完';
    case 'absent':
      return '缺席';
    default:
      return '未知';
  }
};

// 初始化
onMounted(() => {
  fetchCourses();
});
</script>

<template>
  <div class="course-hours-management-container">
    <!-- 课程列表视图 -->
    <div v-if="viewState === 'courseList'">
      <h1>课时管理</h1>
      
      <el-card shadow="never">
        <template #header>
          <div class="card-header">
            <span>所有课程</span>
          </div>
        </template>
        
        <el-table :data="coursesList" v-loading="loading" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="name" label="课程名称" />
          <el-table-column prop="teacher" label="授课教师" />
          <el-table-column label="课时信息">
            <template #default="scope">
              <div class="hours-info">
                <span>总课时: {{ scope.row.totalHours }}</span>
                <span>已完成: {{ scope.row.completedHours }}</span>
                <span>剩余: {{ scope.row.remainingHours }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button
                size="small"
                type="primary"
                @click="viewCourseDetail(scope.row)"
              >
                <el-icon><View /></el-icon>
                查看课程
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 课程详情视图 -->
    <div v-else-if="viewState === 'courseDetail'" class="course-detail-view">
      <div class="page-header">
        <el-button @click="backToCourseList" type="primary" plain>
          <el-icon><ArrowLeft /></el-icon>
          返回课程列表
        </el-button>
        <h2>{{ currentCourse.name }} - {{ currentCourse.teacher }}</h2>
      </div>
      
      <!-- 课程总览 -->
      <el-card class="course-overview" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>课程总览</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="overview-item">
              <div class="overview-label">总课时</div>
              <div class="overview-value">{{ currentCourse.totalHours }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="overview-item">
              <div class="overview-label">已完成课时</div>
              <div class="overview-value">{{ currentCourse.completedHours }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="overview-item">
              <div class="overview-label">剩余课时</div>
              <div class="overview-value">{{ currentCourse.remainingHours }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 学生列表 -->
      <el-card class="student-list" shadow="never">
        <template #header>
          <div class="card-header">
            <span>学生列表</span>
          </div>
        </template>
        
        <el-table :data="courseStudents" v-loading="loading" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="name" label="学生姓名" />
          <el-table-column label="已完成课时" width="120">
            <template #default="scope">
              {{ scope.row.completedHours }}
            </template>
          </el-table-column>
          <el-table-column label="剩余课时" width="120">
            <template #default="scope">
              {{ scope.row.remainingHours }}
            </template>
          </el-table-column>
          <el-table-column label="总课时" width="100">
            <template #default="scope">
              {{ scope.row.totalHours }}
            </template>
          </el-table-column>
          <el-table-column label="进度" width="180">
            <template #default="scope">
              <el-progress
                :percentage="Math.round((scope.row.completedHours / scope.row.totalHours) * 100)"
                :status="scope.row.completedHours === scope.row.totalHours ? 'success' : ''"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button
                size="small"
                type="primary"
                @click="viewStudentRecords(scope.row)"
              >
                <el-icon><Document /></el-icon>
                查看课时记录
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 学生课时记录视图 -->
    <div v-else-if="viewState === 'studentRecords'" class="student-records-view">
      <div class="page-header">
        <el-button @click="backToCourseDetail" type="primary" plain>
          <el-icon><ArrowLeft /></el-icon>
          返回课程详情
        </el-button>
        <h2>{{ selectedStudent.name }} 的课时记录 - {{ currentCourse.name }}</h2>
      </div>
      
      <!-- 学生信息卡片 -->
      <el-card class="student-info" shadow="hover">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <div class="info-label">学生ID</div>
              <div class="info-value">{{ selectedStudent.id }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <div class="info-label">学生姓名</div>
              <div class="info-value">{{ selectedStudent.name }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <div class="info-label">总课时</div>
              <div class="info-value">{{ selectedStudent.totalHours }}</div>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="mt-20">
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">已完成课时</div>
              <div class="info-value">{{ selectedStudent.completedHours }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">剩余课时</div>
              <div class="info-value">{{ selectedStudent.remainingHours }}</div>
            </div>
          </el-col>
        </el-row>
        
        <el-row class="mt-20">
          <el-col :span="24">
            <div class="info-label">课程进度</div>
            <el-progress 
              :percentage="Math.round((selectedStudent.completedHours / selectedStudent.totalHours) * 100)"
              :status="selectedStudent.completedHours === selectedStudent.totalHours ? 'success' : ''"
            />
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 课时记录表格 -->
      <el-card class="class-records" shadow="never">
        <template #header>
          <div class="card-header">
            <span>课时记录</span>
          </div>
        </template>
        
        <el-table :data="studentClassRecords" v-loading="loading" style="width: 100%">
          <el-table-column prop="date" label="日期" width="120" />
          <el-table-column label="时间" width="180">
            <template #default="scope">
              {{ scope.row.startTime }} - {{ scope.row.endTime }}
            </template>
          </el-table-column>
          <el-table-column prop="topic" label="课程主题" />
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="hours" label="课时" width="80" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button
                size="small"
                type="primary"
                @click="editClassReport(scope.row)"
              >
                <el-icon><Edit /></el-icon>
                编辑报告
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 课时报告编辑对话框 -->
    <el-dialog
      v-model="reportDialogVisible"
      title="编辑课时报告"
      width="60%"
      @closed="closeReportDialog"
    >
      <div v-if="currentReport" class="report-form">
        <el-form :model="reportForm" label-width="120px">
          <el-form-item label="课程状态" required>
            <el-radio-group v-model="reportForm.status" class="status-radio-group">
              <el-radio
                v-for="option in statusOptions"
                :key="option.value"
                :label="option.value"
                border
              >
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="课程主题" required>
            <el-input v-model="reportForm.topic" placeholder="请输入课程主题" />
          </el-form-item>
          
          <template v-if="reportForm.status !== 'absent'">
            <el-form-item label="课程内容" :required="reportForm.status === 'completed'">
              <el-input 
                v-model="reportForm.content" 
                type="textarea" 
                rows="4"
                placeholder="请输入本次课程的主要内容"
              />
            </el-form-item>
            
            <el-form-item label="学习进度">
              <el-input 
                v-model="reportForm.progress" 
                type="textarea" 
                rows="3"
                placeholder="请描述学生的学习进度和掌握情况"
              />
            </el-form-item>
            
            <el-form-item label="作业布置">
              <el-input 
                v-model="reportForm.homework" 
                placeholder="请输入布置的作业"
              />
            </el-form-item>
            
            <el-form-item label="下次课目标">
              <el-input 
                v-model="reportForm.nextGoals" 
                placeholder="请输入下次课的学习目标"
              />
            </el-form-item>
          </template>
          
          <div v-else class="absent-notice">
            <p>学生缺席，无需填写课程内容和作业</p>
          </div>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reportDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveReport">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.course-hours-management-container {
  padding: 20px;
}

.hours-info {
  display: flex;
  gap: 15px;
}

.card-header {
  font-weight: bold;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 0 20px;
}

.course-overview, .student-info {
  margin-bottom: 20px;
}

.overview-item, .info-item {
  text-align: center;
  padding: 15px;
}

.overview-label, .info-label {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.overview-value, .info-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.mt-20 {
  margin-top: 20px;
}

.student-list {
  margin-bottom: 20px;
}

.class-records {
  margin-top: 20px;
}

.status-radio-group {
  display: flex;
  margin-bottom: 20px;
}

.status-radio-group .el-radio {
  margin-right: 20px;
}

.absent-notice {
  background-color: #fef0f0;
  color: #f56c6c;
  padding: 15px;
  border-radius: 4px;
  margin: 20px 0;
  text-align: center;
}

.report-form {
  padding: 10px;
}
</style> 