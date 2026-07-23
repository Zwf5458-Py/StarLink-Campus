<template>
  <div class="attendance-container" v-loading="loading">
    <el-card header="👶 幼儿每日打卡记录与月度伙食费退费计算器">
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
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getAttendanceList, calculateRefund } from '@/api/attendance';
import { ElMessage, ElMessageBox } from 'element-plus';

const loading = ref(false);
const attendanceList = ref([]);

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getAttendanceList();
    attendanceList.value = res.data || [];
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

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
