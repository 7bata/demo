<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
import { get } from '@/net';

const router = useRouter();
const courseList = ref([]);
const loading = ref(true);

onMounted(() => {
  fetchCourses();
});

// 获取教师课程列表
const fetchCourses = () => {
  // 从session中获取用户ID
  const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
  const teacherId = userInfo.id;
  
  if (!teacherId) {
    ElMessage.error('未找到教师信息，请重新登录');
    loading.value = false;
    return;
  }
  
  loading.value = true;
  get(`/api/teacher/courses?teacherId=${teacherId}`, (data) => {
    console.log("获取课程列表成功:", data);
    
    // 检查数据是否嵌套在message中（根据API返回格式调整）
    const courseData = Array.isArray(data) ? data : (data.message || []);
    
    // 处理课程数据
    courseList.value = [];
    
    // 按课程分组
    const courseGroups = {};
    courseData.forEach(course => {
      // 获取正确的ID字段
      const courseId = course.courseId || course.id;
      
      // 如果还没有这个课程ID的组，创建一个
      if (!courseGroups[courseId]) {
        courseGroups[courseId] = {
          id: courseId,
          name: course.courseName,
          students: [],
          totalHours: course.totalHours || 40,
          completedHours: course.completedHours || 0
        };
      }
      
      // 添加学生到组
      const studentId = course.studentId;
      const studentName = course.studentName;
      
      // 排除admin用户和姓名为空的情况
      if (studentId && studentName && studentName !== 'admin') {
        // 检查是否已存在相同的学生
        const existingStudent = courseGroups[courseId].students.find(
          s => s.id === studentId
        );
        
        if (!existingStudent) {
          courseGroups[courseId].students.push({
            id: studentId,
            name: studentName,
            completedHours: course.completedHours || 0
          });
        }
      }
    });
    
    // 将分组后的数据转换为列表
    for (const courseId in courseGroups) {
      const group = courseGroups[courseId];
      
      if (group.students.length > 0) {
        // 多个学生的情况，为每个学生创建一条记录
        group.students.forEach(student => {
          // 获取学生课程完成情况
          get(`/api/teacher/student-course-records?studentId=${student.id}&courseId=${group.id}`, (hoursData) => {
            // 处理API响应
            console.log(`获取学生${student.id}课程${group.id}完成情况:`, hoursData);
            
            // 从响应中提取数据
            let records = [];
            if (hoursData.success && Array.isArray(hoursData.message)) {
              records = hoursData.message;
            } else if (hoursData.success && Array.isArray(hoursData.data)) {
              records = hoursData.data;
            } else if (Array.isArray(hoursData)) {
              records = hoursData;
            }
            
            // 计算已完成课时
            let completedHours = 0;
            records.forEach(record => {
              if (record.status === 'completed') {
                completedHours += 2; // 假设每节课是2课时
              }
            });
            
            // 创建或更新课程记录
            const courseRecord = {
              id: group.id,
              name: group.name,
              studentId: student.id,
              studentName: student.name,
              status: '进行中',
              totalHours: group.totalHours || 40,
              completedHours: completedHours || student.completedHours || 0
            };
            
            // 添加或更新列表中的记录
            const existingIndex = courseList.value.findIndex(
              c => c.id === courseRecord.id && c.studentId === courseRecord.studentId
            );
            
            if (existingIndex >= 0) {
              courseList.value[existingIndex] = courseRecord;
            } else {
              courseList.value.push(courseRecord);
            }
          }, (error) => {
            console.error(`获取学生${student.id}课程${group.id}完成情况失败:`, error);
            
            // 如果获取失败，仍使用默认值添加记录
            courseList.value.push({
              id: group.id,
              name: group.name,
              studentId: student.id,
              studentName: student.name,
              status: '进行中',
              totalHours: group.totalHours || 40,
              completedHours: 0
            });
          });
        });
      } else {
        // 没有学生的课程，尝试获取学生列表
        get(`/api/teacher/course-students/${group.id}`, (students) => {
          const studentList = Array.isArray(students) ? students : (students.message || []);
          
          // 过滤掉admin用户
          const filteredStudents = studentList.filter(
            s => s.studentName && s.studentName !== 'admin' && s.studentId
          );
          
          if (filteredStudents.length > 0) {
            // 如果找到学生，为每个学生创建记录
            filteredStudents.forEach(student => {
              // 检查是否已存在相同的记录
              const existingCourse = courseList.value.find(
                c => c.id === group.id && c.studentId === student.studentId
              );
              
              if (!existingCourse) {
                courseList.value.push({
                  id: group.id,
                  name: group.name,
                  studentId: student.studentId,
                  studentName: student.studentName,
                  status: '进行中',
                  totalHours: group.totalHours || 40,
                  completedHours: 0
                });
              }
            });
          } else {
            console.log(`跳过课程: ${group.name}，因为没有关联的学生`);
          }
        }, () => {
          // 如果API调用失败，不添加此课程
          console.log(`跳过课程: ${group.name}，因为获取学生列表失败`);
        });
      }
    }
    
    console.log("处理后的课程列表:", courseList.value);
    loading.value = false;
  }, (message) => {
    console.error("获取课程列表失败:", message);
    ElMessage.error('获取课程列表失败: ' + message);
    loading.value = false;
  });
};

// 查看课时信息
const viewCourseHours = (course) => {
  console.log("查看课时信息:", course);
  
  // 检查是否有学生ID
  if (!course.studentId) {
    // 学生ID为空的情况，显示提示
    ElMessage.warning('此课程没有关联学生，无法查看课时信息');
    return;
  }
  
  router.push({
    path: '/index/teacher/student-course-detail',
    query: { 
      courseId: course.id, 
      studentId: course.studentId,
      studentName: course.studentName 
    }
  });
};

// 生成课时报告
const generateReport = (course) => {
  ElMessageBox.confirm(
    `确定要为${course.studentName}的${course.name}课程生成并导出课时报告吗？`,
    '生成并导出课时报告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }
  )
    .then(() => {
      ElMessage.success(`已为${course.studentName}的${course.name}课程生成并导出课时报告`);
    })
    .catch(() => {
      // 取消生成
    });
};
</script>

<template>
  <div class="course-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span class="title">课程管理</span>
          <div class="controls">
            <el-button type="primary" @click="router.push('/index/teacher/course-schedule')">查看课程表</el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="courseList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="课程名称" />
        <el-table-column prop="studentName" label="学生姓名" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === '进行中' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="课时进度">
          <template #default="scope">
            <div>{{ scope.row.completedHours }}/{{ scope.row.totalHours }}</div>
            <el-progress 
              :percentage="Math.round((scope.row.completedHours / scope.row.totalHours) * 100)" 
              :status="scope.row.completedHours >= scope.row.totalHours ? 'success' : ''"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="viewCourseHours(scope.row)"
            >
              查看课时信息
            </el-button>
            <el-button 
              size="small" 
              type="warning" 
              @click="generateReport(scope.row)"
            >
              生成并导出学习报告
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.course-management-container {
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
  gap: 10px;
}
</style>