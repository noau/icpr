import httpInstance from '@/utils/http.js';

// 发送消息
export const chatCompletions = (data) => {
    return httpInstance.post('/api/v1/chat/completions', data);
  };
  