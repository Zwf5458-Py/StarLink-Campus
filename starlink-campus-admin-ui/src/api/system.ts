import request from '@/utils/request';

export function getRoleList() {
  return request.get('/system/role/list');
}

export function getMenuTree() {
  return request.get('/system/menu/tree');
}
