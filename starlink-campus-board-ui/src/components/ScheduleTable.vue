<template>
  <div class="glass-card flex-1">
    <div class="card-title">
      <span>📅 今日班级活动排期 (课表)</span>
      <span class="badge blue">实时进度</span>
    </div>
    <div class="schedule-list">
      <div 
        v-for="(item, idx) in scheduleList" 
        :key="idx" 
        class="schedule-item"
        :class="{ active: item.current }"
      >
        <span class="item-time">{{ item.time }}</span>
        <span class="item-name">{{ item.name }}</span>
        <span class="item-tag" v-if="item.current">进行中</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';
defineProps({
  scheduleList: Array
});
</script>

<style scoped>
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
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.flex-1 { flex: 1; overflow: hidden; }

.card-title { 
  display: flex; justify-content: space-between; align-items: center; 
  font-size: 18px; font-weight: 700; color: #f8fafc; letter-spacing: 0.5px;
}
.badge { font-size: 12px; padding: 4px 10px; border-radius: 8px; font-weight: 600; }
.badge.blue { background: rgba(56, 189, 248, 0.15); color: #7dd3fc; border: 1px solid rgba(56, 189, 248, 0.3); }

.schedule-list { 
  display: flex; flex-direction: column; gap: 10px; 
  overflow-y: auto; padding-right: 4px;
}
/* Custom Scrollbar */
.schedule-list::-webkit-scrollbar { width: 4px; }
.schedule-list::-webkit-scrollbar-track { background: transparent; }
.schedule-list::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.2); border-radius: 4px; }

.schedule-item { 
  display: flex; align-items: center; justify-content: space-between; 
  background: rgba(255, 255, 255, 0.03); 
  padding: 12px 16px; 
  border-radius: 12px; 
  font-size: 14px; 
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
}
.schedule-item:hover { background: rgba(255,255,255,0.06); }

.schedule-item.active { 
  background: linear-gradient(90deg, rgba(56, 189, 248, 0.15), rgba(56, 189, 248, 0.02)); 
  border-left: 4px solid var(--primary-glow); 
  box-shadow: 0 4px 15px rgba(56, 189, 248, 0.15);
  transform: translateX(4px);
}
.item-time { color: #94a3b8; font-size: 13px; font-family: 'Outfit', monospace; font-weight: 600;}
.schedule-item.active .item-time { color: #7dd3fc; }

.item-name { font-weight: 700; color: #cbd5e1; flex: 1; margin-left: 16px; letter-spacing: 0.5px;}
.schedule-item.active .item-name { color: #fff; text-shadow: 0 0 8px rgba(255,255,255,0.3); }

.item-tag { 
  background: var(--primary-glow); 
  color: #0f172a; 
  font-size: 11px; 
  padding: 2px 8px; 
  border-radius: 6px; 
  font-weight: 800; 
  box-shadow: 0 0 10px rgba(56, 189, 248, 0.6);
  animation: breathe 2s infinite alternate;
}

@keyframes breathe {
  0% { opacity: 0.8; box-shadow: 0 0 5px rgba(56, 189, 248, 0.4); }
  100% { opacity: 1; box-shadow: 0 0 15px rgba(56, 189, 248, 0.8); }
}
</style>
