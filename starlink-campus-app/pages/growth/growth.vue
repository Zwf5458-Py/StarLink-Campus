<template>
  <view class="container">
    <scroll-view class="tab-scroll" scroll-x>
      <view 
        v-for="(tab, index) in tabs" 
        :key="index" 
        :class="['tab-item', activeTab === index ? 'active' : '']"
        @click="activeTab = index"
      >
        {{ tab }}
      </view>
    </scroll-view>

    <view class="timeline" v-if="records.length > 0">
      <view class="timeline-item" v-for="item in records" :key="item.id">
        <view class="time">{{ item.recordDate }}</view>
        <view class="content">
          <view class="title">{{ item.category }}：{{ item.recordValue }}</view>
          <view class="comment" v-if="item.teacherComment">{{ item.teacherComment }}</view>
          <image v-if="item.photoUrl" :src="item.photoUrl" mode="aspectFill" class="photo" />
        </view>
      </view>
    </view>
    <view class="empty" v-else>暂无记录</view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const tabs = ['身高', '体重', '语言', '社交', '运动', '艺术']
const activeTab = ref(0)
const records = ref([])

const getList = async () => {
  try {
    uni.showLoading({ title: '加载中' })
    const [err, res] = await uni.request({
      url: '/api/kindergarten/growth/list',
      data: { category: tabs[activeTab.value] }
    })
    if (!err && res.data.code === 200) {
      records.value = res.data.data.records || []
    }
  } finally {
    uni.hideLoading()
    uni.stopPullDownRefresh()
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.container { padding: 20rpx; }
.tab-scroll { white-space: nowrap; margin-bottom: 30rpx; }
.tab-item { display: inline-block; padding: 10rpx 30rpx; margin-right: 20rpx; border-radius: 30rpx; background: #f1f5f9; color: #64748b; }
.tab-item.active { background: #0071E3; color: #fff; }
.timeline-item { display: flex; margin-bottom: 30rpx; }
.time { width: 160rpx; font-size: 24rpx; color: #94a3b8; }
.content { flex: 1; background: #fff; padding: 20rpx; border-radius: 12rpx; box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.05); }
.title { font-size: 28rpx; font-weight: bold; margin-bottom: 10rpx; }
.comment { font-size: 26rpx; color: #475569; margin-bottom: 10rpx; }
.photo { width: 100%; height: 200rpx; border-radius: 8rpx; }
.empty { text-align: center; color: #94a3b8; margin-top: 100rpx; }
</style>
