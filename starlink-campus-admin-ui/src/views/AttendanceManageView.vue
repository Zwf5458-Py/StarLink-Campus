<template>
  <div class="attendance-container">
    <el-card header="📅 考勤与退费管理中心">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <!-- 幼儿考勤 Tab -->
        <el-tab-pane label="👶 幼儿考勤与退费" name="student">
          <div v-loading="loading">
            <el-alert title="根据规章制度：幼儿月累计缺勤天数 > 5 天，按 20 元/天 自动计算退还伙食费。" type="info" show-icon :closable="false" style="margin-bottom: 16px;" />
            <div style="display: flex; justify-content: flex-end; margin-bottom: 12px;">
              <el-button type="success" @click="handleExport">📥 导出考勤报表 Excel</el-button>
            </div>
            <el-table :data="attendanceList" border style="width: 100%">
              <el-table-column prop="studentId" label="学号" width="100" />
              <el-table-column prop="studentName" label="幼儿姓名" width="130" />
              <el-table-column prop="className" label="班级" width="160" />
              <el-table-column prop="attendanceDate" label="考勤日期" width="130" />
              <el-table-column prop="checkInTime" label="打卡时间" width="160" />
              <el-table-column prop="checkInTemperature" label="体温(℃)" width="110">
                <template #default="scope">
                  <span :style="{ color: scope.row.checkInTemperature > 37.3 ? '#ef4444' : '#22c55e', fontWeight: 'bold' }">
                    {{ scope.row.checkInTemperature }}℃
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="考勤状态" width="120">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="当月预计退费计算">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="handleCalculateRefund(scope.row)">
                    计算退费
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 教职工考勤 Tab -->
        <el-tab-pane label="👨‍🏫 教职工考勤" name="staff">
          <div v-loading="staffLoading">
            <el-alert title="教职工考勤包含打卡、排班及加班申请记录。" type="info" show-icon :closable="false" style="margin-bottom: 16px;" />
            <div style="display: flex; justify-content: flex-end; margin-bottom: 12px; gap: 10px;">
              <el-button type="primary" @click="handleStaffCheckIn">📍 模拟教职工打卡</el-button>
            </div>
            <el-table :data="staffAttendanceList" border style="width: 100%">
              <el-table-column prop="staffId" label="工号" width="100" />
              <el-table-column prop="staffName" label="姓名" width="130" />
              <el-table-column prop="attendanceDate" label="考勤日期" width="130">
                <template #default="scope">
                  {{ formatDate(scope.row.attendanceDate) }}
                </template>
              </el-table-column>
              <el-table-column prop="checkInTime" label="打卡时间" width="160">
                <template #default="scope">
                  {{ formatDateTime(scope.row.checkInTime) }}
                </template>
              </el-table-column>
              <el-table-column prop="shiftType" label="班次" width="120" />
              <el-table-column prop="attendanceStatus" label="考勤状态" width="120">
                <template #default="scope">
                  <el-tag :type="scope.row.attendanceStatus === '正常出勤' ? 'success' : (scope.row.attendanceStatus === '缺勤' ? 'danger' : 'warning')">
                    {{ scope.row.attendanceStatus || '正常出勤' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="overtimeHours" label="加班时长(小时)" width="130">
                <template #default="scope">
                  <span style="color: #ff9800; font-weight: bold;">{{ scope.row.overtimeHours || 0 }}h</span>
                </template>
              </el-table-column>
              <el-table-column label="操作">
                <template #default="scope">
                  <el-button type="warning" size="small" @click="handleApplyOvertime(scope.row)">
                    申请调休/加班
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getAttendanceList, calculateRefund, getStaffAttendanceList, staffCheckIn, applyOvertime } from '@/api/attendance';
import { ElMessage, ElMessageBox } from 'element-plus';

const activeTab = ref('student');
const defaultStudentAttendance = [
  { studentId: 101, studentName: '张小明', className: '小(1)班 - 雏菊班', attendanceDate: '2026-07-23', checkInTime: '07:55:12', checkInTemperature: 37.5, status: '异常' },
  { studentId: 102, studentName: '李思思', className: '小(1)班 - 雏菊班', attendanceDate: '2026-07-23', checkInTime: '08:05:40', checkInTemperature: 36.6, status: '正常' },
  { studentId: 103, studentName: '王豆豆', className: '小(1)班 - 雏菊班', attendanceDate: '2026-07-23', checkInTime: '08:12:15', checkInTemperature: 36.5, status: '正常' },
  { studentId: 106, studentName: '林梓涵', className: '小(2)班 - 苹果班', attendanceDate: '2026-07-23', checkInTime: '08:02:11', checkInTemperature: 36.5, status: '正常' },
  { studentId: 111, studentName: '孙浩然', className: '中(1)班 - 满天星班', attendanceDate: '2026-07-23', checkInTime: '07:58:30', checkInTemperature: 36.5, status: '正常' },
  { studentId: 116, studentName: '罗佳琪', className: '大(1)班 - 葵花班', attendanceDate: '2026-07-23', checkInTime: '07:50:55', checkInTemperature: 36.5, status: '正常' }
];

const defaultStaffAttendance = [
  { staffId: 1, staffName: '园长 (系统管理员)', attendanceDate: '2026-07-23', checkInTime: '2026-07-23T07:45:00', shiftType: '行政常班', attendanceStatus: '正常出勤', overtimeHours: 1.5 },
  { staffId: 201, staffName: '李老师', attendanceDate: '2026-07-23', checkInTime: '2026-07-23T07:50:00', shiftType: '早班', attendanceStatus: '正常出勤', overtimeHours: 2.0 },
  { staffId: 202, staffName: '陈老师', attendanceDate: '2026-07-23', checkInTime: '2026-07-23T07:55:00', shiftType: '早班', attendanceStatus: '正常出勤', overtimeHours: 0.0 },
  { staffId: 203, staffName: '王医生', attendanceDate: '2026-07-23', checkInTime: '2026-07-23T07:40:00', shiftType: '早班', attendanceStatus: '正常出勤', overtimeHours: 1.0 }
];

const attendanceList = ref(defaultStudentAttendance);
const staffAttendanceList = ref(defaultStaffAttendance);

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getAttendanceList();
    if (res && res.data && res.data.length > 0) {
      attendanceList.value = res.data;
    }
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const fetchStaffData = async () => {
  staffLoading.value = true;
  try {
    const res = await getStaffAttendanceList();
    if (res && res.data && res.data.length > 0) {
      staffAttendanceList.value = res.data;
    }
  } catch (error) {
    console.error(error);
  } finally {
    staffLoading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

const handleTabChange = (tabName) => {
  if (tabName === 'staff' && staffAttendanceList.value.length === 0) {
    fetchStaffData();
  }
};

const formatDate = (val) => {
  if (!val) return '';
  return val.split('T')[0];
};

const formatDateTime = (val) => {
  if (!val) return '';
  return val.replace('T', ' ').substring(0, 16);
};

const handleStaffCheckIn = () => {
  ElMessageBox.prompt('请输入要打卡的教职工工号(如: 1)', '模拟打卡', {
    confirmButtonText: '打卡',
    cancelButtonText: '取消',
    inputPattern: /^\d+$/,
    inputErrorMessage: '工号必须是数字'
  }).then(async ({ value }) => {
    try {
      await staffCheckIn(Number(value), 'GPS');
      ElMessage.success('打卡成功');
      fetchStaffData();
    } catch (e) {
      ElMessage.error(e.message || '打卡失败');
    }
  }).catch(() => {});
};

const handleApplyOvertime = (row) => {
  ElMessageBox.prompt('请输入申请加班时长(小时)', '加班申请', {
    confirmButtonText: '提交申请',
    cancelButtonText: '取消',
    inputPattern: /^\d+(\.\d+)?$/,
    inputErrorMessage: '请输入有效的数字'
  }).then(async ({ value }) => {
    try {
      await applyOvertime(row.staffId, Number(value));
      ElMessage.success('加班申请提交成功，待审批');
      fetchStaffData();
    } catch (e) {
      ElMessage.error(e.message || '申请失败');
    }
  }).catch(() => {});
};

const handleCalculateRefund = async (row) => {
  try {
    const month = new Date().toISOString().substring(0, 7);
    const res = await calculateRefund(row.studentId, month);
    const refundAmount = res.data || 0;
    ElMessageBox.alert(
      `学生：${row.studentName}<br>月份：${month}<br>系统自动根据 DB 算子计算退费金额：<b style="color:#22c55e;">￥${refundAmount} 元</b>`,
      '退费计算结果',
      { dangerouslyUseHTMLString: true, confirmButtonText: '确定' }
    );
  } catch (error) {
    ElMessage.error('退费计算失败');
  }
};

const handleExport = () => {
  const baseUrl = import.meta.env.VITE_API_BASE_URL || '/api';
  window.open(`${baseUrl}/kindergarten/attendance/export?classId=1&date=${new Date().toISOString().split('T')[0]}`, '_blank');
  ElMessage.success('考勤报表正在导出...');
};
</script>

<style scoped>
.attendance-container {
  padding: 24px;
}
:deep(.el-card) {
  border-radius: 16px;
  border: none;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04);
}
:deep(.el-card__header) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  font-weight: 700;
  font-size: 16px;
}
:deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
}
</style>
