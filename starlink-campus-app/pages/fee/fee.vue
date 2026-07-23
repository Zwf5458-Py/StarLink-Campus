<template>
  <view class="container">
    <view class="tabs">
      <text :class="['tab', activeTab === 0 ? 'active' : '']" @click="activeTab = 0">待缴账单</text>
      <text :class="['tab', activeTab === 1 ? 'active' : '']" @click="activeTab = 1">缴费历史</text>
    </view>

    <view class="list" v-if="activeTab === 0">
      <view class="bill-card" v-for="item in unpaidList" :key="item.id">
        <view class="header">
          <text class="title">{{ item.itemName }}</text>
          <text class="amount">¥{{ item.amount }}</text>
        </view>
        <view class="desc">截止日期：{{ item.deadline }}</view>
        <button class="pay-btn" @click="handlePay(item)">去缴费</button>
      </view>
      <view class="empty" v-if="unpaidList.length === 0">暂无待缴账单</view>
    </view>

    <view class="list" v-if="activeTab === 1">
      <view class="bill-card" v-for="item in historyList" :key="item.id">
        <view class="header">
          <text class="title">{{ item.itemName }}</text>
          <text class="amount">¥{{ item.amount }}</text>
        </view>
        <view class="desc">支付时间：{{ item.payTime }}</view>
        <view class="status paid">已缴费</view>
      </view>
      <view class="empty" v-if="historyList.length === 0">暂无记录</view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const activeTab = ref(0)
const unpaidList = ref([])
const historyList = ref([])

const getList = async () => {
  try {
    uni.showLoading({ title: '加载中' })
    if (activeTab.value === 0) {
      const [err, res] = await uni.request({ url: '/api/kindergarten/fee/payment/unpaid' })
      if (!err && res.data.code === 200) unpaidList.value = res.data.data || []
    } else {
      const [err, res] = await uni.request({ url: '/api/kindergarten/fee/payment/list' })
      if (!err && res.data.code === 200) historyList.value = res.data.data.records || []
    }
  } finally {
    uni.hideLoading()
    uni.stopPullDownRefresh()
  }
}

const handlePay = (item) => {
  uni.showModal({
    title: '确认缴费',
    content: `是否确认支付 ¥${item.amount}？`,
    success: async (res) => {
      if (res.confirm) {
        uni.showToast({ title: '模拟支付成功', icon: 'success' })
        getList()
      }
    }
  })
}

onMounted(() => { getList() })
</script>

<style scoped>
.container { padding: 20rpx; }
.tabs { display: flex; background: #fff; padding: 20rpx; border-radius: 12rpx; margin-bottom: 20rpx; }
.tab { flex: 1; text-align: center; font-size: 28rpx; color: #64748b; }
.tab.active { color: #0071E3; font-weight: bold; }
.bill-card { background: #fff; padding: 30rpx; border-radius: 12rpx; margin-bottom: 20rpx; }
.header { display: flex; justify-content: space-between; margin-bottom: 20rpx; }
.title { font-size: 30rpx; font-weight: bold; }
.amount { font-size: 32rpx; color: #ef4444; font-weight: bold; }
.desc { font-size: 24rpx; color: #94a3b8; margin-bottom: 20rpx; }
.pay-btn { background: #0071E3; color: #fff; border-radius: 8rpx; font-size: 28rpx; }
.status.paid { color: #10b981; font-size: 24rpx; }
.empty { text-align: center; color: #94a3b8; padding: 100rpx 0; }
</style>
