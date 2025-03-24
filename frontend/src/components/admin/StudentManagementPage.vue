<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Edit, Delete, Plus, Search, Refresh } from '@element-plus/icons-vue';

const students = ref([]);
const loading = ref(false);
const searchQuery = ref('');

// 分页
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 加载学生数据
const fetchStudents = () => {
  loading.value = true;
  // 模拟API请求
  setTimeout(() => {
    students.value = [
      { id: 1, name: '张三', gender: '男', age: 16, grade: '高二', contact: '13800138001', parentName: '张父', parentContact: '13900139001', courses: '数学、英语', hours: { total: 60, completed: 28, remaining: 32 } },
      { id: 2, name: '李四', gender: '男', age: 17, grade: '高三', contact: '13800138002', parentName: '李父', parentContact: '13900139002', courses: '数学、物理', hours: { total: 80, completed: 45, remaining: 35 } },
      { id: 3, name: '王五', gender: '女', age: 15, grade: '高一', contact: '13800138003', parentName: '王母', parentContact: '13900139003', courses: '语文、英语', hours: { total: 50, completed: 20, remaining: 30 } },
      { id: 4, name: '赵六', gender: '男', age: 16, grade: '高二', contact: '13800138004', parentName: '赵父', parentContact: '13900139004', courses: '物理、化学', hours: { total: 40, completed: 25, remaining: 15 } },
      { id: 5, name: '钱七', gender: '女', age: 17, grade: '高三', contact: '13800138005', parentName: '钱母', parentContact: '13900139005', courses: '数学、英语、物理', hours: { total: 70, completed: 50, remaining: 20 } },
      { id: 6, name: '孙八', gender: '男', age: 16, grade: '高二', contact: '13800138006', parentName: '孙父', parentContact: '13900139006', courses: '语文、历史', hours: { total: 60, completed: 30, remaining: 30 } },
      { id: 7, name: '周九', gender: '女', age: 15, grade: '高一', contact: '13800138007', parentName: '周母', parentContact: '13900139007', courses: '数学、地理', hours: { total: 40, completed: 20, remaining: 20 } },
      { id: 8, name: '吴十', gender: '男', age: 17, grade: '高三', contact: '13800138008', parentName: '吴父', parentContact: '13900139008', courses: '物理、化学、生物', hours: { total: 90, completed: 60, remaining: 30 } },
      { id: 9, name: '郑十一', gender: '女', age: 16, grade: '高二', contact: '13800138009', parentName: '郑母', parentContact: '13900139009', courses: '数学、英语', hours: { total: 60, completed: 40, remaining: 20 } },
      { id: 10, name: '王十二', gender: '男', age: 15, grade: '高一', contact: '13800138010', parentName: '王父', parentContact: '13900139010', courses: '语文、历史、政治', hours: { total: 70, completed: 35, remaining: 35 } },
    ];
    total.value = 50; // 模拟总数
    loading.value = false;
  }, 300);
};

// 添加学生
const addStudent = () => {
  ElMessage.info('添加学生功能待实现');
};

// 编辑学生
const editStudent = (row) => {
  ElMessage.info(`编辑学生 ${row.name} 功能待实现`);
};

// 删除学生
const deleteStudent = (row) => {
  ElMessageBox.confirm(
    `确定要删除学生 ${row.name} 吗?`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      ElMessage({
        type: 'success',
        message: `${row.name}已成功删除`,
      });
      fetchStudents();
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消删除',
      });
    });
};

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchStudents();
};

// 处理分页变化
const handleCurrentChange = (val) => {
  currentPage.value = val;
  fetchStudents();
};

// 处理每页数量变化
const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1;
  fetchStudents();
};

// 刷新数据
const refreshData = () => {
  fetchStudents();
};

// 初始化
onMounted(() => {
  fetchStudents();
});
</script>

<template>
  <div class="student-management-container">
    <h2 class="page-title">学生管理</h2>
    
    <div class="action-bar">
      <div class="left-actions">
        <el-button type="primary" @click="addStudent">
          <el-icon><Plus /></el-icon>
          添加学生
        </el-button>
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
      
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索学生姓名、联系方式或课程"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #suffix>
            <el-icon @click="handleSearch"><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>
    
    <!-- 学生表格 -->
    <el-table
      :data="students"
      v-loading="loading"
      border
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="70" fixed />
      <el-table-column prop="name" label="姓名" width="100" fixed />
      <el-table-column prop="gender" label="性别" width="70" />
      <el-table-column prop="age" label="年龄" width="70" />
      <el-table-column prop="grade" label="年级" width="80" />
      <el-table-column prop="contact" label="联系电话" width="130" />
      <el-table-column prop="parentName" label="家长姓名" width="100" />
      <el-table-column prop="parentContact" label="家长电话" width="130" />
      <el-table-column prop="courses" label="所学课程" min-width="150" />
      <el-table-column label="课时情况" width="220">
        <template #default="scope">
          <div class="hours-info">
            <div class="hours-text">
              总课时: {{ scope.row.hours.total }} / 
              已完成: {{ scope.row.hours.completed }} / 
              剩余: {{ scope.row.hours.remaining }}
            </div>
            <el-progress 
              :percentage="Math.round((scope.row.hours.completed / scope.row.hours.total) * 100)"
              :format="format => `${Math.round((scope.row.hours.completed / scope.row.hours.total) * 100)}%`"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="editStudent(scope.row)">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button size="small" type="danger" @click="deleteStudent(scope.row)">
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<style scoped>
.student-management-container {
  padding: 20px;
}

.page-title {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 24px;
  color: #303133;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.left-actions {
  display: flex;
  gap: 10px;
}

.search-bar {
  width: 300px;
}

.hours-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.hours-text {
  font-size: 12px;
  color: #606266;
  white-space: nowrap;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 