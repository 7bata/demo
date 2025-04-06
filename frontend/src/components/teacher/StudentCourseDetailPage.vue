<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElDialog, ElForm, ElFormItem, ElInput, ElButton, ElRadioGroup, ElRadio, ElDatePicker, ElTimePicker, ElMessageBox, ElLoading } from 'element-plus';
import { ArrowLeft, Calendar, Document, Edit } from '@element-plus/icons-vue';
import { get, post, put } from '@/net';
import axios from 'axios';
import qs from 'qs'; // 引入qs库用于表单数据序列化

const route = useRoute();
const router = useRouter();
const courseId = route.query.courseId;
const studentId = route.query.studentId || null;
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
  status: 'completed', // 默认状态为已完成
  classDate: new Date().toISOString().substring(0, 10), // 添加上课日期
  startTime: '08:00', // 添加开始时间
  endTime: '09:40' // 添加结束时间
});

// 课时状态选项
const statusOptions = [
  { label: '已完成', value: 'completed' },
  { label: '未上完', value: 'incomplete' },
  { label: '缺席', value: 'absent' }
];

// 获取课程详情和近期课程数据
onMounted(() => {
  // 检查URL参数
  if (!courseId) {
    ElMessage.error('缺少课程ID参数');
    setTimeout(() => {
      router.push('/index/teacher/course-management');
    }, 2000);
    return;
  }
  
  console.log("课程详情页参数:", {
    courseId,
    studentId,
    studentName
  });
  
  fetchCourseInfo();
  fetchClassRecords(); // 这个函数会根据是否有studentId决定如何处理
});

// 通过学生姓名查找学生ID
const findStudentIdByName = () => {
  if (!courseId || !studentName) {
    ElMessage.error('缺少课程或学生信息');
    loading.value = false;
    return;
  }
  
  get(`/api/teacher/course-students/${courseId}`, (students) => {
    console.log("获取课程学生列表成功:", students);
    
    // 处理可能的嵌套数据结构
    const studentList = Array.isArray(students) ? students : (students.message || []);
    
    // 查找匹配的学生
    const student = studentList.find(s => s.studentName === studentName);
    
    if (student) {
      console.log("找到学生:", student);
      studentId = student.studentId;
      fetchRecordsByStudentId();
    } else {
      // 尝试在数据库查找学生ID
      get(`/api/teacher/student/by-name?name=${encodeURIComponent(studentName)}`, (result) => {
        if (result && result.id) {
          console.log("通过名称API找到学生:", result);
          studentId = result.id;
          fetchRecordsByStudentId();
        } else {
          ElMessage.error(`找不到学生: ${studentName}`);
          loading.value = false;
        }
      }, (error) => {
        console.error("查找学生失败:", error);
        ElMessage.error(`找不到学生: ${studentName}`);
        loading.value = false;
      });
    }
  }, (message) => {
    ElMessage.error('获取学生列表失败: ' + message);
    loading.value = false;
  });
};

// 获取课程信息
const fetchCourseInfo = () => {
  if (!courseId) {
    ElMessage.error('缺少课程ID');
    loading.value = false;
    return;
  }
  
  get(`/api/teacher/course/${courseId}`, (response) => {
    console.log("获取课程信息成功，完整响应:", response);
    
    // 处理嵌套在message中的数据
    let courseData = null;
    if (response.success && response.message) {
      courseData = response.message;
    } else if (response.data) {
      courseData = response.data;
    } else {
      courseData = response;
    }
    
    console.log("提取的课程数据:", courseData);
    
    courseInfo.value = {
      id: courseData.id || courseId,
      name: courseData.courseName || '未知课程',
      studentName: studentName || '未知学生',
      totalHours: courseData.totalHours || 40, // 默认课时为40
      completedHours: courseData.completedHours || 0,
      startDate: courseData.startDate || '2023-09-01',
      endDate: courseData.endDate || '2023-12-31'
    };
    
    // 查找学生的课时记录的总时长 - 使用正确的API URL
    if (studentId) {
      get(`/api/teacher/student-course-records?studentId=${studentId}&courseId=${courseId}`, (records) => {
        console.log("获取课时统计响应:", records);
        let recordData = [];
        
        // 处理嵌套在message中的数据
        if (records.success && Array.isArray(records.message)) {
          recordData = records.message;
        } else if (records.success && records.data && Array.isArray(records.data)) {
          recordData = records.data;
        } else if (Array.isArray(records)) {
          recordData = records;
        }
        
        // 计算完成的课时数
        let completed = 0;
        recordData.forEach(record => {
          if (record.status === 'completed') {
            completed += 2; // 假设每节课是2课时
          }
        });
        
        // 更新课时信息
        courseInfo.value.completedHours = completed;
      });
    }
  }, (message) => {
    console.error("获取课程信息失败:", message);
    ElMessage.error('获取课程信息失败: ' + message);
    loading.value = false;
  });
};

