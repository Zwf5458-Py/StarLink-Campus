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
    <view v-if="notices.length === 0 && !loading" class="empty-state">暂无通知公告</view>
    <view v-if="loading" class="loading-state">加载中...</view>
    <view v-if="loadError" class="error-state"><text>加载失败</text><button @click="retryLoad">重试</button></view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { onPullDownRefresh } from '@dcloudio/uni-app';
import { getArticleList, approveArticle } from '../../api/article';

const notices = ref([]);
const loading = ref(true);
const loadError = ref(false);

const fetchData = async () => {
  loading.value = true;
  loadError.value = false;
  try {
    const res = await getArticleList();
    if (res && res.data) {
      notices.value = res.data.map(a => ({
        id: a.id,
        type: a.category || '全园公告',
        date: a.createTime ? a.createTime.substring(0, 10) : '刚刚',
        title: a.title,
        content: a.content,
        isSigned: a.status === '已发布' ? false : false // Or based on other real logic
      }));
    }
  } catch (e) {
    console.error("Failed to fetch articles", e);
    loadError.value = true;
  } finally {
    loading.value = false;
    uni.stopPullDownRefresh();
  }
};

onMounted(() => {
  fetchData();
});

onPullDownRefresh(() => {
  fetchData();
});

const retryLoad = () => {
  fetchData();
};

const handleSign = async (item) => {
  try {
    uni.showLoading({ title: '签收中' });
    await approveArticle(item.id);
    item.isSigned = true;
    uni.hideLoading();
    uni.showToast({ title: '已成功签收答复！', icon: 'success' });
  } catch (error) {
    uni.hideLoading();
    uni.showToast({ title: '签收失败', icon: 'none' });
  }
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
.empty-state { text-align: center; color: #94a3b8; padding: 40px 0; font-size: 14px; }
.loading-state { text-align: center; color: #94a3b8; padding: 20px 0; font-size: 14px; }
.error-state { text-align: center; color: #ef4444; padding: 20px 0; font-size: 14px; }
</style>
