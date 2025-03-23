<script setup>
import { ref, onMounted, computed } from 'vue'
import { post } from '@/net'
import { ElMessage } from 'element-plus'
import { Document, ArrowRight, Clock } from '@element-plus/icons-vue'

// 课时统计数据
const courseHoursStats = ref({
  remainingHours: 0,
  consumedHours: 0
})

// 课程分类数据
const courseCategories = ref([])
const loading = ref(false)

// 历史记录详情数据
const historyRecords = ref([])
const detailsVisible = ref(false)
const currentCourse = ref({})

// 获取课时统计数据
const fetchCourseHoursStats = () => {
  post('/api/student/course-hours/stats', (data) => {
    courseHoursStats.value = data
  }, {}, (message) => {
    ElMessage.error(message)
  })
}

// 获取课程分类数据
const fetchCourseCategories = () => {
  loading.value = true
  // 模拟API请求
  setTimeout(() => {
    courseCategories.value = [
      {
        id: 1,
        name: '高等数学',
        teacher: '张教授',
        totalHours: 40,
        consumedHours: 28,
        remainingHours: 12
      },
      {
        id: 2,
        name: '大学英语',
        teacher: '李教授',
        totalHours: 30,
        consumedHours: 15,
        remainingHours: 15
      },
      {
        id: 3,
        name: '物理学',
        teacher: '王教授',
        totalHours: 20,
        consumedHours: 20,
        remainingHours: 0
      },
      {
        id: 4,
        name: '计算机编程',
        teacher: '赵教授',
        totalHours: 60,
        consumedHours: 42,
        remainingHours: 18
      }
    ]
    loading.value = false
  }, 500)
}

// 查看课程详情
const viewCourseDetails = (course) => {
  currentCourse.value = course
  fetchCourseHistoryRecords(course.id)
  detailsVisible.value = true
}

// 获取课程历史记录
const fetchCourseHistoryRecords = (courseId) => {
  loading.value = true
  // 模拟API请求
  setTimeout(() => {
    historyRecords.value = [
      {
        id: 1,
        date: '2023-10-15',
        startTime: '08:00',
        endTime: '09:40',
        subject: currentCourse.value.name,
        teacher: currentCourse.value.teacher,
        hours: 2,
        hasReport: true
      },
      {
        id: 2,
        date: '2023-10-18',
        startTime: '10:00',
        endTime: '11:40',
        subject: currentCourse.value.name,
        teacher: currentCourse.value.teacher,
        hours: 2,
        hasReport: true
      },
      {
        id: 3,
        date: '2023-10-22',
        startTime: '14:00',
        endTime: '15:40',
        subject: currentCourse.value.name,
        teacher: currentCourse.value.teacher,
        hours: 2,
        hasReport: false
      }
    ]
    loading.value = false
  }, 500)
}

// 返回课程列表
const backToCourseList = () => {
  detailsVisible.value = false
}

// 总课时统计
const totalStats = computed(() => {
  let total = 0
  let consumed = 0
  let remaining = 0
  
  courseCategories.value.forEach(course => {
    total += course.totalHours
    consumed += course.consumedHours
    remaining += course.remainingHours
  })
  
  return {
    total,
    consumed,
    remaining
  }
})

// 页面加载时获取数据
onMounted(() => {
  fetchCourseHoursStats()
  fetchCourseCategories()
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
          :data="courseCategories"
          style="width: 100%"
          border>
        <el-table-column
            prop="name"
            label="课程名称"
            width="180">
        </el-table-column>
        <el-table-column
            prop="teacher"
            label="教师名称"
            width="150">
        </el-table-column>
        <el-table-column
            prop="totalHours"
            label="总课时"
            width="120">
          <template #default="scope">
            {{ scope.row.totalHours }}
          </template>
        </el-table-column>
        <el-table-column
            prop="consumedHours"
            label="已消耗"
            width="120">
          <template #default="scope">
            {{ scope.row.consumedHours }}
          </template>
        </el-table-column>
        <el-table-column
            prop="remainingHours"
            label="剩余"
            width="120">
          <template #default="scope">
            {{ scope.row.remainingHours }}
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
        <h2>{{ currentCourse.name }} - {{ currentCourse.teacher }}</h2>
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
              width="100">
            <template #default="scope">
              {{ scope.row.hours }}
            </template>
          </el-table-column>
          <el-table-column
              label="课时报告">
            <template #default="scope">
              <el-button
                  :type="scope.row.hasReport ? 'success' : 'info'"
                  :disabled="!scope.row.hasReport"
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