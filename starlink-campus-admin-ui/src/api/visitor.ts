import request from '@/utils/request'

export function getVisitorList(params?: any) {
  return request.get('/kindergarten/visitor/list', { params })
}

export function createVisitorPass(data: any) {
  return request.post('/kindergarten/visitor/create-pass', data)
}

export function verifyPass(passCode: string) {
  return request.post(`/kindergarten/visitor/verify-pass`, null, { params: { passCode } })
}

export function checkOvertime() {
  return request.get('/kindergarten/visitor/check-overtime')
}
