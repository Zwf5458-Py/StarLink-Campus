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
    const wsUrl = `ws://${window.location.hostname}:8080/api/ws/board/${classInfo.value.roomNumber}`;
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
body {
  margin: 0;
  padding: 0;
}
.board-container {
  width: 100vw;
  height: 100vh;
  background: radial-gradient(circle at top left, #0f172a 0%, #1e1b4b 100%);
  color: #fff;
  padding: 16px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  font-family: -apple-system, BlinkMacSystemFont, "SF Pro Display", sans-serif;
}
.board-main-grid { flex: 1; display: grid; grid-template-columns: 1fr 1fr; gap: 12px; overflow: hidden; }
.grid-column { display: flex; flex-direction: column; gap: 12px; overflow: hidden; }

.glass-card { background: rgba(255, 255, 255, 0.06); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 14px; padding: 16px; display: flex; flex-direction: column; gap: 10px; }
.flex-1 { flex: 1; overflow: hidden; }

.card-title { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: bold; color: #e2e8f0; }
.badge { font-size: 10px; padding: 2px 6px; border-radius: 6px; }
.badge.blue { background: rgba(56, 189, 248, 0.2); color: #38bdf8; }
.badge.green { background: rgba(74, 222, 128, 0.2); color: #4ade80; }
.badge.yellow { background: rgba(253, 224, 71, 0.2); color: #fde047; }

.recipe-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; }
.recipe-box { background: rgba(255, 255, 255, 0.04); border-radius: 8px; padding: 10px; display: flex; flex-direction: column; gap: 4px; }
.recipe-box.highlight { background: rgba(74, 222, 128, 0.15); border: 1px solid rgba(74, 222, 128, 0.3); }
.meal-name { font-size: 12px; color: #4ade80; font-weight: bold; }
.meal-food { font-size: 11px; color: #cbd5e1; }

.teachers-row { display: flex; justify-content: space-around; }
.teacher-card { display: flex; align-items: center; gap: 8px; }
.avatar-circle { width: 36px; height: 36px; border-radius: 50%; background: rgba(255,255,255,0.1); display: flex; align-items: center; justify-content: center; font-size: 18px; }
.teacher-info { display: flex; flex-direction: column; }
.t-name { font-size: 12px; font-weight: bold; }
.t-role { font-size: 10px; color: #94a3b8; }

.stars-gallery { display: flex; gap: 10px; }
.star-student-card { flex: 1; background: rgba(253, 224, 71, 0.1); border: 1px solid rgba(253, 224, 71, 0.3); border-radius: 8px; padding: 10px; display: flex; flex-direction: column; gap: 4px; }
.star-badge { font-size: 11px; color: #fde047; font-weight: bold; }
.student-name { font-size: 12px; color: #fff; }
</style>
