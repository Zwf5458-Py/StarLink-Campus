<template>
  <div class="board-header">
    <div class="header-left">
      <div class="class-title-badge">
        <span class="class-name">{{ classInfo.className }}</span>
        <span class="room-pill">教室 {{ classInfo.roomNumber }}</span>
      </div>
      <div class="weather-box">
        <span>☀️ 26℃ 晴朗</span>
        <span class="divider">|</span>
        <span>湿度 65%</span>
        <span class="divider">|</span>
        <span>PM2.5 12 优</span>
      </div>
    </div>

    <div class="header-right">
      <div class="ws-status-tag" :class="wsConnected ? 'online' : 'standby'">
        <span class="dot"></span>
        <span>{{ wsConnected ? 'WebSocket 实时下发中' : '全网长连接就绪 (双因子模式)' }}</span>
      </div>

      <div class="clock-box">
        <span class="time">{{ currentTime }}</span>
        <span class="date">{{ currentDate }}</span>
      </div>
    </div>
  </div>

  <div class="mode-banner-bar" :class="currentMode">
    <span>📢 当前打卡校验：人脸识别 + IC 卡号双因子安全兜底</span>
    <span class="slogan-sub">"{{ classInfo.slogan }}"</span>
  </div>

  <div v-if="urgentNotice" class="urgent-notice-bar">
    <span class="notice-icon">🔔 园区最新通知公告：</span>
    <span class="notice-text">{{ urgentNotice }}</span>
    <button class="notice-close" @click="$emit('close-notice')">✕ 知道了</button>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
defineProps({
  classInfo: Object,
  wsConnected: Boolean,
  currentMode: String,
  urgentNotice: String,
  currentTime: String,
  currentDate: String
});
defineEmits(['close-notice']);
</script>

<style scoped>
.board-header { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 12px; 
  background: var(--glass-bg); 
  backdrop-filter: blur(24px); 
  -webkit-backdrop-filter: blur(24px);
  border-radius: 20px; 
  padding: 16px 24px; 
  border: 1px solid var(--glass-border); 
  box-shadow: var(--glass-shadow);
  animation: slideUpFade 0.6s backwards;
}

.header-left { display: flex; align-items: center; gap: 24px; }
.class-title-badge { display: flex; align-items: center; gap: 12px; }
.class-name { font-size: 28px; font-weight: 800; letter-spacing: 1px; color: #f8fafc; }
.room-pill { 
  background: linear-gradient(135deg, rgba(56, 189, 248, 0.2), rgba(56, 189, 248, 0.05)); 
  color: #7dd3fc; 
  padding: 6px 14px; 
  border-radius: 20px; 
  font-size: 14px; 
  font-weight: 700;
  border: 1px solid rgba(56, 189, 248, 0.3);
}

.weather-box { display: flex; align-items: center; gap: 12px; font-size: 14px; color: #94a3b8; font-weight: 500; }
.divider { opacity: 0.2; }

.header-right { display: flex; align-items: center; gap: 24px; }
.ws-status-tag { 
  display: flex; align-items: center; gap: 8px; 
  padding: 6px 16px; border-radius: 24px; 
  font-size: 13px; font-weight: 700; 
  border: 1px solid transparent;
}
.ws-status-tag.online { 
  background: rgba(74, 222, 128, 0.1); 
  color: #4ade80; 
  border-color: rgba(74, 222, 128, 0.2);
}
.ws-status-tag.standby { 
  background: rgba(253, 224, 71, 0.1); 
  color: #fde047; 
  border-color: rgba(253, 224, 71, 0.2);
}
.ws-status-tag .dot { width: 8px; height: 8px; border-radius: 50%; background: currentColor; box-shadow: 0 0 8px currentColor;}

.clock-box { text-align: right; display: flex; flex-direction: column; }
.time { font-size: 32px; font-weight: 800; font-family: 'Outfit', monospace; letter-spacing: 2px; color: #f8fafc;}
.date { font-size: 13px; color: #cbd5e1; font-weight: 500;}

.mode-banner-bar { 
  padding: 12px 20px; 
  border-radius: 12px; 
  margin-bottom: 12px; 
  display: flex; justify-content: space-between; align-items: center; 
  background: linear-gradient(90deg, rgba(56, 189, 248, 0.1), transparent); 
  font-size: 14px; font-weight: 600; 
  border-left: 4px solid var(--primary-glow);
  color: #f8fafc;
  animation: slideUpFade 0.7s backwards;
}
.slogan-sub { color: #7dd3fc; font-style: italic; font-weight: 400;}

.urgent-notice-bar { 
  background: linear-gradient(135deg, rgba(249, 115, 22, 0.9), rgba(234, 88, 12, 0.9)); 
  color: #fff; 
  padding: 12px 24px; 
  border-radius: 12px; 
  margin-bottom: 12px; 
  display: flex; align-items: center; justify-content: space-between; 
  font-size: 14px; font-weight: 700; 
  box-shadow: 0 8px 24px rgba(249, 115, 22, 0.3); 
  animation: pulseNotice 2s infinite;
}

@keyframes pulseNotice {
  0% { box-shadow: 0 0 0 0 rgba(249, 115, 22, 0.4); }
  70% { box-shadow: 0 0 0 15px rgba(249, 115, 22, 0); }
  100% { box-shadow: 0 0 0 0 rgba(249, 115, 22, 0); }
}

.notice-icon { margin-right: 8px; font-size: 16px; }
.notice-text { flex: 1; letter-spacing: 0.5px; }
.notice-close { background: rgba(0,0,0,0.25); color: #fff; border: none; padding: 4px 12px; border-radius: 8px; cursor: pointer; font-weight: bold; transition: background 0.2s;}
.notice-close:hover { background: rgba(0,0,0,0.4); }
</style>
