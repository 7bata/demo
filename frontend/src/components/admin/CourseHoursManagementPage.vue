<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, View, Edit, Delete, Clock, User, Document } from '@element-plus/icons-vue';

const router = useRouter();
const courses = ref([]);
const loading = ref(false);
const searchQuery = ref('');

// 分页配置
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 获取所有课程数据
const fetchCourses = () => {
  loading.value = true;
  // 模拟API请求
  setTimeout(() => {
    const mockCourses = [
      {
        id: 1,
        name: '高等数学',
        teacher: '张教授',
        teacherId: 101,
        studentCount: 15,
        totalHours: 60,
        completedHours: 28
      },
      {
        id: 2,
        name: '大学英语',
        teacher: '李教授',
        teacherId: 102,
        studentCount: 12,
        totalHours: 45,
        completedHours: 20
      },
      {
        id: 3,
        name: '物理学',
        teacher: '王教授',
        teacherId: 103,
        studentCount: 8,
        totalHours: 40,
        completedHours: 35
      },
      {
        id: 4,
        name: '计算机编程',
        teacher: '赵教授',
        teacherId: 104,
        studentCount: 20,
        totalHours: 80,
        completedHours: 42
      },
      {
        id: 5,
        name: '数据结构',
        teacher: '刘教授',
        teacherId: 105,
        studentCount: 18,
        totalHours: 50,
        completedHours: 25
      }
    ];
    
    courses.value = mockCourses;
    pagination.value.total = mockCourses.length;
    loading.value = false;
  }, 500);
};

// 查看课程详情
const viewCourseDetail = (course) => {
  router.push({
    path: '/index/admin/course-detail',
    query: {
      courseId: course.id,
      courseName: course.name,
      teacherId: course.teacherId,
      teacherName: course.teacher
    }
  });
};

// 根据搜索条件过滤课程
const filteredCourses = computed(() => {
  if (!searchQuery.value) return courses.value;
  
  const query = searchQuery.value.toLowerCase();
  return courses.value.filter(course => 
    course.name.toLowerCase().includes(query) || 
    course.teacher.toLowerCase().includes(query)
  );
});

// 处理页码变化
const handleCurrentChange = (page) => {
  pagination.value.currentPage = page;
};

// 处理每页显示数量变化
const handleSizeChange = (size) => {
  pagination.value.pageSize = size;
  pagination.value.currentPage = 1;
};

// 页面加载时获取数据
onMounted(() => {
  fetchCourses();
});
</script>

<template>
  <div class="course-hours-management-container">
    <div class="page-header">
      <h2>课时管理</h2>
      <div class="search-box">
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索课程名称或教师" 
          prefix-icon="Search"
          clearable
        />
      </div>
    </div>
    
    <el-card class="course-list" shadow="never">
      <template #header>
        <div class="card-header">
          <span>课程列表</span>
        </div>
      </template>
      
      <el-table
        v-loading="loading"
        :data="filteredCourses"
        style="width: 100%"
        border
      >
        <el-table-column
          prop="name"
          label="课程名称"
          min-width="150"
        />
        <el-table-column
          prop="teacher"
          label="授课教师"
          min-width="150"
        />
        <el-table-column
          prop="studentCount"
          label="学生数量"
          width="100"
          align="center"
        />
        <el-table-column
          prop="totalHours"
          label="总课时"
          width="100"
          align="center"
        />
        <el-table-column
          prop="completedHours"
          label="已完成课时"
          width="100"
          align="center"
        />
        <el-table-column
          label="完成进度"
          width="180"
        >
          <template #default="scope">
            <el-progress 
              :percentage="Math.round((scope.row.completedHours / scope.row.totalHours) * 100)"
              :format="format => `${scope.row.completedHours}/${scope.row.totalHours}`"
            />
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="120"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="viewCourseDetail(scope.row)"
            >
              <el-icon><View /></el-icon>
              查看课程
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页器 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.course-hours-management-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.search-box {
  width: 300px;
}

.card-header {
  font-weight: bold;
}

.course-list {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 