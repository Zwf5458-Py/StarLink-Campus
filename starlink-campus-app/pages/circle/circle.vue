<template>
  <view class="circle-container">
    <view class="header-title-box">
      <text class="title">📷 班级圈精彩风采</text>
      <button class="post-btn" @click="handlePublish">发布照片</button>
    </view>

    <view class="posts-list">
      <view v-for="post in posts" :key="post.id" class="post-card">
        <view class="user-row">
          <text class="avatar">👩‍🏫</text>
          <view class="user-info">
            <text class="author">{{ post.author }}</text>
            <text class="time">{{ post.time }} · {{ post.className }}</text>
          </view>
        </view>
        <text class="content">{{ post.content }}</text>
        <view class="action-row">
          <view class="like-btn" @click="handleLike(post)">
            <text>❤️ {{ post.likesCount || 0 }} 点赞</text>
          </view>
          <text class="comment-count">💬 {{ post.commentsCount || 0 }} 评论</text>
        </view>
      </view>
    </view>
    <view v-if="posts.length === 0 && !loading" class="empty-state">📷 暂无班级圈动态，快来发布第一条吧！</view>
    <view v-if="loading" class="loading-state">加载中...</view>
    <view v-if="loadError" class="error-state"><text>加载失败</text><button @click="retryLoad">重试</button></view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { onPullDownRefresh } from '@dcloudio/uni-app';
import { getCircleList, publishCircle } from '../../api/circle';

const posts = ref([]);
const loading = ref(true);
const loadError = ref(false);

const fetchData = async () => {
  loading.value = true;
  loadError.value = false;
  try {
    const res = await getCircleList();
    if (res && res.data) {
      posts.value = res.data;
    }
  } catch (error) {
    console.error('Failed to fetch circle list', error);
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

const handleLike = (post) => {
  if (!post.likesCount) post.likesCount = 0;
  post.likesCount++;
  uni.showToast({ title: '点赞成功 ❤️', icon: 'success' });
};

const handlePublish = () => {
  uni.chooseImage({
    count: 1,
    success: async (res) => {
      const tempFilePath = res.tempFilePaths[0];
      try {
        uni.showLoading({ title: '发布中' });
        // Assuming there is an upload API or we just send the URL for now
        await publishCircle({
          content: '新照片动态',
          photoUrls: tempFilePath,
          className: '大(1)班',
          authorId: 201
        });
        uni.hideLoading();
        uni.showToast({ title: '发布成功' });
        fetchData();
      } catch (e) {
        uni.hideLoading();
        uni.showToast({ title: '发布失败', icon: 'none' });
      }
    }
  });
};
</script>

<style scoped>
.circle-container { min-height: 100vh; background: #f8fafc; padding: 16px; box-sizing: border-box; }
.header-title-box { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.title { font-size: 18px; font-weight: bold; color: #0f172a; }
.post-btn { background: #0071e3; color: #fff; border: none; padding: 6px 14px; border-radius: 12px; font-size: 12px; font-weight: bold; }
.posts-list { display: flex; flex-direction: column; gap: 12px; }
.post-card { background: #ffffff; border-radius: 16px; padding: 16px; box-shadow: 0 4px 16px rgba(0,0,0,0.03); }
.user-row { display: flex; gap: 10px; align-items: center; margin-bottom: 10px; }
.avatar { font-size: 28px; }
.user-info { display: flex; flex-direction: column; }
.author { font-weight: bold; font-size: 14px; color: #0f172a; }
.time { font-size: 11px; color: #94a3b8; }
.content { font-size: 13px; color: #334155; line-height: 1.5; margin-bottom: 12px; display: block; }
.action-row { display: flex; gap: 16px; align-items: center; font-size: 12px; color: #64748b; font-weight: bold; }
.like-btn { background: rgba(255,45,85,0.1); color: #ff2d55; padding: 4px 10px; border-radius: 10px; }
.empty-state { text-align: center; color: #94a3b8; padding: 40px 0; font-size: 14px; }
.loading-state { text-align: center; color: #94a3b8; padding: 20px 0; font-size: 14px; }
.error-state { text-align: center; color: #ef4444; padding: 20px 0; font-size: 14px; }
</style>
