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
.glass-card { 
  background: var(--glass-bg); 
  backdrop-filter: blur(24px); 
  -webkit-backdrop-filter: blur(24px);
  border: 1px solid var(--glass-border); 
  border-radius: 20px; 
  padding: 24px; 
  display: flex; flex-direction: column; gap: 16px; 
  box-shadow: var(--glass-shadow);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.card-title { display: flex; justify-content: space-between; align-items: center; font-size: 18px; font-weight: 700; color: #f8fafc; }
.badge { font-size: 12px; padding: 4px 10px; border-radius: 8px; font-weight: 600; letter-spacing: 0.5px;}
.badge.green { background: rgba(74, 222, 128, 0.15); color: #86efac; border: 1px solid rgba(74, 222, 128, 0.3); }

.attendance-body { display: flex; justify-content: space-between; align-items: center; padding: 12px 0;}
.stat-left { display: flex; flex-direction: column; gap: 8px;}
.big-number .num { font-size: 48px; font-weight: 800; color: #4ade80; font-family: 'Outfit'; text-shadow: 0 0 20px rgba(74, 222, 128, 0.4);}
.big-number .total { font-size: 16px; color: #94a3b8; font-weight: 600;}
.progress-bar { height: 10px; width: 160px; background: rgba(255,255,255,0.1); border-radius: 6px; overflow: hidden; box-shadow: inset 0 2px 4px rgba(0,0,0,0.2);}
.progress-fill { height: 100%; background: linear-gradient(90deg, #4ade80, #38bdf8); box-shadow: 0 0 10px rgba(74, 222, 128, 0.5); transition: width 0.5s ease-out;}

.punch-btn { 
  background: linear-gradient(135deg, #0ea5e9, #38bdf8); 
  color: #fff; border: none; padding: 14px 24px; border-radius: 12px; 
  font-size: 15px; font-weight: 700; cursor: pointer; 
  box-shadow: 0 8px 24px rgba(14, 165, 233, 0.4);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}
.punch-btn::before {
  content: ''; position: absolute; top: 0; left: -100%; width: 50%; height: 100%;
  background: linear-gradient(to right, transparent, rgba(255,255,255,0.3), transparent);
  transform: skewX(-20deg); transition: 0.5s;
}
.punch-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(14, 165, 233, 0.6);
}
.punch-btn:hover::before { left: 150%; }

.modal-overlay { 
  position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; 
  background: rgba(5, 11, 20, 0.85); 
  backdrop-filter: blur(10px);
  display: flex; align-items: center; justify-content: center; z-index: 2000; 
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.punch-modal { text-align: center; padding: 40px; width: 500px; transform: scale(1); animation: popIn 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275); border: 1px solid rgba(56, 189, 248, 0.4); box-shadow: 0 20px 60px rgba(14, 165, 233, 0.3); }

@keyframes popIn {
  from { transform: scale(0.9); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.punch-modal h3 { font-size: 22px; color: #f8fafc; margin-bottom: 24px; font-weight: 700; letter-spacing: 1px;}

.scan-animation { 
  width: 180px; height: 180px; 
  border: 2px solid rgba(56, 189, 248, 0.3); 
  border-radius: 20px;
  margin: 0 auto 24px; position: relative; overflow: hidden; 
  background: radial-gradient(circle, rgba(56,189,248,0.1) 0%, transparent 70%);
}
.scan-animation::before {
  content: '👤';
  font-size: 80px;
  position: absolute;
  top: 50%; left: 50%; transform: translate(-50%, -50%);
  opacity: 0.5;
}
.scan-animation::after { 
  content: ''; position: absolute; width: 100%; height: 4px; background: #38bdf8; 
  top: 0; left: 0; animation: scan 2s cubic-bezier(0.4, 0, 0.2, 1) infinite; 
  box-shadow: 0 0 20px 4px #38bdf8; 
}
@keyframes scan { 0% { top: -10%; opacity: 0; } 10% { opacity: 1; } 90% { opacity: 1; } 100% { top: 110%; opacity: 0;} }

.dual-factor-steps { display: flex; flex-direction: column; gap: 12px; text-align: left; font-size: 14px; }
.step-line { 
  color: #94a3b8; padding: 12px 16px; border-radius: 10px; 
  background: rgba(255,255,255,0.03); 
  border: 1px solid transparent;
  transition: all 0.3s ease;
}
.step-line.active { 
  color: #4ade80; font-weight: 700; 
  background: linear-gradient(90deg, rgba(74, 222, 128, 0.1), transparent);
  border-left: 4px solid #4ade80;
  box-shadow: inset 0 0 20px rgba(74, 222, 128, 0.05);
}
</style>
