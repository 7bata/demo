<script setup>
import { ref, onMounted, computed } from 'vue'
import { post } from '@/net'
import { ElMessage } from 'element-plus'

// 视图类型：周视图或月视图
const viewType = ref('week') // 'week' 或 'month'

// 当前选中的日期
const currentDate = ref(new Date())

// 课程数据
const courseData = ref([])
const loading = ref(false)

// 计算当前视图的日期范围
const dateRange = computed(() => {
  const dates = []
  const date = new Date(currentDate.value)
  
  if (viewType.value === 'week') {
    // 获取本周的起始日期（周一）
    const day = date.getDay() || 7 // 将周日的0转换为7
    date.setDate(date.getDate() - day + 1) // 设置为本周一
    
    // 生成一周的日期
    for (let i = 0; i < 7; i++) {
      const d = new Date(date)
      d.setDate(date.getDate() + i)
      dates.push(d)
    }
  } else { // 月视图
    // 设置为当月1号
    date.setDate(1)
    
    // 获取当月的天数
    const monthDays = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate()
    
    // 获取1号是星期几
    const firstDayWeek = date.getDay() || 7
    
    // 日历通常从周一开始，计算需要显示的上个月的天数
    const prevMonthDays = firstDayWeek - 1
    
    // 上个月的日期
    const prevMonth = new Date(date)
    prevMonth.setMonth(prevMonth.getMonth() - 1)
    const prevMonthLastDay = new Date(date.getFullYear(), date.getMonth(), 0).getDate()
    
    for (let i = 0; i < prevMonthDays; i++) {
      const d = new Date(prevMonth)
      d.setDate(prevMonthLastDay - prevMonthDays + i + 1)
      dates.push(d)
    }
    
    // 当月的日期
    for (let i = 0; i < monthDays; i++) {
      const d = new Date(date)
      d.setDate(i + 1)
      dates.push(d)
    }
    
    // 补全日历到6行，添加下个月的日期
    const totalDays = 42 // 6行7列
    const nextMonthDays = totalDays - prevMonthDays - monthDays
    
    const nextMonth = new Date(date)
    nextMonth.setMonth(nextMonth.getMonth() + 1)
    
    for (let i = 0; i < nextMonthDays; i++) {
      const d = new Date(nextMonth)
      d.setDate(i + 1)
      dates.push(d)
    }
  }
  
  return dates
})

// 获取课程数据
const fetchCourseData = () => {
  loading.value = true
  
  // 计算日期范围
  const startDate = dateRange.value[0]
  const endDate = dateRange.value[dateRange.value.length - 1]
  
  // 格式化日期为 YYYY-MM-DD
  const formatDate = (date) => {
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }
  
  post('/api/student/course-schedule', (data) => {
    courseData.value = data
    loading.value = false
  }, {
    startDate: formatDate(startDate),
    endDate: formatDate(endDate),
    viewType: viewType.value
  }, (message) => {
    ElMessage.error(message)
    loading.value = false
  })
}

// 切换视图类型
const switchViewType = (type) => {
  viewType.value = type
  fetchCourseData()
}

// 上一周/月
const goToPrevious = () => {
  const date = new Date(currentDate.value)
  if (viewType.value === 'week') {
    date.setDate(date.getDate() - 7)
  } else {
    date.setMonth(date.getMonth() - 1)
  }
  currentDate.value = date
  fetchCourseData()
}

// 下一周/月
const goToNext = () => {
  const date = new Date(currentDate.value)
  if (viewType.value === 'week') {
    date.setDate(date.getDate() + 7)
  } else {
    date.setMonth(date.getMonth() + 1)
  }
  currentDate.value = date
  fetchCourseData()
}

// 返回今天
const goToToday = () => {
  currentDate.value = new Date()
  fetchCourseData()
}

// 获取指定日期的课程
const getCoursesForDate = (date) => {
  const dateStr = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
  return courseData.value.filter(course => course.date === dateStr)
}

// 判断日期是否是当前月份
const isCurrentMonth = (date) => {
  return date.getMonth() === currentDate.value.getMonth()
}

// 判断日期是否是今天
const isToday = (date) => {
  const today = new Date()
  return date.getDate() === today.getDate() && 
         date.getMonth() === today.getMonth() && 
         date.getFullYear() === today.getFullYear()
}

// 查看课程详情
const viewCourseDetail = (course) => {
  // 这里可以实现查看课程详情的逻辑，比如打开对话框显示详情
  ElMessage.info(`查看课程：${course.name}，教师：${course.teacher}`)
}

onMounted(() => {
  fetchCourseData()
})
</script>

