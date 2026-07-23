import request from '@/utils/request';

export function getRoleList() {
  return request.get('/system/role/list');
}

export function getMenuTree() {
  return request.get('/system/menu/tree');
}

export function addRole(data: any) {
  return request.post('/system/role', data);
}

export function saveRolePermissions(data: any) {
  return request.post('/system/role/permission', data);
}

