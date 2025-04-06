/**
 * 编码调试工具
 * 用于调试和修复后端响应中的中文编码问题
 */

import axios from 'axios';

/**
 * 测试解码乱码中文
 * @param {string} encodedText - 乱码文本
 */
function decodeChineseText(encodedText) {
  console.log('原始乱码文本:', encodedText);
  
  // 尝试多种解码方式
  try {
    // 尝试URI解码
    const decodedURI = decodeURIComponent(escape(encodedText));
    console.log('URI解码结果:', decodedURI);
  } catch (e) {
    console.error('URI解码失败:', e);
  }
  
  try {
    // 尝试使用TextDecoder
    const textDecoder = new TextDecoder('utf-8');
    const uint8Array = new TextEncoder().encode(encodedText);
    const decoded = textDecoder.decode(uint8Array);
    console.log('TextDecoder解码结果:', decoded);
  } catch (e) {
    console.error('TextDecoder解码失败:', e);
  }
  
  // 已知乱码映射
  const knownEncodingIssues = {
    'ç"¨æˆ·åæˆ–å¯†ç é"™è¯¯': '用户名或密码错误',
    'ç"¨æˆ·åä¸å­˜åœ¨': '用户名不存在',
    'å¯†ç é"™è¯¯': '密码错误',
    'è´¦å·å·²é"': '账号已锁',
    'æ"ä½œæˆåŠŸ': '操作成功'
  };
  
  // 检查是否匹配已知乱码
  if (knownEncodingIssues[encodedText]) {
    console.log('通过已知映射解码:', knownEncodingIssues[encodedText]);
    return knownEncodingIssues[encodedText];
  }
  
  return '无法解码，请联系管理员';
}

/**
 * 修复API响应中的中文乱码
 * @param {Object} response - API响应对象
 * @returns {Object} 修复后的响应对象
 */
function fixResponseEncoding(response) {
  console.log('修复前的响应:', response);
  
  if (!response) return response;
  
  // 创建新的响应对象，避免修改原始对象
  const fixedResponse = { ...response };
  
  // 检查并修复message字段
  if (fixedResponse.message && typeof fixedResponse.message === 'string') {
    // 检查message是否包含常见的乱码特征
    if (fixedResponse.message.includes('ç') || fixedResponse.message.includes('æ')) {
      // 尝试解码或使用已知映射
      fixedResponse.message = decodeChineseText(fixedResponse.message);
    }
  }
  
  // 修复嵌套对象
  if (fixedResponse.data && typeof fixedResponse.data === 'object') {
    fixedResponse.data = fixResponseEncoding(fixedResponse.data);
  }
  
  console.log('修复后的响应:', fixedResponse);
  return fixedResponse;
}

/**
 * 测试登录请求
 * @param {string} username - 用户名
 * @param {string} password - 密码
 */
async function testLogin(username, password) {
  try {
    const response = await axios.post('/api/auth/login', {
      username,
      password
    }, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });
    
    console.log('原始登录响应:', response.data);
    
    // 修复响应中的编码问题
    const fixedResponse = fixResponseEncoding(response.data);
    console.log('修复后的登录响应:', fixedResponse);
    
    return fixedResponse;
  } catch (error) {
    console.error('登录请求错误:', error);
    
    if (error.response) {
      console.error('错误状态:', error.response.status);
      console.error('错误响应:', error.response.data);
      
      // 修复错误响应中的编码问题
      const fixedErrorResponse = fixResponseEncoding(error.response.data);
      console.log('修复后的错误响应:', fixedErrorResponse);
      
      return fixedErrorResponse;
    }
    
    return { success: false, message: '请求失败，无法连接到服务器' };
  }
}

// 导出函数供其他模块使用
export {
  decodeChineseText,
  fixResponseEncoding,
  testLogin
}; 