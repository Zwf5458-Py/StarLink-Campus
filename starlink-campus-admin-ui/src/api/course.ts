import request from '@/utils/request'

export function getCourseList(params?: any) {
  return request.get('/kindergarten/course/list', { params })
}

export function addCourse(data: any) {
  return request.post('/kindergarten/course/add', data)
}

export function deleteCourse(id: number) {
  return request.delete(`/kindergarten/course/delete/${id}`)
}
