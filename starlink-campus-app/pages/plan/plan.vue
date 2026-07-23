<template>
  <view class="container">
    <view class="plan-card" v-for="item in planList" :key="item.id">
      <view class="header">
        <text class="week">{{ item.weekStart }} 周计划</text>
        <text class="theme">主题：{{ item.theme }}</text>
      </view>
      <view class="content">
        <view class="section">
          <text class="title">🎯 教学目标</text>
          <text class="desc">{{ item.goals }}</text>
        </view>
        <view class="section">
          <text class="title">🏃 户外活动</text>
          <text class="desc">{{ item.outdoorActivities || '无' }}</text>
        </view>
        <view class="section">
          <text class="title">🤝 家长配合</text>
          <text class="desc">{{ item.parentCoop }}</text>
        </view>
      </view>
    </view>
    <view class="empty" v-if="planList.length === 0">暂无周计划</view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const planList = ref([])

const getList = async () => {
  try {
    uni.showLoading({ title: '加载中' })
    const [err, res] = await uni.request({ url: '/api/kindergarten/plan/list' })
    if (!err && res.data.code === 200) planList.value = res.data.data.records || []
  } finally {
    uni.hideLoading()
    uni.stopPullDownRefresh()
  }
}

onMounted(() => { getList() })
</script>

<style scoped>
.container { padding: 20rpx; }
.plan-card { background: #fff; border-radius: 16rpx; margin-bottom: 30rpx; overflow: hidden; box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05); }
.header { background: #0071E3; padding: 30rpx; color: #fff; }
.week { display: block; font-size: 32rpx; font-weight: bold; margin-bottom: 10rpx; }
.theme { font-size: 26rpx; opacity: 0.9; }
.content { padding: 30rpx; }
.section { margin-bottom: 24rpx; }
.title { display: block; font-size: 28rpx; font-weight: bold; color: #333; margin-bottom: 10rpx; }
.desc { font-size: 26rpx; color: #64748b; line-height: 1.5; }
.empty { text-align: center; color: #94a3b8; padding: 100rpx 0; }
</style>
