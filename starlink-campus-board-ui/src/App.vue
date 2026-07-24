<template>
  <div class="board-container">
    <WeatherWidget 
      :classInfo="classInfo"
      :wsConnected="wsConnected"
      :currentMode="currentMode"
      :urgentNotice="urgentNotice"
      :currentTime="currentTime"
      :currentDate="currentDate"
      @close-notice="urgentNotice = ''"
    />

    <main class="board-main-grid">
      <section class="grid-column">
        <ScheduleTable :scheduleList="scheduleList" />
        
        <div class="glass-card">
          <div class="card-title">
            <span>🥗 今日园所健康食谱</span>
            <span class="badge green">营养搭配</span>
          </div>
          <div class="recipe-grid">
            <div class="recipe-box">
              <span class="meal-name">早餐</span>
              <span class="meal-food">皮蛋瘦肉粥 + 蒸包</span>
            </div>
            <div class="recipe-box highlight">
              <span class="meal-name">午餐</span>
              <span class="meal-food">红烧小排 + 炒青菜 + 虾仁豆腐</span>
            </div>
            <div class="recipe-box">
              <span class="meal-name">点心</span>
              <span class="meal-food">新鲜香蕉 + 低脂牛奶</span>
            </div>
          </div>
        </div>
      </section>

      <section class="grid-column">
        <AttendanceCard 
          :attendance="attendance"
          :showPunchModal="showPunchModal"
          :checkStep="checkStep"
          @punch="handleMockDualFactorCheckIn"
        />

        <div class="glass-card">
          <div class="card-title">
            <span>👩‍🏫 班级教职团队</span>
            <span class="badge blue">服务名片</span>
          </div>
          <div class="teachers-row">
            <div class="teacher-card">
              <div class="avatar-circle">👩‍🏫</div>
              <div class="teacher-info">
                <span class="t-name">李老师</span>
                <span class="t-role">班主任</span>
              </div>
            </div>
            <div class="teacher-card">
              <div class="avatar-circle">👩‍⚕️</div>
              <div class="teacher-info">
                <span class="t-name">王医生</span>
                <span class="t-role">保健医师</span>
              </div>
            </div>
            <div class="teacher-card">
              <div class="avatar-circle">👩‍🍳</div>
              <div class="teacher-info">
                <span class="t-name">张阿姨</span>
                <span class="t-role">保育员</span>
              </div>
            </div>
          </div>
        </div>

        <div class="glass-card flex-1">
          <div class="card-title">
            <span>⭐ 本周成长之星与风采</span>
            <span class="badge yellow">荣誉榜</span>
          </div>
          <div class="stars-gallery">
            <div class="star-student-card">
              <span class="star-badge">🌟 本周小能手</span>
              <span class="student-name">张小明 (文明礼貌)</span>
            </div>
            <div class="star-student-card">
              <span class="star-badge">🏆 绘画小达人</span>
              <span class="student-name">李思思 (创意水彩)</span>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { BoardWebSocketClient } from './utils/websocket';
import { getBoardConfig, checkIn } from '@/api/board';
import WeatherWidget from './components/WeatherWidget.vue';
import AttendanceCard from './components/AttendanceCard.vue';
import ScheduleTable from './components/ScheduleTable.vue';

const currentTime = ref('');
const currentDate = ref('');
const wsConnected = ref(false);
const currentMode = ref('NORMAL');
const showPunchModal = ref(false);
const checkStep = ref(0);
const urgentNotice = ref('关于开展秋季幼儿园传染病防控与晨检卫生的通告');

const classInfo = ref({
  className: '大(1)班 - 葵花班',
  roomNumber: 'A101',
  slogan: '快乐成长，健康每一步！探索世界，梦想起航！'
});

const attendance = ref({
  totalCount: 28,
  presentCount: 27
});

const scheduleList = ref([
  { time: '08:00 - 08:30', name: '晨检入园与体温检测', current: false },
  { time: '08:30 - 09:00', name: '营养早餐时间', current: false },
  { time: '09:00 - 09:40', name: '海星科学实验探索课', current: true },
  { time: '10:00 - 11:00', name: '户外体能锻炼活动', current: false },
  { time: '12:00 - 14:30', name: '午餐与午休恢复', current: false },
  { time: '15:00 - 15:40', name: '绘本阅读与兴趣拓育', current: false }
]);

const updateClock = () => {
  const now = new Date();
  currentTime.value = now.toTimeString().substring(0, 8);
  currentDate.value = `${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日 星期${['日','一','二','三','四','五','六'][now.getDay()]}`;
};

