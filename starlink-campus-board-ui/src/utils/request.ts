import axios from 'axios';

const request = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  timeout: 8000,
});

request.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== undefined && res.code !== 200) {
      return Promise.reject(new Error(res.message || 'Error'));
    }
    return res;
  },
  error => {
    return Promise.reject(error);
  }
);

export default request;
