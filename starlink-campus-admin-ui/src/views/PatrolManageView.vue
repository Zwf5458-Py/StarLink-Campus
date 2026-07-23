<template>
  <div class="patrol-container" v-loading="loading">
    <el-card header="🛡️ 安防巡检打卡与故障报修工单中心">
      <div style="margin-bottom: 16px;">
        <el-button type="primary" @click="fetchData">刷新巡检与工单数据</el-button>
        <el-button type="success" @click="handleExportPatrol">📥 导出巡检报表</el-button>
      </div>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="安防巡检打卡记录" name="patrol">
          <el-table :data="patrolRecords" border style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="patrolPointName" label="巡更点位名称" width="200" />
            <el-table-column prop="patrolStaffId" label="巡检安保员ID" width="130" />
            <el-table-column prop="patrolTime" label="打卡时间" width="180" />
            <el-table-column prop="watermarkInfo" label="防伪水印信息" />
            <el-table-column prop="isNormal" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.isNormal === 1 ? 'success' : 'danger'">
                  {{ scope.row.isNormal === 1 ? '设备正常' : '发现故障' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="后勤维修工单" name="repair">
          <el-table :data="repairOrders" border style="width: 100%">
            <el-table-column prop="orderNo" label="工单编号" width="180" />
            <el-table-column prop="description" label="故障描述" />
            <el-table-column prop="status" label="处理状态" width="140">
              <template #default="scope">
                <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="生成时间" width="180" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getPatrolList, getRepairOrders } from '@/api/patrol';
import { ElMessage } from 'element-plus';

const defaultPatrolRecords = [
  { id: 1, patrolPointName: '校园正门东侧门禁闸机', patrolStaffId: 'SEC-001 (张保安)', patrolTime: '2026-07-23 06:30', watermarkInfo: 'GPS: 113.93,22.54 [时间防篡改已核验]', isNormal: 1 },
  { id: 2, patrolPointName: '食堂中央厨房配餐与留样柜', patrolStaffId: 'SEC-001 (张保安)', patrolTime: '2026-07-23 07:00', watermarkInfo: 'GPS: 113.93,22.54 [时间防篡改已核验]', isNormal: 1 },
  { id: 3, patrolPointName: '1楼小(1)班室内消防栓与灭火器', patrolStaffId: 'SEC-002 (李保安)', patrolTime: '2026-07-23 08:30', watermarkInfo: 'GPS: 113.93,22.54 [时间防篡改已核验]', isNormal: 1 },
  { id: 4, patrolPointName: '3楼大班走廊紧急疏散指示灯', patrolStaffId: 'SEC-002 (李保安)', patrolTime: '2026-07-23 10:15', watermarkInfo: 'GPS: 113.93,22.54 [时间防篡改已核验]', isNormal: 0 }
];

const defaultRepairOrders = [
  { orderNo: 'REP-20260723-001', description: '3楼大班走廊紧急疏散指示灯备用电池电压低，需更换光源电池', status: '处理中', createTime: '2026-07-23 10:20' },
  { orderNo: 'REP-20260722-004', description: '小(2)班后排洗手池龙头微渗水，后勤组已完成更换防水阀芯', status: '已完成', createTime: '2026-07-22 14:15' },
  { orderNo: 'REP-20260721-002', description: '校园户外操场东侧照明红外感应探头镜头清扫维护', status: '已完成', createTime: '2026-07-21 16:30' }
];

const patrolRecords = ref(defaultPatrolRecords);
const repairOrders = ref(defaultRepairOrders);

const fetchData = async () => {
  loading.value = true;
  try {
    const resPatrol = await getPatrolList();
    if (resPatrol && resPatrol.data && resPatrol.data.length > 0) {
      patrolRecords.value = resPatrol.data;
    }

    const resRepair = await getRepairOrders();
    if (resRepair && resRepair.data && resRepair.data.length > 0) {
      repairOrders.value = resRepair.data;
    }
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

const handleExportPatrol = () => {
  const baseUrl = import.meta.env.VITE_API_BASE_URL || '/api';
  window.open(`${baseUrl}/kindergarten/patrol/export`, '_blank');
  ElMessage.success('巡检报表正在导出...');
};
</script>

<style scoped>
.patrol-container {
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