const handleMockDualFactorCheckIn = async () => {
  showPunchModal.value = true;
  checkStep.value = 1;
  
  setTimeout(async () => {
    checkStep.value = 2;
    const temp = (36.2 + Math.random() * 0.8).toFixed(1);
    try {
      await checkIn(101, 'FACE_CARD_DUAL', temp);
      setTimeout(() => {
        showPunchModal.value = false;
        checkStep.value = 0;
        alert(`🎉 人脸+IC卡双因子核验成功！张小明 体温: ${temp}℃ (安全打卡完成)`);
        if (attendance.value.presentCount < attendance.value.totalCount) {
          attendance.value.presentCount++;
        }
      }, 1000);
    } catch (e) {
      setTimeout(() => {
        showPunchModal.value = false;
        checkStep.value = 0;
        alert(`❌ 打卡核验失败，无效通行卡号或人脸识别错误！`);
      }, 1000);
    }
  }, 1000);
};

onMounted(async () => {
  updateClock();
  setInterval(updateClock, 1000);

  try {
    const res = await getBoardConfig(classInfo.value.roomNumber);
    if (res && res.data) {
      const data = res.data;
      classInfo.value.className = data.className;
      classInfo.value.slogan = data.slogan;
      if (data.scheduleList) scheduleList.value = data.scheduleList;
    }
  } catch (e) {
    console.warn("Failed to fetch board config", e);
  }

  try {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    // When served from Spring Boot, we should use the same host and port
    const host = import.meta.env.DEV ? `${window.location.hostname}:8080` : window.location.host;
    const wsUrl = `${protocol}//${host}/api/ws/board/${classInfo.value.roomNumber}`;
    
    const client = new BoardWebSocketClient(wsUrl, (msg) => {
      wsConnected.value = true;
      if (msg.type === 'MODE_SWITCH') {
        currentMode.value = msg.mode;
      } else if (msg.type === 'NOTICE') {
        urgentNotice.value = msg.notice;
      }
    });
    client.connect();
  } catch (e) {
    wsConnected.value = false;
  }
});

</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700;800&family=Noto+Sans+SC:wght@400;500;700&display=swap');

body {
  margin: 0;
  padding: 0;
  background-color: #050b14;
}

:root {
  --primary-glow: #38bdf8;
  --success-glow: #4ade80;
  --warning-glow: #fde047;
  --glass-bg: rgba(255, 255, 255, 0.05);
  --glass-border: rgba(255, 255, 255, 0.1);
  --glass-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
}

.board-container {
  width: 100vw;
  height: 100vh;
  /* Dynamic gradient background */
  background: 
    radial-gradient(circle at 15% 50%, rgba(15, 23, 42, 0.9), transparent 50%),
    radial-gradient(circle at 85% 30%, rgba(30, 27, 75, 0.9), transparent 50%),
    #0a0f1c;
  color: #fff;
  padding: 24px 32px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  font-family: 'Outfit', 'Noto Sans SC', -apple-system, sans-serif;
  position: relative;
}

/* Add a subtle moving light orb in the background */
.board-container::before {
  content: '';
  position: absolute;
  top: -20%; left: -10%;
  width: 50vw; height: 50vw;
  background: radial-gradient(circle, rgba(56,189,248,0.05) 0%, transparent 60%);
  border-radius: 50%;
  animation: float 20s infinite alternate ease-in-out;
  pointer-events: none;
  z-index: 0;
}

@keyframes float {
  0% { transform: translate(0, 0) scale(1); }
  100% { transform: translate(20%, 10%) scale(1.1); }
}

.board-container > * {
  position: relative;
  z-index: 1;
}

.board-main-grid { 
  flex: 1; 
  display: grid; 
  grid-template-columns: 1fr 1fr; 
  gap: 20px; 
  overflow: hidden; 
  margin-top: 12px;
}

.grid-column { 
  display: flex; 
  flex-direction: column; 
  gap: 20px; 
  overflow: hidden; 
}

/* Glass Card with enhanced blur and hover effects */
.glass-card { 
  background: var(--glass-bg); 
  backdrop-filter: blur(24px); 
  -webkit-backdrop-filter: blur(24px);
  border: 1px solid var(--glass-border); 
  border-radius: 20px; 
  padding: 24px; 
  display: flex; 
  flex-direction: column; 
  gap: 16px; 
  box-shadow: var(--glass-shadow);
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.3s ease;
  animation: slideUpFade 0.8s cubic-bezier(0.16, 1, 0.3, 1) backwards;
}

