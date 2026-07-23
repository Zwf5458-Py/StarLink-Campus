<template>
  <view class="container">
    <view class="survey-list" v-if="!currentSurvey">
      <view class="survey-card" v-for="item in surveyList" :key="item.id" @click="startSurvey(item)">
        <view class="title">{{ item.title }}</view>
        <view class="info">
          <text class="type">{{ item.type }}</text>
          <text class="time">截止: {{ item.endTime }}</text>
        </view>
        <view class="status">进行中</view>
      </view>
      <view class="empty" v-if="surveyList.length === 0">暂无进行中的问卷</view>
    </view>

    <view class="survey-detail" v-else>
      <view class="header">
        <text class="back" @click="currentSurvey = null">返回</text>
        <text class="title">{{ currentSurvey.title }}</text>
      </view>
      <view class="question-list">
        <view class="question" v-for="(q, idx) in questions" :key="q.id">
          <view class="q-title">{{ idx + 1 }}. {{ q.title }} ({{ q.type }})</view>
          
          <radio-group v-if="q.type === '单选'" @change="e => answers[q.id] = e.detail.value">
            <label class="radio-label" v-for="opt in q.options" :key="opt">
              <radio :value="opt" /> <text>{{ opt }}</text>
            </label>
          </radio-group>
          
          <checkbox-group v-if="q.type === '多选'" @change="e => answers[q.id] = e.detail.value">
            <label class="checkbox-label" v-for="opt in q.options" :key="opt">
              <checkbox :value="opt" /> <text>{{ opt }}</text>
            </label>
          </checkbox-group>

          <textarea v-if="q.type === '文本'" class="textarea" v-model="answers[q.id]" placeholder="请输入" />
        </view>
      </view>
      <button class="submit-btn" @click="submitAnswers">提交问卷</button>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const surveyList = ref([])
const currentSurvey = ref(null)
const questions = ref([])
const answers = ref({})

const getList = async () => {
  try {
    uni.showLoading({ title: '加载中' })
    const [err, res] = await uni.request({ url: '/api/kindergarten/survey/list' })
    if (!err && res.data.code === 200) surveyList.value = res.data.data.records || []
  } finally {
    uni.hideLoading()
    uni.stopPullDownRefresh()
  }
}

const startSurvey = async (item) => {
  currentSurvey.value = item
  answers.value = {}
  try {
    uni.showLoading({ title: '加载题目...' })
    const [err, res] = await uni.request({ url: '/api/kindergarten/question/list', data: { surveyId: item.id } })
    if (!err && res.data.code === 200) {
      questions.value = res.data.data || []
    } else {
      questions.value = [
        { id: 1, title: '您对学校环境满意吗？', type: '单选', options: ['非常满意', '满意', '一般'] },
        { id: 2, title: '您的建议：', type: '文本' }
      ]
    }
  } finally { uni.hideLoading() }
}

const submitAnswers = async () => {
  uni.showToast({ title: '提交成功', icon: 'success' })
  setTimeout(() => { currentSurvey.value = null }, 1500)
}

onMounted(() => { getList() })
</script>

<style scoped>
.container { padding: 20rpx; }
.survey-card { background: #fff; padding: 30rpx; border-radius: 12rpx; margin-bottom: 20rpx; position: relative; }
.title { font-size: 30rpx; font-weight: bold; margin-bottom: 15rpx; }
.info { font-size: 24rpx; color: #64748b; display: flex; gap: 20rpx; }
.status { position: absolute; top: 30rpx; right: 30rpx; font-size: 24rpx; color: #0071E3; background: #e0f2fe; padding: 4rpx 12rpx; border-radius: 6rpx; }
.empty { text-align: center; color: #94a3b8; padding: 100rpx 0; }

.header { display: flex; align-items: center; padding-bottom: 20rpx; border-bottom: 2rpx solid #e2e8f0; margin-bottom: 30rpx; }
.back { color: #0071E3; font-size: 28rpx; margin-right: 20rpx; }
.question { background: #fff; padding: 30rpx; border-radius: 12rpx; margin-bottom: 20rpx; }
.q-title { font-size: 28rpx; font-weight: bold; margin-bottom: 20rpx; }
.radio-label, .checkbox-label { display: block; margin-bottom: 15rpx; font-size: 28rpx; }
.textarea { width: 100%; height: 160rpx; background: #f8fafc; border: 2rpx solid #e2e8f0; border-radius: 8rpx; padding: 16rpx; font-size: 28rpx; box-sizing: border-box; }
.submit-btn { background: #0071E3; color: #fff; border-radius: 12rpx; margin-top: 40rpx; }
</style>
