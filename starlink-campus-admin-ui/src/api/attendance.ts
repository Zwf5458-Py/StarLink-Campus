import request from '@/utils/request'
export function getAttendanceList(params?: any) { return request.get('/kindergarten/attendance/list', { params }) }
export function checkIn(data: any) { return request.post('/kindergarten/attendance/check-in', data) }
export function calculateRefund(studentId: number, month: string) { return request.get(`/kindergarten/attendance/calculate-refund/${studentId}`, { params: { month } }) }
export function getClassSummary(classId: number, date: string) { return request.get('/kindergarten/attendance/class-summary', { params: { classId, date } }) }
