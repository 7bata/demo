<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const userList = ref([]);

onMounted(() => {
  // 模拟获取用户数据
  userList.value = [
    { id: 1, username: 'student1', name: '张三', role: '学生', status: '正常' },
    { id: 2, username: 'teacher1', name: '李老师', role: '教师', status: '正常' },
    { id: 3, username: 'admin1', name: '王管理', role: '管理员', status: '正常' }
  ];
});

const handleEdit = (index, row) => {
  ElMessage.info(`编辑用户：${row.name}`);
};

const handleDelete = (index, row) => {
  ElMessage.warning(`删除用户：${row.name}`);
};

const handleResetPassword = (index, row) => {
  ElMessage.success(`重置密码：${row.name}`);
};
</script>

<template>
  <div class="user-management-container">
    <h1>用户管理</h1>
    
    <div class="user-actions">
      <el-button type="primary">新增用户</el-button>
    </div>
    
    <el-table :data="userList" style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="role" label="角色" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="300">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="small" type="warning" @click="handleResetPassword(scope.$index, scope.row)">重置密码</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.user-management-container {
  padding: 20px;
}

.user-actions {
  margin-top: 20px;
}
</style>