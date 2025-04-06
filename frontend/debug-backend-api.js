/**
 * 后端API调试文件
 * 这是一个模拟后端API的调试文件，用于排查前端和后端交互问题
 */

import axios from 'axios';

// 配置基础URL
const API_BASE_URL = 'http://localhost:8080';

// 模拟课时记录数据
const mockClassHourRecord = {
  id: null,
  studentId: 2,
  courseId: 1,
  teacherId: 3,
  classDate: new Date().toISOString().split('T')[0],
  theme: '测试课时主题',
  content: '这是测试课时内容',
  status: 'completed',
  learningProgress: '学习进度良好',
  homework: '完成练习1-10',
  nextClassGoal: '学习下一章节',
  startTime: '08:00',
  endTime: '09:40'
};

/**
 * 测试获取课时记录
 */
async function testGetStudentCourseRecords() {
  console.log('------- 测试获取课时记录 -------');
  
  try {
    const studentId = 2;
    const courseId = 1;
    
    const url = `${API_BASE_URL}/api/teacher/student-course-records?studentId=${studentId}&courseId=${courseId}`;
    console.log('请求URL:', url);
    
    const response = await axios.get(url, {
      withCredentials: true
    });
    
    console.log('响应状态:', response.status);
    console.log('响应数据:', JSON.stringify(response.data, null, 2));
    
    // 分析响应结构
    if (response.data.success) {
      console.log('成功响应，检查message字段');
      console.log('message类型:', typeof response.data.message);
      console.log('message是否为数组:', Array.isArray(response.data.message));
      console.log('message内容:', response.data.message);
    } else {
      console.log('响应不成功，检查错误信息');
    }
  } catch (error) {
    console.error('API调用错误:', error.message);
    
    if (error.response) {
      console.error('错误状态:', error.response.status);
      console.error('错误数据:', error.response.data);
    }
  }
}

/**
 * 测试保存课时记录
 */
async function testSaveClassHourRecord() {
  console.log('------- 测试保存课时记录 -------');
  
  try {
    const url = `${API_BASE_URL}/api/teacher/class-hour-record`;
    console.log('请求URL:', url);
    console.log('请求数据:', JSON.stringify(mockClassHourRecord, null, 2));
    
    const response = await axios.post(url, mockClassHourRecord, {
      headers: {
        'Content-Type': 'application/json'
      },
      withCredentials: true
    });
    
    console.log('响应状态:', response.status);
    console.log('响应数据:', JSON.stringify(response.data, null, 2));
    
    // 保存成功后测试获取
    if (response.data.success) {
      console.log('保存成功，现在测试获取记录');
      await testGetStudentCourseRecords();
    }
  } catch (error) {
    console.error('API调用错误:', error.message);
    
    if (error.response) {
      console.error('错误状态:', error.response.status);
      console.error('错误数据:', error.response.data);
    }
  }
}

/**
 * 分析后端API返回格式
 * 对于熟悉的几个API进行测试，以确定其返回格式
 */
async function analyzeApiResponses() {
  console.log('===== 分析后端API返回格式 =====');
  
  const apis = [
    { name: '获取课程详情', url: `${API_BASE_URL}/api/teacher/course/1` },
    { name: '获取学生课时记录', url: `${API_BASE_URL}/api/teacher/student-course-records?studentId=2&courseId=1` },
    { name: '获取教师课程列表', url: `${API_BASE_URL}/api/teacher/courses?teacherId=3` },
    { name: '获取课程学生列表', url: `${API_BASE_URL}/api/teacher/course-students/1` }
  ];
  
  for (const api of apis) {
    console.log(`\n测试API: ${api.name} - ${api.url}`);
    
    try {
      const response = await axios.get(api.url, { withCredentials: true });
      console.log('响应状态:', response.status);
      console.log('响应结构:', Object.keys(response.data));
      
      // 检查响应格式
      if (response.data.success !== undefined) {
        console.log('- RestBean格式 (包含success字段)');
        
        if (response.data.message !== undefined) {
          console.log('- 数据在message字段中');
          console.log('- message类型:', typeof response.data.message);
          console.log('- message是否为数组:', Array.isArray(response.data.message));
        }
        
        if (response.data.data !== undefined) {
          console.log('- 数据在data字段中');
          console.log('- data类型:', typeof response.data.data);
          console.log('- data是否为数组:', Array.isArray(response.data.data));
        }
      } else {
        console.log('- 非RestBean格式');
        console.log('- 响应类型:', typeof response.data);
        console.log('- 响应是否为数组:', Array.isArray(response.data));
      }
    } catch (error) {
      console.error('API调用错误:', error.message);
      
      if (error.response) {
        console.error('错误状态:', error.response.status);
        if (error.response.data) {
          console.error('错误数据结构:', Object.keys(error.response.data));
        }
      }
    }
  }
}

// 选择测试函数
// analyzeApiResponses();
// testGetStudentCourseRecords();
// testSaveClassHourRecord();

// 导出测试函数以便在控制台调用
export {
  testGetStudentCourseRecords,
  testSaveClassHourRecord,
  analyzeApiResponses
}; 