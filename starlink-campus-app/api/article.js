import { request } from '../utils/request';

export const getArticleList = () => {
  return request({
    url: '/kindergarten/article/list',
    method: 'GET'
  });
};

export const approveArticle = (id) => {
  return request({
    url: `/kindergarten/article/approve/${id}`,
    method: 'POST'
  });
};
