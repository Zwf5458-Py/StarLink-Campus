<template>
  <view class="container">
    <view class="week-selector">本周食谱</view>
    
    <view class="menu-list" v-if="menuList.length > 0">
      <view class="day-card" v-for="(day, index) in groupedMenu" :key="index">
        <view class="day-title">{{ day.dayOfWeek }}</view>
        <view class="meal-item" v-for="meal in day.meals" :key="meal.id">
          <view class="meal-type">{{ meal.mealType }}</view>
          <view class="dish-name">{{ meal.dishName }}</view>
          <view class="note" v-if="meal.nutritionNote">营养：{{ meal.nutritionNote }}</view>
          <view class="allergy" v-if="meal.allergyWarning">⚠️ 注意：含有过敏原</view>
        </view>
      </view>
    </view>
    <view class="empty" v-else>暂无食谱</view>
  </view>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const menuList = ref([])

const groupedMenu = computed(() => {
  const map = {}
  menuList.value.forEach(item => {
    if (!map[item.dayOfWeek]) map[item.dayOfWeek] = { dayOfWeek: item.dayOfWeek, meals: [] }
    map[item.dayOfWeek].meals.push(item)
  })
  return Object.values(map)
})

const getList = async () => {
  try {
    uni.showLoading({ title: '加载中' })
    const [err, res] = await uni.request({ url: '/api/kindergarten/menu/list' })
    if (!err && res.data.code === 200) menuList.value = res.data.data.records || []
  } finally {
    uni.hideLoading()
    uni.stopPullDownRefresh()
  }
}

onMounted(() => { getList() })
</script>

<style scoped>
.container { padding: 20rpx; }
.week-selector { font-size: 32rpx; font-weight: bold; text-align: center; margin-bottom: 30rpx; }
.day-card { background: #fff; padding: 20rpx; border-radius: 12rpx; margin-bottom: 20rpx; box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05); }
.day-title { font-size: 30rpx; font-weight: bold; color: #0071E3; margin-bottom: 20rpx; border-bottom: 2rpx solid #f1f5f9; padding-bottom: 10rpx; }
.meal-item { margin-bottom: 20rpx; }
.meal-type { font-size: 24rpx; color: #fff; background: #f59e0b; padding: 4rpx 12rpx; border-radius: 6rpx; display: inline-block; margin-bottom: 10rpx; }
.dish-name { font-size: 28rpx; color: #333; margin-bottom: 8rpx; }
.note { font-size: 24rpx; color: #64748b; }
.allergy { font-size: 22rpx; color: #ef4444; margin-top: 6rpx; }
.empty { text-align: center; color: #94a3b8; padding: 100rpx 0; }
</style>
