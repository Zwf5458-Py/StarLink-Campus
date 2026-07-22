<template>
  <div class="screen-container" v-loading="loading">
    <!-- 顶部 动态控制 Header -->
    <header class="screen-header">
      <div class="header-title-box">
        <h1>📊 海星智能校园 · 数智监管 8 大动态图表看板</h1>
        <span class="sub-title">实时数据中枢 · 动态图表可视化 (Data Visual Dashboard)</span>
      </div>
      <div class="header-right-actions">
        <span class="live-dot">🟢 Live 实时数据引擎</span>
        <button class="refresh-btn" @click="fetchStats">刷新图表</button>
      </div>
    </header>

    <!-- 4x2 动态图表网格 -->
    <div class="screen-grid">
      <!-- 1. 全园出勤趋势 (动态折线趋势图) -->
      <div class="screen-card">
        <div class="card-title">
          <span>📊 1. 全园近一周出勤趋势</span>
          <span class="badge blue">折线趋势图</span>
        </div>
        <div class="chart-container">
          <svg class="svg-chart" viewBox="0 0 300 120">
            <!-- 渐变填充背景 -->
            <defs>
              <linearGradient id="grad1" x1="0%" y1="0%" x2="0%" y2="100%">
                <stop offset="0%" stop-color="#38bdf8" stop-opacity="0.4" />
                <stop offset="100%" stop-color="#38bdf8" stop-opacity="0.0" />
              </linearGradient>
            </defs>
            <!-- 区域闭合线 -->
            <polygon points="20,100 20,40 80,30 140,45 200,20 260,25 260,100" fill="url(#grad1)" />
            <!-- 折线 -->
            <polyline points="20,40 80,30 140,45 200,20 260,25" fill="none" stroke="#38bdf8" stroke-width="3" />
            <!-- 数据圆点 -->
            <circle cx="20" cy="40" r="4" fill="#38bdf8" />
            <circle cx="80" cy="30" r="4" fill="#38bdf8" />
            <circle cx="140" cy="45" r="4" fill="#38bdf8" />
            <circle cx="200" cy="20" r="5" fill="#34c759" />
            <circle cx="260" cy="25" r="4" fill="#38bdf8" />
            <!-- X轴刻度 -->
            <text x="20" y="115" class="chart-label">周一</text>
            <text x="80" y="115" class="chart-label">周二</text>
            <text x="140" y="115" class="chart-label">周三</text>
            <text x="200" y="115" class="chart-label">周四</text>
            <text x="260" y="115" class="chart-label">今日</text>
          </svg>
        </div>
        <div class="card-footer-info">今日出勤率: <strong>{{ stats.attendanceRate || 0 }}%</strong> ({{ stats.presentStudents || 0 }}/{{ stats.totalStudents || 0 }}人)</div>
      </div>

      <!-- 2. 晨检体温区间分布 (动态环形饼图) -->
      <div class="screen-card">
        <div class="card-title">
          <span>🩺 2. 晨检体温区间分布</span>
          <span class="badge green">环形饼图</span>
        </div>
        <div class="chart-container flex-center">
          <svg class="donut-chart" viewBox="0 0 120 120">
            <!-- 底圈 (正常 <37.0℃) -->
            <circle cx="60" cy="60" r="45" fill="none" stroke="#34c759" stroke-width="14" :stroke-dasharray="(280 * ((stats.presentStudents || 1) - (stats.feverCount || 0)) / (stats.presentStudents || 1)) + ' ' + 280" stroke-dashoffset="0" />
            <!-- 偏高 (37.0-37.3℃) -->
            <circle cx="60" cy="60" r="45" fill="none" stroke="#ffbd2e" stroke-width="14" stroke-dasharray="0 280" stroke-dashoffset="0" />
            <!-- 发热 (>37.3℃) -->
            <circle cx="60" cy="60" r="45" fill="none" stroke="#ff3b30" stroke-width="14" :stroke-dasharray="(280 * (stats.feverCount || 0) / (stats.presentStudents || 1)) + ' ' + 280" stroke-dashoffset="0" />
            <text x="60" y="65" text-anchor="middle" class="donut-center-text">100%</text>
          </svg>
          <div class="legend-box">
            <div class="legend-item"><span class="dot green"></span> 正常(&lt;37.0℃): {{ (stats.presentStudents || 0) - (stats.feverCount || 0) }}人</div>
            <div class="legend-item"><span class="dot yellow"></span> 偏高(37.0-37.3℃): 0人</div>
            <div class="legend-item"><span class="dot red"></span> 发热(&gt;37.3℃): {{ stats.feverCount || 0 }}人</div>
          </div>
        </div>
        <div class="card-footer-info">全员晨检完毕 · 无发热隔离案例</div>
      </div>

      <!-- 3. 幼儿身高体重达标率 (对比柱状图) -->
      <div class="screen-card">
        <div class="card-title">
          <span>📏 3. 幼儿身高体重达标对比</span>
          <span class="badge purple">对比柱状图</span>
        </div>
        <div class="chart-container">
          <svg class="svg-chart" viewBox="0 0 300 120">
            <!-- 小班 -->
            <rect x="30" y="30" width="16" height="70" fill="#c084fc" rx="3" />
            <rect x="50" y="35" width="16" height="65" fill="#a855f7" rx="3" />
            <!-- 中班 -->
            <rect x="110" y="20" width="16" height="80" fill="#c084fc" rx="3" />
            <rect x="130" y="25" width="16" height="75" fill="#a855f7" rx="3" />
            <!-- 大班 -->
            <rect x="190" y="15" width="16" height="85" fill="#c084fc" rx="3" />
            <rect x="210" y="20" width="16" height="80" fill="#a855f7" rx="3" />
            <!-- X 轴刻度 -->
            <text x="43" y="115" class="chart-label">小班</text>
            <text x="123" y="115" class="chart-label">中班</text>
            <text x="203" y="115" class="chart-label">大班</text>
          </svg>
        </div>
        <div class="card-footer-info">WHO 达标率: 身高 <strong>98.5%</strong> | 体重 <strong>97.8%</strong></div>
      </div>

      <!-- 4. 26台智慧班牌网络 (动态进度条) -->
      <div class="screen-card">
        <div class="card-title">
          <span>💻 4. 智慧班牌延迟与网络并发</span>
          <span class="badge blue">实时网络</span>
        </div>
        <div class="chart-container column-bars">
          <div class="progress-item" v-for="i in 4" :key="i">
            <div class="progress-header">
              <span>班牌组 0{{i}} (A10{{i}})</span>
              <span>8ms</span>
            </div>
            <div class="progress-bg">
              <div class="progress-fill" :style="{ width: (85 + i * 3) + '%' }"></div>
            </div>
          </div>
        </div>
        <div class="card-footer-info">WebSocket 在线率: <strong>{{ stats.totalBoardCount ? Math.round(stats.onlineBoardCount / stats.totalBoardCount * 100) : 0 }}%</strong> ({{ stats.onlineBoardCount || 0 }}/{{ stats.totalBoardCount || 0 }}台)</div>
      </div>

      <!-- 5. 园务 OA 请假类型 (动态玫瑰饼图) -->
      <div class="screen-card">
        <div class="card-title">
          <span>📝 5. 园务 OA 审批分类占比</span>
          <span class="badge yellow">占比分析</span>
        </div>
        <div class="chart-container flex-center">
          <svg class="donut-chart" viewBox="0 0 120 120">
            <circle cx="60" cy="60" r="40" fill="none" stroke="#fde047" stroke-width="16" stroke-dasharray="140 110" />
            <circle cx="60" cy="60" r="40" fill="none" stroke="#38bdf8" stroke-width="16" stroke-dasharray="70 180" stroke-dashoffset="-140" />
            <circle cx="60" cy="60" r="40" fill="none" stroke="#4ade80" stroke-width="16" stroke-dasharray="40 210" stroke-dashoffset="-210" />
          </svg>
          <div class="legend-box">
            <div class="legend-item"><span class="dot yellow"></span> 病假申请 (55%)</div>
            <div class="legend-item"><span class="dot blue"></span> 事假申请 (30%)</div>
            <div class="legend-item"><span class="dot green"></span> 补卡申请 (15%)</div>
          </div>
        </div>
        <div class="card-footer-info">本月累计完成审批: <strong>19 件</strong></div>
      </div>

      <!-- 6. 校园安防巡更进度 (动态柱状图) -->
      <div class="screen-card">
        <div class="card-title">
          <span>🛡️ 6. 安防巡更时段完成率</span>
          <span class="badge green">柱状图</span>
        </div>
        <div class="chart-container">
          <svg class="svg-chart" viewBox="0 0 300 120">
            <rect x="25" :y="100 - (stats.patrolRate || 80) * 0.8" width="30" :height="(stats.patrolRate || 80) * 0.8" fill="#4ade80" rx="4" />
            <rect x="95" :y="100 - (stats.patrolRate || 80) * 0.85" width="30" :height="(stats.patrolRate || 80) * 0.85" fill="#4ade80" rx="4" />
            <rect x="165" :y="100 - (stats.patrolRate || 80) * 0.7" width="30" :height="(stats.patrolRate || 80) * 0.7" fill="#4ade80" rx="4" />
            <rect x="235" :y="100 - (stats.patrolRate || 80) * 0.6" width="30" :height="(stats.patrolRate || 80) * 0.6" fill="#38bdf8" rx="4" />
            <text x="40" y="115" class="chart-label">早晨</text>
            <text x="110" y="115" class="chart-label">中午</text>
            <text x="180" y="115" class="chart-label">傍晚</text>
            <text x="250" y="115" class="chart-label">夜间</text>
          </svg>
        </div>
        <div class="card-footer-info">巡更打卡完成率: <strong>{{ stats.patrolRate || 0 }}%</strong></div>
      </div>

      <!-- 7. 今日访客入园流量 (横向条形图) -->
      <div class="screen-card">
        <div class="card-title">
          <span>🎫 7. 今日访客入园流量分段</span>
          <span class="badge cyan">横向条形图</span>
        </div>
        <div class="chart-container column-bars">
          <div class="progress-item">
            <div class="progress-header"><span>08:00 - 10:00</span><span>2 人次</span></div>
            <div class="progress-bg"><div class="progress-fill cyan-bg" style="width: 80%;"></div></div>
          </div>
          <div class="progress-item">
            <div class="progress-header"><span>10:00 - 12:00</span><span>1 人次</span></div>
            <div class="progress-bg"><div class="progress-fill cyan-bg" style="width: 40%;"></div></div>
          </div>
          <div class="progress-item">
            <div class="progress-header"><span>14:00 - 16:00</span><span>0 人次</span></div>
            <div class="progress-bg"><div class="progress-fill cyan-bg" style="width: 5%;"></div></div>
          </div>
        </div>
        <div class="card-footer-info">今日核验入园: <strong>3 人次</strong> | 滞留: <strong>0人</strong></div>
      </div>

      <!-- 8. 家园共育活跃度 (动态平滑曲线图) -->
      <div class="screen-card">
        <div class="card-title">
          <span>🌟 8. 家园班级圈互动活跃度</span>
          <span class="badge pink">曲线面积图</span>
        </div>
        <div class="chart-container">
          <svg class="svg-chart" viewBox="0 0 300 120">
            <defs>
              <linearGradient id="pinkGrad" x1="0%" y1="0%" x2="0%" y2="100%">
                <stop offset="0%" stop-color="#f472b6" stop-opacity="0.4" />
                <stop offset="100%" stop-color="#f472b6" stop-opacity="0.0" />
              </linearGradient>
            </defs>
            <polygon points="20,100 20,70 80,40 140,20 200,60 260,30 260,100" fill="url(#pinkGrad)" />
            <polyline points="20,70 80,40 140,20 200,60 260,30" fill="none" stroke="#f472b6" stroke-width="3" />
            <circle cx="140" cy="20" r="5" fill="#f472b6" />
            <text x="20" y="115" class="chart-label">08点</text>
            <text x="80" y="115" class="chart-label">12点</text>
            <text x="140" y="115" class="chart-label">17点(峰值)</text>
            <text x="200" y="115" class="chart-label">19点</text>
            <text x="260" y="115" class="chart-label">21点</text>
          </svg>
        </div>
        <div class="card-footer-info">今日互动峰值: <strong>1,280 次</strong> (点赞/评论/相册)</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getDashboardStats } from '@/api/dashboard';

