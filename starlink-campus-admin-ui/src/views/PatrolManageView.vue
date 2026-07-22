<template>
  <div class="patrol-container" v-loading="loading">
    <el-card header="🛡️ 安防巡检打卡与故障报修工单中心">
      <div style="margin-bottom: 16px;">
        <el-button type="primary" @click="fetchData">刷新巡检与工单数据</el-button>
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

const loading = ref(false);
const activeTab = ref('patrol');
const patrolRecords = ref([]);
const repairOrders = ref([]);

const fetchData = async () => {
  loading.value = true;
  try {
    const resPatrol = await getPatrolList();
    patrolRecords.value = resPatrol.data || [];

    const resRepair = await getRepairOrders();
    repairOrders.value = resRepair.data || [];
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});
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
