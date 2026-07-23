<template>
  <view class="container">
    <view class="form-group">
      <text class="label">请假幼儿</text>
      <input class="input" v-model="form.studentName" placeholder="请输入幼儿姓名" />
    </view>
    <view class="form-group">
      <text class="label">请假类型</text>
      <picker mode="selector" :range="leaveTypes" @change="onTypeChange">
        <view class="picker">{{ form.leaveType || '请选择请假类型' }}</view>
      </picker>
    </view>
    <view class="form-group">
      <text class="label">开始日期</text>
      <picker mode="date" @change="onStartDateChange">
        <view class="picker">{{ form.startDate || '请选择开始日期' }}</view>
      </picker>
    </view>
    <view class="form-group">
      <text class="label">结束日期</text>
      <picker mode="date" @change="onEndDateChange">
        <view class="picker">{{ form.endDate || '请选择结束日期' }}</view>
      </picker>
    </view>
    <view class="form-group">
      <text class="label">请假原因</text>
      <textarea class="textarea" v-model="form.reason" placeholder="请输入详细请假原因"></textarea>
    </view>
    <button class="submit-btn" @click="submitForm">提交申请</button>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { request } from '@/utils/request.js'

const leaveTypes = ['事假', '病假']
const form = ref({
  studentName: '',
  leaveType: '',
  startDate: '',
  endDate: '',
  reason: ''
})

const onTypeChange = (e) => {
  form.value.leaveType = leaveTypes[e.detail.value]
}

const onStartDateChange = (e) => {
  form.value.startDate = e.detail.value
}

const onEndDateChange = (e) => {
  form.value.endDate = e.detail.value
}

const submitForm = () => {
  if (!form.value.studentName || !form.value.leaveType || !form.value.startDate || !form.value.reason) {
    uni.showToast({ title: '请填写完整信息', icon: 'none' })
    return
  }
  
  const payload = {
    applicantName: form.value.studentName + '家长',
    applicantRole: '家长',
    approvalType: '请假申请',
    reason: `【${form.value.leaveType}】 ${form.value.startDate} 至 ${form.value.endDate}，原因：${form.value.reason}`
  }

  request({
    url: '/kindergarten/oa/submit',
    method: 'POST',
    data: payload
  }).then(res => {
    uni.showToast({ title: '提交成功', icon: 'success' })
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  }).catch(err => {
    uni.showToast({ title: '提交失败，请重试', icon: 'none' })
  })
}
</script>

<style scoped>
.container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}
.form-group {
  margin-bottom: 20px;
  background: #fff;
  padding: 15px;
  border-radius: 8px;
}
.label {
  font-size: 14px;
  color: #333;
  margin-bottom: 10px;
  display: block;
}
.input, .picker, .textarea {
  font-size: 14px;
  color: #666;
  width: 100%;
}
.textarea {
  height: 80px;
}
.submit-btn {
  background-color: #007aff;
  color: #fff;
  border-radius: 25px;
  margin-top: 30px;
}
</style>
