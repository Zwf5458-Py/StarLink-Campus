import request from '@/utils/request'

export function getPatrolList(params?: any) {
  return request.get('/kindergarten/patrol/list', { params })
}

export function submitPatrol(data: any) {
  return request.post('/kindergarten/patrol/submit', data)
}

export function getRepairOrders(params?: any) {
  return request.get('/kindergarten/patrol/repair-orders', { params })
}
