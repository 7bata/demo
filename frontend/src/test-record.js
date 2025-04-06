const axios = require('axios');

// 测试数据
const testRecord = {
  studentId: 2,
  courseId: 1,
  classDate: '2023-09-20',
  startTime: '08:00',
  endTime: '09:40',
  theme: '测试课时主题',
  content: '这是一个测试的课时内容',
  status: 'completed',
  learningProgress: '学习进度良好',
  homework: '复习今天的内容',
  nextClassGoal: '下节课目标是学习新概念',
  teacherId: 3
};

console.log('开始测试创建课时记录...');
console.log('发送数据:', JSON.stringify(testRecord, null, 2));

// 测试创建课时记录
axios.post('http://localhost:8080/api/teacher/class-hour-record', testRecord, {
  headers: {
    'Content-Type': 'application/json'
  }
})
.then(response => {
  console.log('创建课时记录成功！');
  console.log('响应状态:', response.status);
  console.log('响应数据:', JSON.stringify(response.data, null, 2));
  
  if (response.data && response.data.data && response.data.data.id) {
    const recordId = response.data.data.id;
    console.log(`新创建的记录ID: ${recordId}`);
    
    // 测试查询课时记录
    console.log('\n开始测试查询课时记录...');
    return axios.get(`http://localhost:8080/api/teacher/student-course-records?studentId=${testRecord.studentId}&courseId=${testRecord.courseId}`, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }
})
.then(response => {
  if (response) {
    console.log('查询课时记录成功！');
    console.log('响应状态:', response.status);
    console.log('响应数据:', JSON.stringify(response.data, null, 2));
  }
})
.catch(error => {
  console.error('测试失败:', error.message);
  if (error.response) {
    console.error('响应状态:', error.response.status);
    console.error('响应数据:', JSON.stringify(error.response.data, null, 2));
  }
}); 