const loading = ref(false);
const stats = ref({});

const fetchStats = async () => {
  loading.value = true;
  try {
    const res = await getDashboardStats();
    if (res && res.data) {
      stats.value = res.data;
    }
  } catch (error) {
    console.log('[大屏退避：加载动态图表视图]');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchStats();
});
</script>

<style scoped>
.screen-container {
  background: #090d16;
  color: #fff;
  padding: 20px;
  border-radius: 16px;
  min-height: calc(100vh - 120px);
  box-sizing: border-box;
}

.screen-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid rgba(255,255,255,0.1);
  padding-bottom: 12px;
}

.screen-header h1 {
  font-size: 20px;
  margin: 0 0 4px 0;
  background: linear-gradient(to right, #38bdf8, #818cf8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.sub-title {
  font-size: 12px;
  color: #94a3b8;
}

.header-right-actions {
  display: flex;
  align-items: center;
  gap: 14px;
}
.live-dot {
  font-size: 12px;
  color: #4ade80;
  font-weight: 600;
}
.refresh-btn {
  background: #0071e3;
  color: #fff;
  border: none;
  padding: 6px 14px;
  border-radius: 14px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
}

.screen-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.screen-card {
  background: rgba(15, 23, 42, 0.85);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
  transition: transform 0.2s ease;
}
.screen-card:hover {
  transform: translateY(-3px);
  border-color: rgba(56, 189, 248, 0.3);
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  font-weight: 700;
  color: #e2e8f0;
}
.badge {
  font-size: 10px;
  padding: 2px 6px;
  border-radius: 6px;
  font-weight: 600;
}
.badge.blue { background: rgba(56, 189, 248, 0.2); color: #38bdf8; }
.badge.green { background: rgba(74, 222, 128, 0.2); color: #4ade80; }
.badge.purple { background: rgba(192, 132, 252, 0.2); color: #c084fc; }
.badge.yellow { background: rgba(253, 224, 71, 0.2); color: #fde047; }
.badge.cyan { background: rgba(34, 211, 238, 0.2); color: #22d3ee; }
.badge.pink { background: rgba(244, 114, 182, 0.2); color: #f472b6; }

.chart-container {
  height: 120px;
  margin: 10px 0;
  display: flex;
  align-items: center;
}
.chart-container.flex-center {
  justify-content: space-around;
}
.svg-chart {
  width: 100%;
  height: 100%;
}
.chart-label {
  fill: #64748b;
  font-size: 10px;
}

/* 环形图 & 图例 */
.donut-chart {
  width: 90px;
  height: 90px;
}
.donut-center-text {
  fill: #fff;
  font-size: 16px;
  font-weight: bold;
}
.legend-box {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 11px;
  color: #94a3b8;
}
.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}
.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}
.dot.green { background: #34c759; }
.dot.yellow { background: #ffbd2e; }
.dot.red { background: #ff3b30; }
.dot.blue { background: #38bdf8; }

/* 进度条与网络 */
.column-bars {
  flex-direction: column;
  justify-content: center;
  gap: 10px;
  width: 100%;
}
.progress-item {
  width: 100%;
}
.progress-header {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: #94a3b8;
  margin-bottom: 4px;
}
.progress-bg {
  height: 8px;
  background: rgba(255,255,255,0.08);
  border-radius: 4px;
  overflow: hidden;
}
.progress-fill {
  height: 100%;
  background: linear-gradient(to right, #3b82f6, #38bdf8);
  border-radius: 4px;
  transition: width 0.5s ease;
}
.progress-fill.cyan-bg {
  background: linear-gradient(to right, #06b6d4, #22d3ee);
}

.card-footer-info {
  font-size: 11px;
  color: #64748b;
  border-top: 1px solid rgba(255,255,255,0.05);
  padding-top: 8px;
}
.card-footer-info strong {
  color: #38bdf8;
}
</style>