.glass-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px 0 rgba(0, 0, 0, 0.4), inset 0 0 0 1px rgba(255,255,255,0.15);
}

/* Staggered entry animation */
.grid-column:nth-child(1) .glass-card:nth-child(1) { animation-delay: 0.1s; }
.grid-column:nth-child(1) .glass-card:nth-child(2) { animation-delay: 0.2s; }
.grid-column:nth-child(2) .glass-card:nth-child(1) { animation-delay: 0.15s; }
.grid-column:nth-child(2) .glass-card:nth-child(2) { animation-delay: 0.25s; }
.grid-column:nth-child(2) .glass-card:nth-child(3) { animation-delay: 0.35s; }

@keyframes slideUpFade {
  0% { opacity: 0; transform: translateY(30px); }
  100% { opacity: 1; transform: translateY(0); }
}

.flex-1 { flex: 1; overflow: hidden; }

.card-title { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  font-size: 18px; 
  font-weight: 700; 
  color: #f8fafc; 
  letter-spacing: 0.5px;
}

.badge { 
  font-size: 12px; 
  padding: 4px 10px; 
  border-radius: 8px; 
  font-weight: 600;
  letter-spacing: 0.5px;
}
.badge.blue { background: rgba(56, 189, 248, 0.15); color: #7dd3fc; border: 1px solid rgba(56, 189, 248, 0.3); }
.badge.green { background: rgba(74, 222, 128, 0.15); color: #86efac; border: 1px solid rgba(74, 222, 128, 0.3); }
.badge.yellow { background: rgba(253, 224, 71, 0.15); color: #fef08a; border: 1px solid rgba(253, 224, 71, 0.3); }

/* Enhanced Recipe Grid */
.recipe-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; height: 100%;}
.recipe-box { 
  background: rgba(255, 255, 255, 0.03); 
  border-radius: 12px; 
  padding: 16px 12px; 
  display: flex; 
  flex-direction: column; 
  gap: 8px; 
  transition: all 0.3s ease;
  border: 1px solid transparent;
  justify-content: center;
}
.recipe-box:hover {
  background: rgba(255, 255, 255, 0.06);
}
.recipe-box.highlight { 
  background: linear-gradient(145deg, rgba(74, 222, 128, 0.1), rgba(74, 222, 128, 0.02)); 
  border: 1px solid rgba(74, 222, 128, 0.3); 
  box-shadow: 0 4px 20px rgba(74, 222, 128, 0.1);
}
.meal-name { font-size: 14px; color: #86efac; font-weight: 700; }
.meal-food { font-size: 13px; color: #cbd5e1; line-height: 1.4; }

/* Enhanced Teachers Row */
.teachers-row { display: flex; justify-content: space-around; align-items: center; height: 100%;}
.teacher-card { display: flex; align-items: center; gap: 12px; transition: transform 0.3s ease; }
.teacher-card:hover { transform: scale(1.05); }
.avatar-circle { 
  width: 48px; 
  height: 48px; 
  border-radius: 50%; 
  background: linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.02)); 
  border: 1px solid rgba(255,255,255,0.15);
  display: flex; align-items: center; justify-content: center; font-size: 24px; 
  box-shadow: inset 0 2px 10px rgba(255,255,255,0.1);
}
.teacher-info { display: flex; flex-direction: column; gap: 2px;}
.t-name { font-size: 15px; font-weight: 700; letter-spacing: 0.5px;}
.t-role { font-size: 12px; color: #94a3b8; }

/* Enhanced Stars Gallery */
.stars-gallery { display: flex; gap: 16px; height: 100%;}
.star-student-card { 
  flex: 1; 
  background: linear-gradient(145deg, rgba(253, 224, 71, 0.08), rgba(253, 224, 71, 0.02)); 
  border: 1px solid rgba(253, 224, 71, 0.2); 
  border-radius: 12px; 
  padding: 16px; 
  display: flex; 
  flex-direction: column; 
  gap: 8px; 
  justify-content: center;
  position: relative;
  overflow: hidden;
}
.star-student-card::before {
  content: '';
  position: absolute;
  top: 0; left: -100%;
  width: 50%; height: 100%;
  background: linear-gradient(to right, transparent, rgba(255,255,255,0.1), transparent);
  transform: skewX(-20deg);
  animation: shine 6s infinite;
}
@keyframes shine {
  0%, 80% { left: -100%; }
  100% { left: 200%; }
}
.star-badge { font-size: 13px; color: #fef08a; font-weight: 700; }
.student-name { font-size: 14px; color: #f8fafc; }
</style>
