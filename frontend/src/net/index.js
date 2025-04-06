import axios from 'axios';
import {ElMessage} from "element-plus";
import Qs from "qs";
import { useRouter } from 'vue-router';

const authItemName = "access_token"

// 默认从网络API请求的配置
const defaultFailure = (message) => {
  console.error(`请求失败: ${message}`);
  ElMessage.error(`请求失败: ${message}`);
}

/**
 * 修复中文乱码问题
 * @param {string} encodedText - 可能包含乱码的文本
 * @returns {string} 修复后的文本
 */
const fixChineseEncoding = (encodedText) => {
  if (!encodedText || typeof encodedText !== 'string') {
    return encodedText;
  }
  
  // 检查是否包含乱码特征
  if (!encodedText.includes('ç') && !encodedText.includes('æ')) {
    return encodedText;
  }
  
  // 已知乱码映射
  const knownEncodingIssues = {
    'ç"¨æˆ·åæˆ–å¯†ç é"™è¯¯': '用户名或密码错误',
    'ç"¨æˆ·åä¸å­˜åœ¨': '用户名不存在',
    'å¯†ç é"™è¯¯': '密码错误',
    'è´¦å·å·²é"': '账号已锁',
    'æ"ä½œæˆåŠŸ': '操作成功',
    'æœªç™»å½"': '未登录',
    'ç»å½"å·²è¿‡æœŸ': '登录已过期'
  };
  
  // 检查是否匹配已知乱码
  return knownEncodingIssues[encodedText] || encodedText;
}

/**
 * 修复响应对象中的乱码
 * @param {Object} response - 响应对象
 * @returns {Object} 修复后的响应对象
 */
const fixResponseEncoding = (response) => {
  if (!response) return response;
  
  // 创建新对象避免修改原始对象
  const result = { ...response };
  
  // 修复message字段
  if (result.message && typeof result.message === 'string') {
    result.message = fixChineseEncoding(result.message);
  }
  
  // 修复data字段
  if (result.data && typeof result.data === 'object' && !Array.isArray(result.data)) {
    result.data = fixResponseEncoding(result.data);
  }
  
  return result;
}

// 处理API返回的数据结构
const handleResponse = (response, success, failure) => {
  // 对不同的响应结构进行处理
  if (response.data) {
    // 修复可能的编码问题
    const fixedData = fixResponseEncoding(response.data);
    
    // 标准的响应结构 {status, success, message, ...}
    if (fixedData.status !== undefined) {
      if (fixedData.success) {
        success(fixedData.message, fixedData.status);
      } else {
        failure(fixedData.message, fixedData.status);
      }
      return;
    }
    
    // 直接返回数据
    success(fixedData);
  } else {
    failure("未接收到有效的响应数据");
  }
}

// 处理特殊状态或错误
const handleError = (error, failure) => {
  console.error('请求错误:', error);
  if (error.response) {
    if (error.response.status === 401) {
      // 处理401错误并尝试解码错误消息
      let errorMsg = '登录状态已过期，请重新登录';
      
      // 尝试获取后端返回的错误消息
      if (error.response.data && error.response.data.message) {
        try {
          // 如果消息是乱码，可能是编码问题，尝试使用原始内容
          const message = error.response.data.message;
          console.log('原始错误消息:', message);
          
          // 检查消息是否为乱码
          if (/^[\u4e00-\u9fa5]+$/.test(message)) {
            // 是中文，直接使用
            errorMsg = message;
          } else if (message.includes('ç') || message.includes('æ')) {
            // 可能是编码问题的乱码，使用固定消息
            errorMsg = '用户名或密码错误';
          } else {
            // 其他情况使用原始消息
            errorMsg = message;
          }
        } catch (e) {
          console.error('处理错误消息时出错:', e);
          // 出错时使用默认消息
          errorMsg = '登录失败，请检查用户名和密码';
        }
      }
      
      ElMessage.error(errorMsg);
      localStorage.removeItem(authItemName);
      // 跳转到登录页面
      setTimeout(() => {
        window.location.href = "/";
      }, 1000);
      return;
    }
    
    // 处理其他错误
    let errorMsg = '请求发生错误';
    if (error.response.data && error.response.data.message) {
      try {
        const message = error.response.data.message;
        // 检查消息是否为乱码
        if (/^[\u4e00-\u9fa5]+$/.test(message)) {
          // 是中文，直接使用
          errorMsg = message;
        } else if (message.includes('ç') || message.includes('æ')) {
          // 可能是编码问题，转换为通用错误
          errorMsg = '请求失败，请稍后再试';
        } else {
          // 其他情况使用原始消息
          errorMsg = message;
        }
      } catch (e) {
        console.error('处理错误消息时出错:', e);
      }
    }
    
    failure(errorMsg);
  } else if (error.message) {
    failure(error.message);
  } else {
    failure('请求失败，请稍后再试，或联系管理员！');
  }
}

