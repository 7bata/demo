<script setup>
import { ref, onMounted, computed } from 'vue'
import { post, get } from '@/net'
import { ElMessage } from 'element-plus'
import { Document, ArrowRight, Clock } from '@element-plus/icons-vue'

// 课时统计数据
const courseHoursStats = ref({
  remainingHours: 0,
  consumedHours: 0
})

// 课程列表数据
const courseList = ref([])
const loading = ref(false)

// 历史记录详情数据
const historyRecords = ref([])
const detailsVisible = ref(false)
const currentCourse = ref({})

// 获取用户ID
const getUserId = () => {
  const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
  return userInfo.id
}

// 获取课时统计数据
const fetchCourseHoursStats = () => {
  const studentId = getUserId()
  if (!studentId) {
    ElMessage.error('未找到学生信息，请重新登录')
    return
  }
  
  console.log("获取学生课时统计，学生ID:", studentId)
  
  post('/api/student/course-hours/stats', (data) => {
    console.log("获取课时统计成功:", data)
    courseHoursStats.value = data
  }, {
    studentId: studentId
  }, (message) => {
    console.error("获取课时统计失败:", message)
    ElMessage.error('获取课时统计失败: ' + message)
  })
}

// 获取课程列表
const fetchCourseList = () => {
  loading.value = true
  const studentId = getUserId()
  if (!studentId) {
    ElMessage.error('未找到学生信息，请重新登录')
    loading.value = false
    return
  }
  
  console.log("获取学生课程列表，学生ID:", studentId)
  
  // 首先获取课程基本信息
  get(`/api/student/courses?studentId=${studentId}`, (courses) => {
    console.log("获取课程列表成功:", courses)
    courseList.value = courses
    loading.value = false
  }, (message) => {
    console.error("获取课程列表失败:", message)
    ElMessage.error('获取课程列表失败: ' + message)
    loading.value = false
  })
}

// 查看课程详情
const viewCourseDetails = (course) => {
  // 设置当前课程
  currentCourse.value = {
    ...course,
    consumedHours: course.completedHours,
    remainingHours: course.totalHours - course.completedHours
  }
  
  fetchCourseHistoryRecords(course.id)
  detailsVisible.value = true
}

// 获取课程历史记录
const fetchCourseHistoryRecords = (courseId) => {
  loading.value = true
  const studentId = getUserId()
  if (!studentId) {
    ElMessage.error('未找到学生信息，请重新登录')
    loading.value = false
    return
  }
  
  get(`/api/student/course-hours/${courseId}?studentId=${studentId}`, (data) => {
    historyRecords.value = data
    loading.value = false
  }, (message) => {
    ElMessage.error('获取课时记录失败: ' + message)
    loading.value = false
  })
}

// 获取课时状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case 'completed':
      return 'success'
    case 'incomplete':
      return 'warning'
    case 'absent':
      return 'danger'
    default:
      return 'info'
  }
}

// 获取课时状态显示文本
const getStatusText = (status) => {
  switch (status) {
    case 'completed':
      return '已完成'
    case 'incomplete':
      return '未上完'
    case 'absent':
      return '缺席'
    default:
      return '未知'
  }
}

// 返回课程列表
const backToCourseList = () => {
  detailsVisible.value = false
}

// 总课时统计
const totalStats = computed(() => {
  if (courseHoursStats.value) {
    return {
      total: courseHoursStats.value.remainingHours + courseHoursStats.value.consumedHours,
      consumed: courseHoursStats.value.consumedHours,
      remaining: courseHoursStats.value.remainingHours
    }
  }
  return { total: 0, consumed: 0, remaining: 0 }
})

// 页面加载时获取数据
onMounted(() => {
  fetchCourseHoursStats()
  fetchCourseList()
})
</script>

<template>
  <div class="course-hours-container">
    <!-- 课时统计卡片 -->
    <el-row v-if="!detailsVisible" :gutter="20" class="stats-cards">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>总课时</span>
            </div>
          </template>
          <div class="card-value">
            {{ totalStats.total }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>已消耗课时</span>
            </div>
          </template>
          <div class="card-value">
            {{ totalStats.consumed }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>剩余课时</span>
            </div>
          </template>
          <div class="card-value">
            {{ totalStats.remaining }}
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 课程分类列表 -->
    <el-card v-if="!detailsVisible" class="course-list" shadow="never">
      <template #header>
        <div class="card-header">
          <span>课程分类</span>
        </div>
      </template>
      <el-table
          v-loading="loading"
          :data="courseList"
          style="width: 100%"
          border>
        <el-table-column
            prop="courseName"
            label="课程名称"
            width="180">
        </el-table-column>
        <el-table-column
            prop="teacherName"
            label="教师名称"
            width="150">
        </el-table-column>
        <el-table-column
            prop="totalHours"
            label="总课时"
            width="120">
        </el-table-column>
        <el-table-column
            prop="completedHours"
            label="已消耗"
            width="120">
        </el-table-column>
        <el-table-column
            label="剩余"
            width="120">
          <template #default="scope">
            {{ scope.row.totalHours - scope.row.completedHours }}
          </template>
        </el-table-column>
        <el-table-column
            label="操作">
          <template #default="scope">
            <el-button
                type="primary"
                size="small"
                @click="viewCourseDetails(scope.row)">
              <el-icon><Document /></el-icon>
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 课程详情 -->
    <div v-if="detailsVisible" class="course-details">
      <div class="details-header">
        <el-button @click="backToCourseList" type="primary" plain>
          返回课程列表
        </el-button>
        <h2>{{ currentCourse.courseName }} - {{ currentCourse.teacherName }}</h2>
      </div>
      
      <el-card class="course-stats" shadow="hover">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-label">总课时</div>
              <div class="stat-value">{{ currentCourse.totalHours }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-label">已消耗</div>
              <div class="stat-value">{{ currentCourse.consumedHours }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-label">剩余</div>
              <div class="stat-value">{{ currentCourse.remainingHours }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <el-card class="history-table">
        <template #header>
          <div class="card-header">
            <span>上课历史记录</span>
          </div>
        </template>
        <el-table
            v-loading="loading"
            :data="historyRecords"
            style="width: 100%"
            border>
          <el-table-column
              prop="date"
              label="上课日期"
              width="120">
          </el-table-column>
          <el-table-column
              label="上课时间"
              width="180">
            <template #default="scope">
              <div class="time-column">
                <el-icon><Clock /></el-icon>
                {{ scope.row.startTime }} - {{ scope.row.endTime }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
              prop="hours"
              label="课时数"
              width="80">
            <template #default="scope">
              {{ scope.row.hours }}
            </template>
          </el-table-column>
          <el-table-column
              label="状态"
              width="100">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
              label="课时报告">
            <template #default="scope">
              <el-button
                  :type="scope.row.hasReport ? 'success' : 'info'"
                  :disabled="!scope.row.hasReport || scope.row.status === 'absent'"
                  link
                  @click="$router.push(`/index/student/course-report/${scope.row.id}`)">
                {{ scope.row.hasReport ? '查看报告' : '暂无报告' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.course-hours-container {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
}

.course-list {
  margin-top: 20px;
}

.course-details {
  margin-top: 20px;
}

.details-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.details-header h2 {
  margin: 0 0 0 20px;
}

.course-stats {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 15px;
}

.stat-label {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.history-table {
  margin-top: 20px;
}

.time-column {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>