import request from '@/utils/request'
export function getMorningSummary(date: string) { return request.get('/kindergarten/health/morning-summary', { params: { date } }) }
export function addMorningCheck(data: any) { return request.post('/kindergarten/health/morning-check', data) }
export function checkRecipeAllergies(ingredients: string[]) { return request.post('/kindergarten/health/check-recipe', { ingredients }) }
