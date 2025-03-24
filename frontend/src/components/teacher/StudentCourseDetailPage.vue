<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElDialog, ElForm, ElFormItem, ElInput, ElButton, ElRadioGroup, ElRadio } from 'element-plus';
import { ArrowLeft, Calendar, Document, Edit } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();
const courseId = route.query.courseId;
const studentName = route.query.studentName;

const loading = ref(true);
const courseInfo = ref({});
const recentClasses = ref([]);
const dialogVisible = ref(false);
const currentReport = ref(null);

const reportForm = reactive({
  topic: '',
  content: '',
  progress: '',
  homework: '',
  nextGoals: '',
  status: 'completed' // 默认状态为已完成
});

// 课时状态选项
const statusOptions = [
  { label: '已完成', value: 'completed' },
  { label: '未上完', value: 'incomplete' },
  { label: '缺席', value: 'absent' }
];

// 获取课程详情和近期课程数据
onMounted(() => {
  // 模拟API请求
  setTimeout(() => {
    // 模拟课程信息
    courseInfo.value = {
      id: courseId,
      name: getCourseNameById(courseId),
      studentName: studentName,
      totalHours: 40,
      completedHours: 28,
      startDate: '2023-09-01',
      endDate: '2023-12-31'
    };
    
    // 模拟近期课程数据
    recentClasses.value = [
      {
        id: 1,
        date: '2023-10-15',
        startTime: '08:00',
        endTime: '09:40',
        hours: 2,
        topic: '函数极限',
        hasReport: true,
        status: 'completed',
        report: {
          topic: '函数极限',
          content: '本节课讲解了函数极限的基本概念和计算方法',
          progress: '学生掌握了基本的极限计算',
          homework: '习题册第三章1-10题',
          nextGoals: '下节课继续讲解函数连续性',
          status: 'completed'
        }
      },
      {
        id: 2,
        date: '2023-10-18',
        startTime: '10:00',
        endTime: '11:40',
        hours: 2,
        topic: '函数连续性',
        hasReport: true,
        status: 'incomplete',
        report: {
          topic: '函数连续性',
          content: '讲解了函数连续性的定义和判断方法',
          progress: '学生理解了连续函数的性质',
          homework: '习题册第三章11-20题',
          nextGoals: '下节课开始微分学的内容',
          status: 'incomplete'
        }
      },
      {
        id: 3,
        date: '2023-10-22',
        startTime: '14:00',
        endTime: '15:40',
        hours: 2,
        topic: '导数概念',
        hasReport: false,
        status: 'absent'
      }
    ];
    
    loading.value = false;
  }, 500);
});

// 根据ID获取课程名称
function getCourseNameById(id) {
  const courses = {
    '1': '高等数学',
    '2': '大学英语',
    '3': '物理学',
    '4': '计算机编程'
  };
  return courses[id] || '未知课程';
}

// 返回课程管理页面
const goBack = () => {
  router.push('/index/teacher/course-management');
};

// 打开课时报告编辑框
const editReport = (course) => {
  currentReport.value = course;
  
  if (course.hasReport) {
    reportForm.topic = course.report.topic || course.topic;
    reportForm.content = course.report.content;
    reportForm.progress = course.report.progress;
    reportForm.homework = course.report.homework;
    reportForm.nextGoals = course.report.nextGoals;
    reportForm.status = course.report.status || course.status || 'completed';
  } else {
    reportForm.topic = course.topic || '';
    reportForm.content = '';
    reportForm.progress = '';
    reportForm.homework = '';
    reportForm.nextGoals = '';
    reportForm.status = course.status || 'completed';
  }
  
  dialogVisible.value = true;
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
        nextGoals: reportForm.nextGoals,
        status: reportForm.status
      };
    } else {
      // 如果是缺席状态，清空报告内容
      currentReport.value.report = null;
    }
  }
  
  ElMessage.success('课时报告保存成功');
  dialogVisible.value = false;
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
</script>

<template>
  <div class="student-course-detail-container" v-loading="loading">
    <div class="page-header">
      <div class="back-button">
        <el-button @click="goBack" icon="arrow-left" class="back-btn">返回</el-button>
      </div>
      <div class="page-title">
        <h2>{{ courseInfo.name }} - {{ studentName }} 课时详情</h2>
      </div>
    </div>
    
    <div class="course-summary">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>课程概览</span>
          </div>
        </template>
        <div class="summary-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="summary-item">
                <div class="label">总课时:</div>
                <div class="value">{{ courseInfo.totalHours }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="summary-item">
                <div class="label">已完成课时:</div>
                <div class="value">{{ courseInfo.completedHours }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="summary-item">
                <div class="label">剩余课时:</div>
                <div class="value">{{ courseInfo.totalHours - courseInfo.completedHours }}</div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="mt-20">
            <el-col :span="12">
              <div class="summary-item">
                <div class="label">开课日期:</div>
                <div class="value">{{ courseInfo.startDate }}</div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="summary-item">
                <div class="label">结束日期:</div>
                <div class="value">{{ courseInfo.endDate }}</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>
    
    <!-- 近期课程记录 -->
    <el-card class="recent-courses">
      <template #header>
        <div class="card-header">
          <span>近期课程记录</span>
        </div>
      </template>
      
      <el-table :data="recentClasses" style="width: 100%">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column label="时间" width="160">
          <template #default="scope">
            {{ scope.row.startTime }} - {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="hours" label="课时" width="80" />
        <el-table-column prop="topic" label="课程主题" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="课时报告" width="120">
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
              @click="editReport(scope.row)"
            >
              编辑报告
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 课时报告编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑课时报告"
      width="60%"
    >
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
          <el-input 
            v-model="reportForm.topic" 
            placeholder="请输入课程主题"
          />
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
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveReport">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.student-course-detail-container {
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  justify-content: flex-start;
}

.back-button {
  width: 100px;
  display: flex;
  justify-content: center;
}

.back-btn {
  width: 80px;
}

.page-title {
  flex: 1;
}

.page-title h2 {
  margin: 0;
}

.course-summary {
  margin-bottom: 20px;
}

.recent-courses {
  margin-top: 20px;
}

.card-header {
  font-weight: bold;
}

.summary-content {
  padding: 10px;
}

.mt-20 {
  margin-top: 20px;
}

.summary-item {
  display: flex;
  align-items: center;
  padding: 10px;
}

.summary-item .label {
  font-weight: bold;
  margin-right: 10px;
  min-width: 100px;
}

.summary-item .value {
  color: #409EFF;
  font-size: 16px;
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
</style> 