// 获取课时记录
const fetchClassRecords = () => {
  if (!courseId || (!studentId && !studentName)) {
    ElMessage.error('缺少课程或学生信息');
    loading.value = false;
    return;
  }
  
  // 如果有studentId则直接获取课时记录
  if (studentId) {
    fetchRecordsByStudentId();
  } else {
    // 否则先通过studentName获取studentId
    findStudentIdByName();
  }
};

// 获取特定学生的课时记录
const fetchRecordsByStudentId = () => {
  loading.value = true;
  
  console.log("获取学生课时记录 - 学生ID:", studentId, "课程ID:", courseId);
  
  // 调用API获取学生的课时记录
  get(`/api/teacher/student-course-records?studentId=${studentId}&courseId=${courseId}`, (data) => {
    const records = data || [];
    console.log("原始课时记录数据:", records);
    
    recentClasses.value = records.map(record => {
      // 确保日期是正确的格式
      const classDate = record.classDate ? new Date(record.classDate).toISOString().split('T')[0] : '';
      
      // 设置课时长度（小时）
      let hours = 0;
      if (record.startTime && record.endTime) {
        const start = record.startTime.split(':').map(Number);
        const end = record.endTime.split(':').map(Number);
        hours = ((end[0] - start[0]) * 60 + (end[1] - start[1])) / 60;
        hours = Math.round(hours * 10) / 10; // 四舍五入到一位小数
      }
      
      return {
        id: record.id,
        date: classDate,
        startTime: record.startTime || '08:00',
        endTime: record.endTime || '09:40',
        hours: hours,
        topic: record.theme || '',
        status: record.status || 'completed',
        hasReport: true,
        report: {
          topic: record.theme,
          content: record.content,
          progress: record.learningProgress,
          homework: record.homework,
          nextGoals: record.nextClassGoal,
          status: record.status
        }
      };
    });
    
    console.log("转换后的课时记录:", recentClasses.value);
    loading.value = false;
  }, (message) => {
    console.error("获取课时记录失败:", message);
    ElMessage.error('获取课时记录失败: ' + message);
    loading.value = false;
  });
};

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
  
  reportForm.classDate = course.date || new Date().toISOString().substring(0, 10);
  reportForm.startTime = course.startTime || '08:00';
  reportForm.endTime = course.endTime || '09:40';
  
  dialogVisible.value = true;
};

// 添加新的课时记录
const addNewClassRecord = () => {
  const today = new Date().toISOString().substring(0, 10);
  
  // 初始化一个新的课时记录对象
  currentReport.value = {
    id: null, // 新记录没有ID
    date: today,
    startTime: '08:00',
    endTime: '09:40',
    topic: '',
    status: 'completed',
    hasReport: false
  };
  
  console.log("创建新课时记录:", currentReport.value);
  
  // 重置表单数据
  reportForm.topic = '';
  reportForm.content = '';
  reportForm.progress = '';
  reportForm.homework = '';
  reportForm.nextGoals = '';
  reportForm.status = 'completed';
  reportForm.classDate = today;
  reportForm.startTime = '08:00';
  reportForm.endTime = '09:40';
  
  // 打开对话框
  dialogVisible.value = true;
};

