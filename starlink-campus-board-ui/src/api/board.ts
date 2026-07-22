import request from '@/utils/request';

export function getBoardConfig(roomNumber: string) {
  return request.get(`/kindergarten/board/config/${roomNumber}`);
}

export function checkIn(studentId: number, type: string, temperature: string) {
  return request.post('/kindergarten/attendance/check-in', null, {
    params: { studentId, type, temperature }
  });
}
