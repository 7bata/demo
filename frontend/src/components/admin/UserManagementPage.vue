<script setup>
import { ref, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption } from 'element-plus';

// 用户列表
const userList = ref([]);
// 当前激活的标签
const activeTab = ref('all');
// 编辑对话框可见性
const editDialogVisible = ref(false);
// 正在编辑的用户
const currentUser = ref({});
// 临时表单数据
const userForm = ref({
  id: '',
  username: '',
  name: '',
  role: '',
  status: '',
  password: ''
});
// 是否重置密码
const resetPassword = ref(false);

// 角色选项
const roleOptions = [
  { value: 'student', label: '学生' },
  { value: 'teacher', label: '教师' },
  { value: 'admin', label: '管理员' }
];

// 状态选项
const statusOptions = [
  { value: 'active', label: '正常' },
  { value: 'inactive', label: '禁用' }
];

// 初始化数据
onMounted(() => {
  // 模拟获取用户数据
  userList.value = [
    { id: 1, username: 'student1', name: '张三', role: 'student', roleName: '学生', status: 'active', statusName: '正常' },
    { id: 2, username: 'student2', name: '李四', role: 'student', roleName: '学生', status: 'active', statusName: '正常' },
    { id: 3, username: 'teacher1', name: '王老师', role: 'teacher', roleName: '教师', status: 'active', statusName: '正常' },
    { id: 4, username: 'teacher2', name: '赵老师', role: 'teacher', roleName: '教师', status: 'active', statusName: '正常' },
    { id: 5, username: 'admin1', name: '陈管理', role: 'admin', roleName: '管理员', status: 'active', statusName: '正常' },
    { id: 6, username: 'admin2', name: '周管理', role: 'admin', roleName: '管理员', status: 'inactive', statusName: '禁用' }
  ];
});

// 根据当前标签筛选用户
const filteredUsers = computed(() => {
  if (activeTab.value === 'all') {
    return userList.value;
  } else {
    return userList.value.filter(user => user.role === activeTab.value);
  }
});

// 打开编辑对话框
const handleEdit = (index, row) => {
  currentUser.value = row;
  userForm.value = {
    id: row.id,
    username: row.username,
    name: row.name,
    role: row.role,
    status: row.status,
    password: ''
  };
  resetPassword.value = false;
  editDialogVisible.value = true;
};

// 保存用户信息
const saveUser = () => {
  if (!userForm.value.username || !userForm.value.name) {
    ElMessage.warning('请填写完整信息');
    return;
  }
  
  // 找到并更新用户
  const userIndex = userList.value.findIndex(u => u.id === userForm.value.id);
  if (userIndex !== -1) {
    // 更新角色名称和状态名称
    const roleName = roleOptions.find(r => r.value === userForm.value.role)?.label || '';
    const statusName = statusOptions.find(s => s.value === userForm.value.status)?.label || '';
    
    // 更新用户信息
    userList.value[userIndex] = {
      ...userList.value[userIndex],
      username: userForm.value.username,
      name: userForm.value.name,
      role: userForm.value.role,
      roleName: roleName,
      status: userForm.value.status,
      statusName: statusName
    };
    
    // 显示密码重置信息
    if (resetPassword.value && userForm.value.password) {
      ElMessage.success(`已重置 ${userForm.value.name} 的密码`);
    } else {
      ElMessage.success(`已更新 ${userForm.value.name} 的信息`);
    }
    
    editDialogVisible.value = false;
  }
};

// 确认删除用户
const handleDelete = (index, row) => {
  ElMessageBox.confirm(
    `确定要删除用户 ${row.name} 吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(() => {
      // 过滤掉要删除的用户
      userList.value = userList.value.filter(user => user.id !== row.id);
      ElMessage.success(`已删除用户：${row.name}`);
    })
    .catch(() => {
      // 取消删除
    });
};

// 添加新用户
const addNewUser = () => {
  // 生成新的用户ID
  const newId = Math.max(...userList.value.map(u => u.id)) + 1;
  
  currentUser.value = null;
  userForm.value = {
    id: newId,
    username: '',
    name: '',
    role: 'student',
    status: 'active',
    password: ''
  };
  resetPassword.value = true;
  editDialogVisible.value = true;
};

// 保存新用户
const saveNewUser = () => {
  if (!userForm.value.username || !userForm.value.name || !userForm.value.password) {
    ElMessage.warning('请填写完整信息（包括密码）');
    return;
  }
  
  // 获取角色名称和状态名称
  const roleName = roleOptions.find(r => r.value === userForm.value.role)?.label || '';
  const statusName = statusOptions.find(s => s.value === userForm.value.status)?.label || '';
  
  // 添加新用户
  userList.value.push({
    id: userForm.value.id,
    username: userForm.value.username,
    name: userForm.value.name,
    role: userForm.value.role,
    roleName: roleName,
    status: userForm.value.status,
    statusName: statusName
  });
  
  ElMessage.success(`已添加新用户：${userForm.value.name}`);
  editDialogVisible.value = false;
};

// 保存用户处理
const handleSaveUser = () => {
  if (currentUser.value) {
    saveUser();
  } else {
    saveNewUser();
  }
};

// 切换密码重置
const toggleResetPassword = () => {
  resetPassword.value = !resetPassword.value;
  if (!resetPassword.value) {
    userForm.value.password = '';
  }
};
</script>

<template>
  <div class="user-management-container">
    <h1>用户管理</h1>
    
    <!-- 次级导航 -->
    <div class="sub-navigation">
      <el-menu
        :default-active="activeTab"
        mode="horizontal"
        @select="activeTab = $event"
        class="role-tabs"
      >
        <el-menu-item index="all">全部用户</el-menu-item>
        <el-menu-item index="student">学生管理</el-menu-item>
        <el-menu-item index="teacher">教师管理</el-menu-item>
        <el-menu-item index="admin">管理员管理</el-menu-item>
      </el-menu>
    </div>
    
    <div class="user-actions">
      <el-button type="primary" @click="addNewUser">新增用户</el-button>
    </div>
    
    <el-table :data="filteredUsers" style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="roleName" label="角色" />
      <el-table-column prop="statusName" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'">
            {{ scope.row.statusName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.$index, scope.row)">
            编辑
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 用户编辑对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="currentUser ? '编辑用户' : '新增用户'"
      width="500px"
    >
      <el-form :model="userForm" label-width="100px">
        <el-form-item label="用户名" required>
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        
        <el-form-item label="姓名" required>
          <el-input v-model="userForm.name" placeholder="请输入姓名" />
        </el-form-item>
        
        <el-form-item label="角色" required>
          <el-select v-model="userForm.role" placeholder="请选择角色" style="width: 100%;">
            <el-option
              v-for="item in roleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态">
          <el-select v-model="userForm.status" placeholder="请选择状态" style="width: 100%;">
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="resetPassword" @change="toggleResetPassword">
            {{ currentUser ? '重置密码' : '设置密码' }}
          </el-checkbox>
        </el-form-item>
        
        <el-form-item v-if="resetPassword" label="密码" required>
          <el-input
            v-model="userForm.password"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveUser">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-management-container {
  padding: 20px;
}

.sub-navigation {
  margin-top: 20px;
  margin-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.role-tabs {
  background-color: transparent;
}

.user-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>