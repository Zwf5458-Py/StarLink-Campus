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
.glass-card { background: rgba(255, 255, 255, 0.06); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 14px; padding: 16px; display: flex; flex-direction: column; gap: 10px; }
.flex-1 { flex: 1; overflow: hidden; }

.card-title { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: bold; color: #e2e8f0; }
.badge { font-size: 10px; padding: 2px 6px; border-radius: 6px; }
.badge.blue { background: rgba(56, 189, 248, 0.2); color: #38bdf8; }

.schedule-list { display: flex; flex-direction: column; gap: 6px; overflow-y: auto; }
.schedule-item { display: flex; align-items: center; justify-content: space-between; background: rgba(255, 255, 255, 0.04); padding: 8px 12px; border-radius: 8px; font-size: 13px; }
.schedule-item.active { background: rgba(56, 189, 248, 0.2); border-left: 3px solid #38bdf8; }
.item-time { color: #94a3b8; font-size: 12px; font-family: monospace; }
.item-name { font-weight: 600; }
.item-tag { background: #38bdf8; color: #000; font-size: 10px; padding: 1px 6px; border-radius: 4px; font-weight: bold; }
</style>
