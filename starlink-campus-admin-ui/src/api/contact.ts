import request from '@/utils/request'

export function getContactList() {
  return request.get('/kindergarten/contact/list')
}

export function searchContacts(keyword: string) {
  return request.get('/kindergarten/contact/search', { params: { keyword } })
}

export function virtualCall(contactId: number) {
  return request.post('/kindergarten/contact/virtual-call', { contactId })
}
