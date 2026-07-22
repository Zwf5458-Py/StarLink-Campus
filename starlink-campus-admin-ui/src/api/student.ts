import request from '@/utils/request'

export function listStudents(params?: any) {
  return request.get('/kindergarten/student/list', { params })
}

export function addStudent(data: any) {
  return request.post('/kindergarten/student/add', data)
}

export function updateStudent(data: any) {
  return request.put('/kindergarten/student/update', data)
}

export function deleteStudent(id: number) {
  return request.delete(`/kindergarten/student/delete/${id}`)
}

export function syncFaceLibrary() {
  return request.post('/kindergarten/student/sync-face-library')
}