function internalPost(url, data, header, success, failure) {
  axios.post(url, data, { headers: header }).then(response => {
    handleResponse(response, success, failure);
  }).catch(error => {
    handleError(error, failure);
  })
}

function internalGet(url, header, success, failure) {
  axios.get(url, { headers: header }).then(response => {
    handleResponse(response, success, failure);
  }).catch(error => {
    handleError(error, failure);
  })
}

// GET请求调用
export function get(url, success, fail = defaultFailure) {
  const accessToken = localStorage.getItem(authItemName)
  axios.get(url, { 
    headers: accessToken ? {
      'Authorization': `Bearer ${accessToken}`
    } : {},
    withCredentials: true
  }).then(({data}) => {
    console.log(`GET请求 ${url} 响应:`, data);
    
    if (data.status && data.status === 200) {
      // 标准RestBean格式，优先使用data字段
      if (data.data !== undefined) {
        success(data.data);
      } 
      // 如果没有data字段，尝试使用message字段
      else if (data.message !== undefined) {
        success(data.message);
      } 
      // 返回整个响应
      else {
        success(data);
      }
    } else if (data.success) {
      // 特殊RestBean格式，优先使用data字段
      if (data.data !== undefined) {
        success(data.data);
      } 
      // 如果没有data字段，尝试使用message字段
      else if (data.message !== undefined) {
        success(data.message);
      } 
      // 返回整个响应
      else {
        success(data);
      }
    } else if (data.status && data.status === 401) {
      // 未登录，返回登录页面
      ElMessage.error('未登录或登录状态已过期，请重新登录');
      const router = useRouter();
      router.push('/');
    } else {
      // 返回完整响应，保留status和message
      success(data);
    }
  }).catch(err => {
    // 记录请求错误
    console.error(`请求 ${url} 发生错误:`, err);
    
    // 如果有响应数据
    if (err.response && err.response.data) {
      const errMsg = err.response.data.message || err.response.data.error || err.message || '未知错误';
      fail(errMsg);
    } else {
      fail(err.message);
    }
  })
}

// POST请求调用
export function post(url, data, success, fail = defaultFailure, type = 'json') {
  const contentType = type === 'form' ? 
    'application/x-www-form-urlencoded' : 
    'application/json';
  
  // 根据类型处理数据
  const postData = type === 'form' ? 
    Qs.stringify(data) : 
    data;
  
  console.log(`POST请求 ${url} 发送数据:`, postData);
  
  axios.post(url, postData, {
    headers: {'Content-Type': contentType},
    withCredentials: true
  }).then(({data}) => {
    console.log(`POST请求 ${url} 响应:`, data);
    
    if (data.status && data.status === 200) {
      // 标准RestBean格式，优先使用data字段
      if (data.data !== undefined) {
        success(data.data);
      } 
      // 如果没有data字段，尝试使用message字段
      else if (data.message !== undefined) {
        success(data.message);
      } 
      // 返回整个响应
      else {
        success(data);
      }
    } else if (data.success) {
      // 特殊RestBean格式，优先使用data字段
      if (data.data !== undefined) {
        success(data.data);
      } 
      // 如果没有data字段，尝试使用message字段
      else if (data.message !== undefined) {
        success(data.message);
      } 
      // 返回整个响应
      else {
        success(data);
      }
    } else if (data.status && data.status === 401) {
      // 未登录，返回登录页面
      ElMessage.error('未登录或登录状态已过期，请重新登录');
      const router = useRouter();
      router.push('/');
    } else {
      // 返回完整响应，保留status和message
      success(data);
    }
  }).catch(err => {
    // 记录请求错误
    console.error(`请求 ${url} 发生错误:`, err);
    
    // 如果有响应数据
    if (err.response && err.response.data) {
      const errMsg = err.response.data.message || err.response.data.error || err.message || '未知错误';
      fail(errMsg);
    } else {
      fail(err.message);
    }
  })
}

