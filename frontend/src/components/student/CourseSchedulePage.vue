<script setup>
import { ref, onMounted, computed } from 'vue'
import { post } from '@/net'
import { ElMessage } from 'element-plus'
import { Calendar, ArrowLeft, ArrowRight, Search } from '@element-plus/icons-vue'

// 视图类型：周视图或月视图
const viewType = ref('week') // 'week' 或 'month'

// 当前选中的日期
const currentDate = ref(new Date())

// 课程数据
const courseData = ref([])
const loading = ref(false)

// 日期选择器
const datePickerVisible = ref(false)
const selectedDate = ref('')

// 获取今日日期显示格式
const todayDateString = computed(() => {
  const today = new Date()
  const year = today.getFullYear()
  const month = String(today.getMonth() + 1).padStart(2, '0')
  const day = String(today.getDate()).padStart(2, '0')
  return `${year}年${month}月${day}日 ${['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][today.getDay()]}`
})

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
  
  // 从sessionStorage获取学生ID
  const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}')
  const studentId = userInfo.id
  
  if (!studentId) {
    ElMessage.error('未找到学生信息，请重新登录')
    loading.value = false
    return
  }
  
  console.log("获取学生课程表，学生ID:", studentId)
  
  post('/api/student/course-schedule', (data) => {
    console.log("获取课程表成功:", data)
    courseData.value = data
    loading.value = false
  }, {
    studentId: studentId
  }, (message) => {
    console.error("获取课程表失败:", message)
    ElMessage.error('获取课程表失败: ' + message)
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

// 跳转到指定日期
const goToSelectedDate = () => {
  if (selectedDate.value) {
    currentDate.value = new Date(selectedDate.value)
    fetchCourseData()
    datePickerVisible.value = false
  }
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
          <div class="title-section">
            <div class="title">课程表</div>
            <div class="today-date">
              <el-tag type="info" effect="plain" size="large">
                今天: {{ todayDateString }}
              </el-tag>
            </div>
          </div>
          <div class="controls">
            <el-button 
              type="primary" 
              plain 
              @click="datePickerVisible = true"
              class="date-picker-btn"
            >
              <el-icon><Calendar /></el-icon>
              跳转日期
            </el-button>
            
            <el-radio-group v-model="viewType" @change="switchViewType" class="ml-4">
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
      
      <!-- 日期选择对话框 -->
      <el-dialog v-model="datePickerVisible" title="跳转到指定日期" width="350px">
        <el-date-picker
          v-model="selectedDate"
          type="date"
          placeholder="选择日期"
          style="width: 100%"
          format="YYYY/MM/DD"
          value-format="YYYY-MM-DD"
        />
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="datePickerVisible = false">取消</el-button>
            <el-button type="primary" @click="goToSelectedDate">确定</el-button>
          </span>
        </template>
      </el-dialog>
      
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
  flex-wrap: wrap;
  gap: 10px;
}

.title-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.today-date {
  margin-left: 12px;
}

.controls {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.date-picker-btn {
  display: flex;
  align-items: center;
  gap: 5px;
}

.ml-4 {
  margin-left: 8px;
}

/* 周视图样式 */
.week-view {
  display: flex;
  flex-direction: column;
  height: 600px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.week-header {
  display: flex;
  border-bottom: 1px solid #ebeef5;
  background-color: #f5f7fa;
}

.day-header {
  flex: 1;
  text-align: center;
  padding: 15px 10px;
  border-right: 1px solid #ebeef5;
  transition: background-color 0.3s;
}

.day-header:hover {
  background-color: #ecf5ff;
}

.day-header:last-child {
  border-right: none;
}

.weekday {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 5px;
}

.date {
  color: #606266;
  font-size: 14px;
}

.week-body {
  display: flex;
  flex: 1;
  overflow-y: auto;
  background-color: #ffffff;
}

.day-column {
  flex: 1;
  padding: 15px;
  border-right: 1px solid #ebeef5;
  min-height: 540px;
  transition: background-color 0.3s;
}

.day-column:hover {
  background-color: #fafafa;
}

.day-column:last-child {
  border-right: none;
}

.course-item {
  background-color: #f0f9ff;
  border-left: 4px solid #409eff;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
}

.course-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0) 100%);
  pointer-events: none;
}

.course-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.course-time {
  font-size: 12px;
  color: #606266;
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.course-time::before {
  content: '⏰';
  margin-right: 5px;
  font-size: 14px;
}

.course-name {
  font-weight: bold;
  font-size: 16px;
  margin: 8px 0;
  color: #303133;
}

.course-teacher, .course-location {
  font-size: 13px;
  color: #606266;
  margin-top: 6px;
  display: flex;
  align-items: center;
}

.course-teacher::before {
  content: '👨‍🏫';
  margin-right: 5px;
}

.course-location::before {
  content: '📍';
  margin-right: 5px;
}

.no-course {
  text-align: center;
  color: #909399;
  padding: 40px 0;
  font-size: 14px;
  font-style: italic;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-top: 20px;
  border: 1px dashed #e0e0e0;
}

/* 月视图样式 */
.month-view {
  display: flex;
  flex-direction: column;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.month-header {
  display: flex;
  background-color: #f5f7fa;
}

.weekday-header {
  flex: 1;
  text-align: center;
  padding: 12px;
  font-weight: bold;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
  border-right: 1px solid #ebeef5;
  color: #303133;
  font-size: 15px;
}

.weekday-header:last-child {
  border-right: none;
}

.month-body {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-template-rows: repeat(6, 1fr);
  height: 600px;
  background-color: #ffffff;
}

.day-cell {
  border: 1px solid #ebeef5;
  padding: 10px;
  min-height: 100px;
  transition: all 0.3s;
  position: relative;
}

.day-cell:hover {
  background-color: #f9fafc;
  z-index: 1;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
}

.day-number {
  font-weight: 500;
  margin-bottom: 8px;
  text-align: center;
  background-color: #f5f7fa;
  padding: 3px 8px;
  border-radius: 15px;
  font-size: 14px;
  display: inline-block;
}

.day-courses {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.course-dot {
  font-size: 12px;
  padding: 6px 8px;
  border-radius: 4px;
  background-color: #f0f9ff;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.course-dot:hover {
  transform: translateX(3px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.other-month {
  opacity: 0.5;
  background-color: #fafafa;
}

.today {
  background-color: #ecf5ff;
  position: relative;
}

.today::after {
  content: '今日';
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: #409eff;
  color: white;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 10px;
  font-weight: bold;
}

/* 课程类型样式 */
.course-type-1 {
  border-left-color: #409eff;
  background-color: #ecf5ff;
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

/* 日期切换控件样式 */
.el-button-group .el-button {
  transition: all 0.3s;
}

.el-button-group .el-button:hover {
  transform: scale(1.05);
  z-index: 2;
}

/* 加载动画 */
.el-loading-spinner .path {
  stroke: #409eff;
  stroke-width: 4;
}
</style>