import request from '@/utils/request';

export function getCircleList() {
  return request.get('/kindergarten/class-circle/list');
}

export function publishCircle(data) {
  return request.post('/kindergarten/class-circle/add', data);
}

export function likeCircle(id) {
  return request.post(`/kindergarten/class-circle/like/${id}`);
}
