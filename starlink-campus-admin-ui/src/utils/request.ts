import axios from 'axios';

const request = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  timeout: 5000,
});

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== undefined && res.code !== 200) {
      return Promise.reject(new Error(res.message || '业务响应异常'));
    }
    return res;
  },
  error => {
    // 捕获网络错误/未连接后端服务，交给组件做离线保底回显，不弹出阻塞性报错
    console.warn('[API 服务连接状态]', error.message);
    return Promise.reject(error);
  }
);

export default request;
