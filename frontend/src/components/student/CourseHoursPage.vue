<script setup>
import { ref, onMounted } from 'vue'
import { post } from '@/net'
import { ElMessage } from 'element-plus'

// 课时统计数据
const courseHoursStats = ref({
  remainingHours: 0,
  consumedHours: 0
})

// 历史记录数据
const historyRecords = ref([])
const loading = ref(false)

// 分页配置
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 获取课时统计数据
const fetchCourseHoursStats = () => {
  post('/api/student/course-hours/stats', (data) => {
    courseHoursStats.value = data
  }, {}, (message) => {
    ElMessage.error(message)
  })
}

// 获取历史记录数据
const fetchHistoryRecords = () => {
  loading.value = true
  post('/api/student/course-hours/history', (data) => {
    historyRecords.value = data.records
    pagination.value.total = data.total
    loading.value = false
  }, {
    page: pagination.value.currentPage,
    size: pagination.value.pageSize
  }, (message) => {
    ElMessage.error(message)
    loading.value = false
  })
}

// 处理页码变化
const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  fetchHistoryRecords()
}

// 处理每页显示数量变化
const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  fetchHistoryRecords()
}

// 页面加载时获取数据
onMounted(() => {
  fetchCourseHoursStats()
  fetchHistoryRecords()
})
</script>

<template>
  <div class="course-hours-container">
    <!-- 课时统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>剩余课时</span>
            </div>
          </template>
          <div class="card-value">
            {{ courseHoursStats.remainingHours }} 小时
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>已消耗课时</span>
            </div>
          </template>
          <div class="card-value">
            {{ courseHoursStats.consumedHours }} 小时
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 历史记录表格 -->
    <el-card class="history-table" shadow="never">
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
            label="上课时间"
            width="180">
        </el-table-column>
        <el-table-column
            prop="subject"
            label="科目"
            width="120">
        </el-table-column>
        <el-table-column
            prop="teacher"
            label="教师"
            width="120">
        </el-table-column>
        <el-table-column
            prop="hours"
            label="课时数"
            width="100">
        </el-table-column>
        <el-table-column
            prop="report"
            label="课时报告">
          <template #default="scope">
            <el-button
                link
                type="primary"
                @click="$router.push(`/student/course-report/${scope.row.id}`)">
              查看报告
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="pagination.currentPage"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 30, 50]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange">
        </el-pagination>
      </div>
    </el-card>
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

.history-table {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>