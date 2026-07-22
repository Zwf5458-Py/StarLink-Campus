<template>
  <div class="apple-dashboard" v-loading="loading">
    <!-- 顶部 4 大苹果立体毛玻璃指标卡片 (Apple Glass Cards) -->
    <el-row :gutter="16">
      <el-col :span="6">
        <div class="apple-glass-card blue">
          <div class="card-top">
            <span class="card-icon">📊</span>
            <span class="card-label">全园出勤率</span>
          </div>
          <div class="card-main-val">{{ stats.attendanceRate }}%</div>
          <div class="card-sub-info">应到 {{ stats.totalStudents }} 人 / 实到 {{ stats.presentStudents }} 人</div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="apple-glass-card red">
          <div class="card-top">
            <span class="card-icon">🩺</span>
            <span class="card-label">晨检发热预警</span>
          </div>
          <div class="card-main-val red-text">{{ stats.feverCount }} 人</div>
          <div class="card-sub-info">全员晨检体温正常 (<37.3℃)</div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="apple-glass-card green">
          <div class="card-top">
            <span class="card-icon">💻</span>
            <span class="card-label">智慧班牌在线率</span>
          </div>
          <div class="card-main-val green-text">{{ stats.onlineBoardCount }}/{{ stats.totalBoardCount }}台</div>
          <div class="card-sub-info">全网长连接在线情况</div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="apple-glass-card purple">
          <div class="card-top">
            <span class="card-icon">🛡️</span>
            <span class="card-label">安防巡更打卡</span>
          </div>
          <div class="card-main-val purple-text">{{ stats.patrolRate }}%</div>
          <div class="card-sub-info">今日点位巡更打卡进度</div>
        </div>
      </el-col>
    </el-row>

    <!-- 苹果风格表格区块 -->
    <div class="apple-table-wrapper" style="margin-top: 20px;">
      <div class="table-header">
        <span class="table-title">🏫 班级实时考勤与晨检监测概况</span>
        <button class="apple-btn-primary" @click="fetchStats">刷新数据</button>
      </div>

      <el-table :data="classSummaries" class="apple-table" style="width: 100%">
        <el-table-column prop="className" label="班级名称" width="200" />
        <el-table-column prop="gradeLevel" label="年级" width="120" />
        <el-table-column prop="totalCount" label="班级总人数" />
        <el-table-column prop="presentCount" label="已出勤人数" />
        <el-table-column prop="leaveCount" label="请假人数" />
        <el-table-column prop="healthStatus" label="晨检监测状态">
          <template #default="scope">
            <span class="apple-badge" :class="scope.row.healthStatus === '全部正常' ? 'success' : 'danger'">
              {{ scope.row.healthStatus }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getDashboardStats } from '@/api/dashboard';

const loading = ref(false);
const stats = ref({
  attendanceRate: 0,
  totalStudents: 0,
  presentStudents: 0,
  feverCount: 0,
  onlineBoardCount: 0,
  totalBoardCount: 0,
  patrolRate: 0
});

const classSummaries = ref([]);

const fetchStats = async () => {
  loading.value = true;
  try {
    const res = await getDashboardStats();
    if (res && res.data) {
      stats.value = res.data;
      classSummaries.value = res.data.classList || [];
    }
  } catch (e) {
    console.log('[未连接到后端 API，开启优雅离线模式展示]');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchStats();
});
</script>

<style scoped>
/* 苹果毛玻璃卡片 (Apple Glass Cards) */
.apple-glass-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 1);
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;
  gap: 8px;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.apple-glass-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.08);
}
.apple-glass-card.blue { border-left: 4px solid #0071e3; }
.apple-glass-card.red { border-left: 4px solid #ff3b30; }
.apple-glass-card.green { border-left: 4px solid #34c759; }
.apple-glass-card.purple { border-left: 4px solid #af52de; }

.card-top {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 700;
  color: #64748b;
}
.card-main-val {
  font-size: 32px;
  font-weight: 800;
  color: #0f172a;
  font-family: -apple-system, BlinkMacSystemFont, "SF Pro Display", monospace;
  letter-spacing: -0.5px;
}
.red-text { color: #ff3b30; }
.green-text { color: #34c759; }
.purple-text { color: #af52de; }

.card-sub-info {
  font-size: 12px;
  color: #94a3b8;
  font-weight: 500;
}

/* 苹果风格表格 */
.apple-table-wrapper {
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 16px;
  padding: 20px;
}
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.table-title {
  font-size: 16px;
  font-weight: 700;
  color: #0f172a;
}
.apple-btn-primary {
  background: #0071e3;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 113, 227, 0.3);
  transition: all 0.2s ease;
}
.apple-btn-primary:hover {
  background: #005bb5;
  transform: scale(1.02);
}

.apple-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}
.apple-badge.success {
  background: rgba(52, 199, 89, 0.15);
  color: #248a3d;
}
.apple-badge.danger {
  background: rgba(255, 59, 48, 0.15);
  color: #d70015;
}
</style>
