const BASE_URL = 'http://localhost:8080/api';

export const request = (options) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Authorization': uni.getStorageSync('token') ? 'Bearer ' + uni.getStorageSync('token') : '',
        ...options.header
      },
      success: (res) => {
        if (res.data.code && res.data.code !== 200) {
          uni.showToast({ title: res.data.message || 'Error', icon: 'none' });
          reject(res.data);
        } else {
          resolve(res.data);
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络连接失败', icon: 'none' });
        reject(err);
      }
    });
  });
};
