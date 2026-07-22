import { request } from '../utils/request';

export const getCircleList = () => {
  return request({
    url: '/kindergarten/class-circle/list',
    method: 'GET'
  });
};
