import { request } from '../utils/request';

export const getCircleList = () => {
  return request({
    url: '/kindergarten/class-circle/list',
    method: 'GET'
  });
};

export const publishCircle = (data) => {
  return request({
    url: '/kindergarten/class-circle/publish',
    method: 'POST',
    data
  });
};
