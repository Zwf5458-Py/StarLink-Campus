import { request } from '../utils/request';

export const getArticleList = () => {
  return request({
    url: '/kindergarten/article/list',
    method: 'GET'
  });
};
