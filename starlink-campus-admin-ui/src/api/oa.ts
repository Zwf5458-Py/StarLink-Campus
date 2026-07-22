import request from '@/utils/request'

export function getOaList(params?: any) {
  return request.get('/kindergarten/oa/list', { params })
}

export function approveOa(id: number) {
  return request.post(`/kindergarten/oa/approve/${id}`)
}

export function rejectOa(id: number) {
  return request.post(`/kindergarten/oa/reject/${id}`)
}