// 保存课时报告
const saveReport = () => {
  // 表单验证
  if (reportForm.status !== 'absent' && (!reportForm.topic || reportForm.topic.trim() === '')) {
    ElMessage.warning('请填写课程主题');
    return;
  }
  
  if (reportForm.status === 'completed' && (!reportForm.content || reportForm.content.trim() === '')) {
    ElMessage.warning('已完成的课程需要填写课程内容');
    return;
  }
  
  // 确保日期格式为YYYY-MM-DD
  const classDate = reportForm.classDate ? 
    (typeof reportForm.classDate === 'string' ? 
      reportForm.classDate : 
      reportForm.classDate.toISOString().split('T')[0]) : 
    new Date().toISOString().split('T')[0];
    
  // 准备提交的数据
  const record = {
    id: currentReport.value?.id || null, // 使用可选链运算符，如果是新记录则设为null
    studentId: parseInt(studentId),
    courseId: parseInt(courseId),
    classDate: classDate,
    startTime: reportForm.startTime, // 使用表单中的开始时间
    endTime: reportForm.endTime, // 使用表单中的结束时间
    theme: reportForm.topic,
    content: reportForm.content,
    status: reportForm.status,
    learningProgress: reportForm.progress,
    homework: reportForm.homework,
    nextClassGoal: reportForm.nextGoals,
    teacherId: JSON.parse(sessionStorage.getItem('userInfo') || '{}').id || 0
  };
  
  console.log("准备保存课时记录数据:", record);
  
  // 创建一个显示加载状态的提示
  const loading = ElLoading.service({
    lock: true,
    text: record.id ? '更新中...' : '创建中...',
    background: 'rgba(0, 0, 0, 0.7)'
  });
  
  try {
    // 使用JSON格式提交数据 (后端使用@RequestBody注解)
    if (record.id) {
      // 使用PUT更新现有记录
      console.log("开始更新现有记录:", record.id);
      axios.put('/api/teacher/class-hour-record', record, {
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true
      }).then(response => {
        loading.close(); // 关闭加载状态
        console.log('更新课时报告完整响应:', response);
        if (response.data && (response.data.success || response.data.status === 200)) {
          ElMessage.success('课时报告更新成功');
          dialogVisible.value = false;
          // 刷新课时记录 - 延长等待时间确保后端处理完成
          setTimeout(() => {
            fetchRecordsByStudentId();
          }, 1000);
        } else {
          ElMessage.error('更新失败: ' + (response.data?.message || '未知错误'));
        }
      }).catch(error => {
        loading.close(); // 关闭加载状态
        console.error("更新课时记录失败:", error);
        ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message || '未知错误'));
      });
    } else {
      // 使用POST创建新记录
      console.log("开始创建新记录");
      axios.post('/api/teacher/class-hour-record', record, {
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true
      }).then(response => {
        loading.close(); // 关闭加载状态
        console.log('保存课时报告完整响应:', response);
        if (response.data && (response.data.success || response.data.status === 200)) {
          ElMessage.success('课时报告保存成功');
          dialogVisible.value = false;
          // 刷新课时记录 - 延长等待时间确保后端处理完成
          setTimeout(() => {
            fetchRecordsByStudentId();
          }, 1000);
        } else {
          ElMessage.error('保存失败: ' + (response.data?.message || '未知错误'));
        }
      }).catch(error => {
        loading.close(); // 关闭加载状态
        console.error("保存课时记录失败:", error);
        if (error.response) {
          console.error("错误响应状态:", error.response.status);
          console.error("错误响应数据:", error.response.data);
        }
        ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message || '未知错误'));
      });
    }
  } catch (error) {
    loading.close(); // 关闭加载状态
    console.error("保存操作发生异常:", error);
    ElMessage.error('保存时发生错误: ' + error.message);
  }
};

// 获取课时状态标签类型
const getStatusType = (status) => {
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

// 格式化日期
const formatDate = (date) => {
  console.log("格式化日期输入:", date, "类型:", typeof date);
  
  if (!date) {
    return '未知日期';
  }
  
  try {
    // 如果是字符串，尝试识别不同的日期格式
    if (typeof date === 'string') {
      // 尝试解析ISO日期格式 (YYYY-MM-DD)
      if (date.match(/^\d{4}-\d{2}-\d{2}$/)) {
        // 已经是YYYY-MM-DD格式，直接返回
        console.log("已经是YYYY-MM-DD格式，直接返回:", date);
        return date;
      }
      
      // 尝试解析MM/DD/YYYY格式
      if (date.match(/^\d{1,2}\/\d{1,2}\/\d{4}$/)) {
        const parts = date.split('/');
        const month = parts[0].padStart(2, '0');
        const day = parts[1].padStart(2, '0');
        const year = parts[2];
        console.log("转换MM/DD/YYYY格式:", `${year}-${month}-${day}`);
        return `${year}-${month}-${day}`;
      }
      
      // 尝试用Date构造函数
      const parsedDate = new Date(date);
      if (!isNaN(parsedDate.getTime())) {
        const year = parsedDate.getFullYear();
        const month = String(parsedDate.getMonth() + 1).padStart(2, '0');
        const day = String(parsedDate.getDate()).padStart(2, '0');
        console.log("使用Date构造函数解析:", `${year}-${month}-${day}`);
        return `${year}-${month}-${day}`;
      }
    }
    
    // 如果是Date对象
    if (date instanceof Date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      console.log("Date对象格式化:", `${year}-${month}-${day}`);
      return `${year}-${month}-${day}`;
    }
    
    // 如果无法识别格式，返回原始值
    console.warn("无法识别的日期格式:", date);
    return date;
  } catch (error) {
    console.error("日期格式化错误:", error);
    return '日期格式错误';
  }
};

// 查看或编辑报告
const viewOrEditReport = (record) => {
  currentReport.value = record;
  
  if (record.hasReport && record.report) {
    reportForm.topic = record.report.topic || '';
    reportForm.content = record.report.content || '';
    reportForm.progress = record.report.progress || '';
    reportForm.homework = record.report.homework || '';
    reportForm.nextGoals = record.report.nextGoals || '';
    reportForm.status = record.report.status || 'completed';
  } else {
    reportForm.topic = record.topic || '';
    reportForm.content = '';
    reportForm.progress = '';
    reportForm.homework = '';
    reportForm.nextGoals = '';
    reportForm.status = record.status || 'completed';
  }
  
  dialogVisible.value = true;
};

// 获取状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case 'completed':
      return 'success';
    case 'absent':
      return 'danger';
    case 'canceled':
      return 'info';
    default:
      return '';
  }
};

