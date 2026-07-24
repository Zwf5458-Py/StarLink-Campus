import { request } from '../utils/request';

export const getCircleList = () => {
  return request({
    url: '/kindergarten/circle/list',
    method: 'GET'
  });
};

export const publishCircle = (data) => {
  return request({
    url: '/kindergarten/circle/post',
    method: 'POST',
    data
  });
};
