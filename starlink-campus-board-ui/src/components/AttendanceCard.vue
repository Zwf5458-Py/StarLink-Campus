<template>
  <div class="glass-card">
    <div class="card-title">
      <span>📊 今日考勤与双因子核验</span>
      <span class="badge green">人脸+IC卡双重兜底</span>
    </div>
    <div class="attendance-body">
      <div class="stat-left">
        <div class="big-number">
          <span class="num">{{ attendance.presentCount }}</span>
          <span class="total">/ {{ attendance.totalCount }} 人</span>
        </div>
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: (attendance.presentCount / attendance.totalCount * 100) + '%' }"></div>
        </div>
      </div>
      <div class="punch-action-area">
        <button class="punch-btn" @click="$emit('punch')">
          📸人脸识别 + 💳IC卡兜底打卡
        </button>
      </div>
    </div>
  </div>

  <div v-if="showPunchModal" class="modal-overlay">
    <div class="punch-modal glass-card">
      <h3>🛡️ 双因子核验中：人脸 + IC 卡兜底</h3>
      <div class="scan-animation"></div>
      <div class="dual-factor-steps">
        <div class="step-line" :class="{ active: checkStep >= 1 }">
          <span>📸 步骤1: CompreFace 人脸特征比对 (512维) ... {{ checkStep >= 1 ? '✅ 命中' : '检测中' }}</span>
        </div>
        <div class="step-line" :class="{ active: checkStep >= 2 }">
          <span>💳 步骤2: 串口 IC 校园卡号校验 (CARD-88902) ... {{ checkStep >= 2 ? '✅ 双重核验通过' : '贴卡兜底中' }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
defineProps({
  attendance: Object,
  showPunchModal: Boolean,
  checkStep: Number
});
defineEmits(['punch']);
</script>

<style scoped>
.glass-card { background: rgba(255, 255, 255, 0.06); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 14px; padding: 16px; display: flex; flex-direction: column; gap: 10px; }
.card-title { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: bold; color: #e2e8f0; }
.badge { font-size: 10px; padding: 2px 6px; border-radius: 6px; }
.badge.green { background: rgba(74, 222, 128, 0.2); color: #4ade80; }

.attendance-body { display: flex; justify-content: space-between; align-items: center; }
.big-number .num { font-size: 36px; font-weight: bold; color: #4ade80; }
.big-number .total { font-size: 14px; color: #94a3b8; }
.progress-bar { height: 8px; width: 140px; background: rgba(255,255,255,0.1); border-radius: 4px; overflow: hidden; margin-top: 4px; }
.progress-fill { height: 100%; background: #4ade80; }
.punch-btn { background: linear-gradient(to right, #0071e3, #38bdf8); color: #fff; border: none; padding: 10px 18px; border-radius: 10px; font-size: 13px; font-weight: bold; cursor: pointer; box-shadow: 0 4px 12px rgba(0, 113, 227, 0.4); }

.modal-overlay { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.8); display: flex; align-items: center; justify-content: center; z-index: 2000; }
.punch-modal { text-align: center; padding: 30px 40px; width: 440px; }
.scan-animation { width: 140px; height: 140px; border: 2px solid #38bdf8; margin: 16px auto; position: relative; overflow: hidden; }
.scan-animation::after { content: ''; position: absolute; width: 100%; height: 3px; background: #38bdf8; top: 0; left: 0; animation: scan 2s linear infinite; box-shadow: 0 0 8px #38bdf8; }
@keyframes scan { 0% { top: 0; } 50% { top: 100%; } 100% { top: 0; } }

.dual-factor-steps { display: flex; flex-direction: column; gap: 8px; text-align: left; margin-top: 14px; font-size: 13px; }
.step-line { color: #94a3b8; padding: 6px 10px; border-radius: 6px; background: rgba(255,255,255,0.04); }
.step-line.active { color: #4ade80; font-weight: bold; background: rgba(74, 222, 128, 0.15); }
</style>
