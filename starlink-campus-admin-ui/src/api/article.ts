import request from '@/utils/request';

export function getArticleList() {
  return request.get('/kindergarten/article/list');
}

export function addArticle(data: any) {
  return request.post('/kindergarten/article/add', data);
}