// PUT请求调用
export function put(url, data, success, fail = defaultFailure, type = 'json') {
  const contentType = type === 'form' ? 
    'application/x-www-form-urlencoded' : 
    'application/json';
  
  // 根据类型处理数据
  const putData = type === 'form' ? 
    Qs.stringify(data) : 
    data;
  
  console.log(`PUT请求 ${url} 发送数据:`, putData);
  
  axios.put(url, putData, {
    headers: {'Content-Type': contentType},
    withCredentials: true
  }).then(({data}) => {
    console.log(`PUT请求 ${url} 响应:`, data);
    
    if (data.status && data.status === 200) {
      // 标准RestBean格式，优先使用data字段
      if (data.data !== undefined) {
        success(data.data);
      } 
      // 如果没有data字段，尝试使用message字段
      else if (data.message !== undefined) {
        success(data.message);
      } 
      // 返回整个响应
      else {
        success(data);
      }
    } else if (data.success) {
      // 特殊RestBean格式，优先使用data字段
      if (data.data !== undefined) {
        success(data.data);
      } 
      // 如果没有data字段，尝试使用message字段
      else if (data.message !== undefined) {
        success(data.message);
      } 
      // 返回整个响应
      else {
        success(data);
      }
    } else if (data.status && data.status === 401) {
      // 未登录，返回登录页面
      ElMessage.error('未登录或登录状态已过期，请重新登录');
      const router = useRouter();
      router.push('/');
    } else {
      // 返回完整响应，保留status和message
      success(data);
    }
  }).catch(err => {
    // 记录请求错误
    console.error(`请求 ${url} 发生错误:`, err);
    
    // 如果有响应数据
    if (err.response && err.response.data) {
      const errMsg = err.response.data.message || err.response.data.error || err.message || '未知错误';
      fail(errMsg);
    } else {
      fail(err.message);
    }
  })
}

// 登录请求
export function login(username, password, success, failure = defaultFailure) {
  // 准备登录数据
  const loginData = {
    username: username,
    password: password
  };
  
  console.log('准备登录数据:', loginData);
  
  axios.post('/api/auth/login', Qs.stringify(loginData), {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    withCredentials: true
  }).then(({data}) => {
    console.log('登录响应:', data);
    
    // 修复可能的编码问题
    const fixedData = fixResponseEncoding(data);
    console.log('修复编码后的登录响应:', fixedData);
    
    if (fixedData.success) {
      // 登录成功，处理token等
      if (fixedData.data) {
        localStorage.setItem(authItemName, fixedData.data);
      }
      success(fixedData.message || '登录成功');
    } else {
      // 登录失败
      failure(fixedData.message || '登录失败');
    }
  }).catch(error => {
    console.error('登录请求错误:', error);
    
    // 处理特定错误
    if (error.response && error.response.status === 401) {
      // 401表示认证失败，通常是用户名或密码错误
      let errorMsg = '用户名或密码错误';
      
      // 尝试从响应中获取更具体的错误信息
      if (error.response.data && error.response.data.message) {
        const fixedMsg = fixChineseEncoding(error.response.data.message);
        errorMsg = fixedMsg;
      }
      
      failure(errorMsg);
    } else {
      // 其他错误
      let errorMsg = '登录失败，请稍后再试';
      if (error.response && error.response.data && error.response.data.message) {
        const fixedMsg = fixChineseEncoding(error.response.data.message);
        errorMsg = fixedMsg;
      }
      failure(errorMsg);
    }
  });
}