<template>
  <div class="course-schedule-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="title">课程表</div>
          <div class="controls">
            <el-radio-group v-model="viewType" @change="switchViewType">
              <el-radio-button label="week">周视图</el-radio-button>
              <el-radio-button label="month">月视图</el-radio-button>
            </el-radio-group>
            
            <el-button-group class="ml-4">
              <el-button @click="goToPrevious">
                <el-icon><ArrowLeft /></el-icon>
                {{ viewType === 'week' ? '上一周' : '上个月' }}
              </el-button>
              <el-button @click="goToToday">今天</el-button>
              <el-button @click="goToNext">
                {{ viewType === 'week' ? '下一周' : '下个月' }}
                <el-icon><ArrowRight /></el-icon>
              </el-button>
            </el-button-group>
          </div>
        </div>
      </template>
      
      <div v-loading="loading">
        <!-- 周视图 -->
        <div v-if="viewType === 'week'" class="week-view">
          <div class="week-header">
            <div v-for="(date, index) in dateRange" :key="index" class="day-header" :class="{ 'today': isToday(date) }">
              <div class="weekday">{{ ['周一', '周二', '周三', '周四', '周五', '周六', '周日'][index] }}</div>
              <div class="date">{{ date.getMonth() + 1 }}/{{ date.getDate() }}</div>
            </div>
          </div>
          
          <div class="week-body">
            <div v-for="(date, index) in dateRange" :key="index" class="day-column" :class="{ 'today': isToday(date) }">
              <div v-for="course in getCoursesForDate(date)" :key="course.id" 
                   class="course-item" :class="`course-type-${course.type}`"
                   @click="viewCourseDetail(course)">
                <div class="course-time">{{ course.startTime }} - {{ course.endTime }}</div>
                <div class="course-name">{{ course.name }}</div>
                <div class="course-teacher">{{ course.teacher }}</div>
                <div class="course-location">{{ course.location }}</div>
              </div>
              
              <div v-if="getCoursesForDate(date).length === 0" class="no-course">
                无课程
              </div>
            </div>
          </div>
        </div>
        
        <!-- 月视图 -->
        <div v-else class="month-view">
          <div class="month-header">
            <div v-for="day in ['一', '二', '三', '四', '五', '六', '日']" :key="day" class="weekday-header">
              周{{ day }}
            </div>
          </div>
          
          <div class="month-body">
            <div v-for="(date, index) in dateRange" :key="index" 
                 class="day-cell" 
                 :class="{ 
                   'today': isToday(date), 
                   'other-month': !isCurrentMonth(date) 
                 }">
              <div class="day-number">{{ date.getDate() }}</div>
              
              <div class="day-courses">
                <div v-for="course in getCoursesForDate(date)" :key="course.id" 
                     class="course-dot" :class="`course-type-${course.type}`"
                     @click="viewCourseDetail(course)">
                  {{ course.name }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.course-schedule-container {
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
  align-items: center;
}

.ml-4 {
  margin-left: 16px;
}

/* 周视图样式 */
.week-view {
  display: flex;
  flex-direction: column;
  height: 600px;
}

.week-header {
  display: flex;
  border-bottom: 1px solid #ebeef5;
}

.day-header {
  flex: 1;
  text-align: center;
  padding: 10px;
  border-right: 1px solid #ebeef5;
}


.day-header:last-child {
  border-right: none;
}

.weekday {
  font-weight: bold;
}

.date {
  color: #606266;
  margin-top: 5px;
}

.week-body {
  display: flex;
  flex: 1;
  overflow-y: auto;
}

.day-column {
  flex: 1;
  padding: 10px;
  border-right: 1px solid #ebeef5;
  min-height: 540px;
}

.day-column:last-child {
  border-right: none;
}

.course-item {
  background-color: #f0f9ff;
  border-left: 4px solid #409eff;
  border-radius: 4px;
  padding: 8px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.course-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.course-time {
  font-size: 12px;
  color: #606266;
}

.course-name {
  font-weight: bold;
  margin: 5px 0;
}

.course-teacher, .course-location {
  font-size: 12px;
  color: #909399;
}

.no-course {
  text-align: center;
  color: #909399;
  padding: 20px 0;
}

/* 月视图样式 */
.month-view {
  display: flex;
  flex-direction: column;
}

.month-header {
  display: flex;
}

.weekday-header {
  flex: 1;
  text-align: center;
  padding: 10px;
  font-weight: bold;
  background-color: #f5f7fa;
}

.month-body {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-template-rows: repeat(6, 1fr);
  height: 600px;
}

.day-cell {
  border: 1px solid #ebeef5;
  padding: 5px;
  min-height: 100px;
}

.day-number {
  font-weight: bold;
  margin-bottom: 5px;
}

.day-courses {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.course-dot {
  font-size: 12px;
  padding: 2px 4px;
  border-radius: 2px;
  background-color: #f0f9ff;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

.other-month {
  opacity: 0.5;
}

.today {
  background-color: #f2f6fc;
}

/* 课程类型样式 */
.course-type-1 {
  border-left-color: #409eff;
  background-color: #f0f9ff;
}

.course-type-2 {
  border-left-color: #67c23a;
  background-color: #f0f9eb;
}

.course-type-3 {
  border-left-color: #e6a23c;
  background-color: #fdf6ec;
}

.course-type-4 {
  border-left-color: #f56c6c;
  background-color: #fef0f0;
}

.course-type-5 {
  border-left-color: #909399;
  background-color: #f4f4f5;
}
</style>