import request from '@/utils/request'

export function getContactList() {
  return request.get('/api/kindergarten/contacts/list')
}

export function searchContacts(keyword: string) {
  return request.get('/api/kindergarten/contacts/search', { params: { keyword } })
}

export function virtualCall(contactId: number) {
  return request.post('/api/kindergarten/contacts/virtual-call', { contactId })
}
