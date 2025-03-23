<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const courseList = ref([]);

onMounted(() => {
  // 模拟获取课程数据
  courseList.value = [
    { id: 1, name: '高等数学', students: 30, status: '进行中' },
    { id: 2, name: '大学英语', students: 25, status: '进行中' },
    { id: 3, name: '大学物理', students: 28, status: '未开始' }
  ];
});

const handleEdit = (index, row) => {
  ElMessage.info(`编辑课程：${row.name}`);
};

const handleDelete = (index, row) => {
  ElMessage.warning(`删除课程：${row.name}`);
};
</script>

<template>
  <div class="course-management-container">
    <h1>课程管理</h1>
    
    <div class="course-actions">
      <el-button type="primary">新增课程</el-button>
    </div>
    
    <el-table :data="courseList" style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="课程名称" />
      <el-table-column prop="students" label="学生数量" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.course-management-container {
  padding: 20px;
}

.course-actions {
  margin-top: 20px;
}
</style>