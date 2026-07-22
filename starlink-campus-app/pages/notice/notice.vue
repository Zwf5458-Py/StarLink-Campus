<template>
  <view class="notice-container">
    <view class="header-title-box">
      <text class="title">📢 家园通知与签收</text>
    </view>

    <view class="notices-list">
      <view v-for="item in notices" :key="item.id" class="notice-card">
        <view class="notice-top">
          <text class="tag">{{ item.type }}</text>
          <text class="time">{{ item.date }}</text>
        </view>
        <text class="n-title">{{ item.title }}</text>
        <text class="n-content">{{ item.content }}</text>
        <view class="notice-footer">
          <text class="signed-text" v-if="item.isSigned">✅ 已签收答复 (已完成)</text>
          <button v-else class="sign-btn" @click="handleSign(item)">✍️ 确认签收告知书</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getArticleList } from '../../api/article';

const notices = ref([]);

onMounted(async () => {
  try {
    const res = await getArticleList();
    if (res && res.data) {
      // 过滤或者直接展示
      notices.value = res.data.map(a => ({
        id: a.id,
        type: a.category || '全园公告',
        date: a.createTime ? a.createTime.substring(0, 10) : '刚刚',
        title: a.title,
        content: a.content,
        isSigned: false
      }));
    }
  } catch (e) {
    console.error("Failed to fetch articles", e);
    // Fallback Mock
    notices.value = [
      { id: 1, type: '班级通知', date: '2026-07-22', title: '关于大(1)班秋季户外体能拓展活动的告知书', content: '各位家长：本周五上午班级将开展海星科普体能拓展，请为孩子穿着轻便运动鞋与水壶。', isSigned: false }
    ];
  }
});

const handleSign = (item) => {
  item.isSigned = true;
  uni.showToast({ title: '已成功签收答复！', icon: 'success' });
};
</script>

<style scoped>
.notice-container { min-height: 100vh; background: #f8fafc; padding: 16px; box-sizing: border-box; }
.header-title-box { margin-bottom: 16px; }
.title { font-size: 18px; font-weight: bold; color: #0f172a; }
.notices-list { display: flex; flex-direction: column; gap: 12px; }
.notice-card { background: #ffffff; border-radius: 16px; padding: 16px; box-shadow: 0 4px 16px rgba(0,0,0,0.03); }
.notice-top { display: flex; justify-content: space-between; font-size: 11px; margin-bottom: 8px; }
.tag { background: rgba(0,113,227,0.1); color: #0071e3; padding: 2px 8px; border-radius: 6px; font-weight: bold; }
.time { color: #94a3b8; }
.n-title { font-size: 15px; font-weight: bold; color: #0f172a; margin-bottom: 6px; display: block; }
.n-content { font-size: 12px; color: #475569; line-height: 1.5; margin-bottom: 12px; display: block; }
.notice-footer { display: flex; justify-content: flex-end; }
.signed-text { font-size: 12px; color: #34c759; font-weight: bold; }
.sign-btn { background: #0071e3; color: #fff; border: none; padding: 6px 16px; border-radius: 10px; font-size: 12px; font-weight: bold; }
</style>
