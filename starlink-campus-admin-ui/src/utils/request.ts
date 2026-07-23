import axios from 'axios';

const request = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  timeout: 5000,
});

// 自动保底登录机制，保证演示和后台管理页面始终拥有 valid Sa-Token
async function ensureToken(forceRefresh = false) {
  if (forceRefresh) {
    localStorage.removeItem('token');
  }
  let token = localStorage.getItem('token');
  if (!token) {
    try {
      const loginRes = await axios.post((import.meta.env.VITE_API_URL || '/api') + '/auth/login', {
        username: 'admin',
        password: 'admin123'
      });
      if (loginRes.data && loginRes.data.code === 200 && loginRes.data.data) {
        token = loginRes.data.data;
        localStorage.setItem('token', token);
      }
    } catch (e) {
      console.warn('自动获取 Token 失败', e);
    }
  }
  return token;
}

request.interceptors.request.use(
  async config => {
    let token = localStorage.getItem('token');
    if (!token) {
      token = await ensureToken();
    }
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
      config.headers['satoken'] = token;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  async response => {
    const res = response.data;
    if (res.code === 401) {
      // 401 时自动重新登录并无缝重试原请求
      const newToken = await ensureToken(true);
      if (newToken && response.config) {
        response.config.headers['Authorization'] = `Bearer ${newToken}`;
        response.config.headers['satoken'] = newToken;
        return request(response.config);
      }
    }
    if (res.code !== undefined && res.code !== 200) {
      return Promise.reject(new Error(res.message || '业务响应异常'));
    }
    return res;
  },
  error => {
    console.warn('[API 服务连接状态]', error.message);
    return Promise.reject(error);
  }
);

export default request;
