import axios from 'axios';
import mockData from './index';

// 创建一个axios拦截器，用于拦截API请求并返回mock数据
const setupMockAdapter = () => {
  // 请求拦截器
  axios.interceptors.request.use(config => {
    return config;
  }, error => {
    return Promise.reject(error);
  });

  // 响应拦截器
  axios.interceptors.response.use(response => {
    return response;
  }, error => {
    // 获取请求的URL和方法
    const { config } = error;
    if (!config) return Promise.reject(error);
    
    const { url, method, data } = config;
    
    // 根据URL返回对应的mock数据
    if (url.includes('/api/student/course-hours/stats')) {
      return Promise.resolve({
        data: {
          success: true,
          message: mockData.courseHoursStats,
          status: 200
        }
      });
    }
    
    if (url.includes('/api/student/course-hours/history')) {
      return Promise.resolve({
        data: {
          success: true,
          message: mockData.courseHoursHistory,
          status: 200
        }
      });
    }
    
    if (url.includes('/api/student/course-schedule')) {
      return Promise.resolve({
        data: {
          success: true,
          message: mockData.courseSchedule,
          status: 200
        }
      });
    }
    
    // 如果没有匹配的mock数据，则返回原始错误
    return Promise.reject(error);
  });
};

export default setupMockAdapter;