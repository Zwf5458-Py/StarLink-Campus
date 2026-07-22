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
.board-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; background: rgba(255, 255, 255, 0.05); backdrop-filter: blur(15px); border-radius: 14px; padding: 10px 18px; border: 1px solid rgba(255, 255, 255, 0.1); }
.header-left { display: flex; align-items: center; gap: 16px; }
.class-title-badge { display: flex; align-items: center; gap: 10px; }
.class-name { font-size: 26px; font-weight: bold; }
.room-pill { background: rgba(56, 189, 248, 0.2); color: #38bdf8; padding: 4px 10px; border-radius: 12px; font-size: 13px; font-weight: 600; }
.weather-box { display: flex; gap: 8px; font-size: 13px; color: #94a3b8; }
.divider { opacity: 0.3; }

.header-right { display: flex; align-items: center; gap: 16px; }
.ws-status-tag { display: flex; align-items: center; gap: 6px; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; }
.ws-status-tag.online { background: rgba(52, 199, 89, 0.2); color: #4ade80; }
.ws-status-tag.standby { background: rgba(255, 189, 46, 0.2); color: #ffbd2e; }
.ws-status-tag .dot { width: 6px; height: 6px; border-radius: 50%; background: currentColor; }

.clock-box { text-align: right; }
.time { font-size: 28px; font-weight: bold; font-family: monospace; margin-right: 8px; }
.date { font-size: 12px; color: #94a3b8; }

.mode-banner-bar { padding: 8px 16px; border-radius: 10px; margin-bottom: 8px; display: flex; justify-content: space-between; align-items: center; background: rgba(255, 255, 255, 0.05); font-size: 13px; font-weight: 600; border: 1px solid rgba(255, 255, 255, 0.08); }
.slogan-sub { color: #38bdf8; font-style: italic; }

.urgent-notice-bar { background: linear-gradient(135deg, #ff9500, #ff5e00); color: #fff; padding: 8px 16px; border-radius: 10px; margin-bottom: 10px; display: flex; align-items: center; justify-content: space-between; font-size: 13px; font-weight: bold; box-shadow: 0 4px 12px rgba(255, 149, 0, 0.4); }
.notice-close { background: rgba(0,0,0,0.2); color: #fff; border: none; padding: 2px 8px; border-radius: 6px; cursor: pointer; }
</style>
