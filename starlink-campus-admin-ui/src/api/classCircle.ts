import request from '@/utils/request';

export function getCircleList() {
  return request.get('/kindergarten/circle/list');
}

export function publishCircle(data) {
  return request.post('/kindergarten/circle/post', data);
}

export function likeCircle(id) {
  return request.post(`/kindergarten/circle/like/${id}`);
}