// 确认删除
const confirmDelete = (record) => {
  ElMessageBox.confirm(
    '确定要删除这条课时记录吗？此操作不可恢复。',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    deleteRecord(record.id);
  }).catch(() => {
    // 用户取消删除
  });
};

// 删除记录
const deleteRecord = (recordId) => {
  if (!recordId) {
    ElMessage.error('记录ID无效');
    return;
  }
  
  axios.delete(`/api/teacher/class-hour-record/${recordId}`, {
    withCredentials: true
  }).then(response => {
    console.log('删除记录响应:', response);
    if (response.data && response.data.success) {
      ElMessage.success('记录删除成功');
      // 刷新课时记录
      fetchRecordsByStudentId();
    } else {
      ElMessage.error('删除失败: ' + (response.data?.message || '未知错误'));
    }
  }).catch(error => {
    console.error('删除记录失败:', error);
    ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message || '未知错误'));
  });
};
</script>

<template>
  <div class="student-course-detail-container" v-loading="loading">
    <div class="header-section">
      <el-button @click="goBack" type="primary" plain>
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h2>{{ courseInfo.name }} - {{ courseInfo.studentName }}</h2>
    </div>
    
    <el-card class="course-info-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <span class="label">总课时:</span>
            <span class="value">{{ courseInfo.totalHours }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <span class="label">已完成课时:</span>
            <span class="value">{{ courseInfo.completedHours }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <span class="label">剩余课时:</span>
            <span class="value">{{ courseInfo.totalHours - courseInfo.completedHours }}</span>
          </div>
        </el-col>
      </el-row>
      
      <el-progress 
        :percentage="Math.round((courseInfo.completedHours / courseInfo.totalHours) * 100)" 
        :stroke-width="18"
        :format="format => `${courseInfo.completedHours}/${courseInfo.totalHours}`"
        class="course-progress"
      />
      
      <el-row :gutter="20" class="date-info">
        <el-col :span="12">
          <div class="info-item">
            <span class="label">开课日期:</span>
            <span class="value">{{ courseInfo.startDate }}</span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <span class="label">结课日期:</span>
            <span class="value">{{ courseInfo.endDate }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <div class="section-title">
      <h3>近期课程记录</h3>
      <el-button type="primary" @click="addNewClassRecord">
        <el-icon><Calendar /></el-icon>
        添加课时记录
      </el-button>
    </div>
    
    <div v-loading="loading">
      <div v-if="recentClasses.length === 0" class="empty-state">
        <el-empty description="暂无课时记录">
          <el-button type="primary" @click="addNewClassRecord">立即添加第一条记录</el-button>
        </el-empty>
      </div>
      
      <el-table v-else :data="recentClasses" style="width: 100%">
        <el-table-column prop="date" label="日期" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.date) }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="120">
          <template #default="scope">
            {{ scope.row.startTime }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="120">
          <template #default="scope">
            {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="hours" label="课时" width="80" />
        <el-table-column prop="topic" label="主题" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="viewOrEditReport(scope.row)">
              {{ scope.row.hasReport ? '查看报告' : '添加报告' }}
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="confirmDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 课时报告编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑课时报告"
      width="60%"
    >
      <el-form :model="reportForm" label-width="120px">
        <el-form-item label="上课日期" required>
          <el-date-picker
            v-model="reportForm.classDate"
            type="date"
            placeholder="选择上课日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="上课时间" required>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-time-picker
                v-model="reportForm.startTime"
                placeholder="开始时间"
                format="HH:mm"
                value-format="HH:mm"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="12">
              <el-time-picker
                v-model="reportForm.endTime"
                placeholder="结束时间"
                format="HH:mm"
                value-format="HH:mm"
                style="width: 100%"
              />
            </el-col>
          </el-row>
        </el-form-item>
        
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

.header-section {
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

.course-info-card {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 10px;
}

.info-item .label {
  font-weight: bold;
  margin-right: 10px;
  min-width: 100px;
}

.info-item .value {
  color: #409EFF;
  font-size: 16px;
}

.course-progress {
  margin-top: 10px;
  margin-bottom: 10px;
}

.date-info {
  margin-top: 10px;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.section-title h3 {
  margin: 0;
}

.add-record-btn {
  width: 100px;
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

.empty-state {
  text-align: center;
  padding: 20px;
}
</style> 