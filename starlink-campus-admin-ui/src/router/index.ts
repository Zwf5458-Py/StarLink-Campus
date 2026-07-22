import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '@/views/DashboardView.vue'
import StudentManageView from '@/views/StudentManageView.vue'
import AttendanceManageView from '@/views/AttendanceManageView.vue'
import HealthMonitorView from '@/views/HealthMonitorView.vue'
import PatrolManageView from '@/views/PatrolManageView.vue'
import VisitorManageView from '@/views/VisitorManageView.vue'
import BigScreenView from '@/views/BigScreenView.vue'
import InterestClassManageView from '@/views/InterestClassManageView.vue'
import OaManageView from '@/views/OaManageView.vue'
import SystemManageView from '@/views/SystemManageView.vue'
import PortalManageView from '@/views/PortalManageView.vue'
import FamilyCooperationView from '@/views/FamilyCooperationView.vue'
import PermissionManageView from '@/views/PermissionManageView.vue'

const routes = [
  { path: '/', name: 'Dashboard', component: DashboardView },
  { path: '/permission', name: 'PermissionManage', component: PermissionManageView },
  { path: '/oa', name: 'OaManage', component: OaManageView },
  { path: '/system', name: 'SystemManage', component: SystemManageView },
  { path: '/portal', name: 'PortalManage', component: PortalManageView },
  { path: '/family', name: 'FamilyCooperation', component: FamilyCooperationView },
  { path: '/student', name: 'StudentManage', component: StudentManageView },
  { path: '/attendance', name: 'AttendanceManage', component: AttendanceManageView },
  { path: '/health', name: 'HealthMonitor', component: HealthMonitorView },
  { path: '/patrol', name: 'PatrolManage', component: PatrolManageView },
  { path: '/visitor', name: 'VisitorManage', component: VisitorManageView },
  { path: '/bigscreen', name: 'BigScreen', component: BigScreenView },
  { path: '/interest', name: 'InterestClassManage', component: InterestClassManageView },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
