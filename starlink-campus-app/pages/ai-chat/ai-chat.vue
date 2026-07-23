<template>
  <view class="chat-container">
    <!-- 头部说明 -->
    <view class="chat-header">
      <text class="header-title">🤖 海星智联 24H AI 智能园秘</text>
      <text class="header-sub">随时解答作息、收费、请假退费与入园安全问题</text>
    </view>

    <!-- 消息滚动区 -->
    <scroll-view class="chat-scroll" scroll-y :scroll-into-view="scrollIntoView">
      <view v-for="(msg, index) in messages" :key="index" :id="'msg-' + index" :class="['msg-item', msg.role]">
        <view class="msg-avatar">{{ msg.role === 'ai' ? '🤖' : '👤' }}</view>
        <view class="msg-bubble">
          <text class="msg-text">{{ msg.content }}</text>
        </view>
      </view>
      <view v-if="loading" class="msg-item ai">
        <view class="msg-avatar">🤖</view>
        <view class="msg-bubble loading-bubble">
          <text class="loading-dot">AI 思考检索中...</text>
        </view>
      </view>
    </scroll-view>

    <!-- 底部输入区 -->
    <view class="chat-input-bar">
      <input class="input-field" v-model="inputText" placeholder="请输入您的疑问 (如：退费标准是什么？)" @confirm="sendQuestion" />
      <button class="send-btn" @click="sendQuestion" :disabled="loading">发送</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'

interface Message {
  role: 'user' | 'ai'
  content: string
}

const messages = ref<Message[]>([
  {
    role: 'ai',
    content: '您好！我是海星智联 AI 智能园秘 🤖。请问有什么可以帮您？例如您可以问我：“园区作息时间是怎样的？”、“缺勤怎么退费？”。'
  }
])

const inputText = ref('')
const loading = ref(false)
const scrollIntoView = ref('')

const sendQuestion = () => {
  const q = inputText.value.trim()
  if (!q || loading.value) return

  messages.value.push({ role: 'user', content: q })
  inputText.value = ''
  loading.value = true
  scrollToBottom()

  uni.request({
    url: 'http://localhost:8080/api/kindergarten/ai/chat?question=' + encodeURIComponent(q),
    method: 'POST',
    header: {
      'satoken': uni.getStorageSync('token') || ''
    },
    success: (res: any) => {
      const reply = res.data?.data || '抱歉，暂时未能查询到相关规则。'
      messages.value.push({ role: 'ai', content: reply })
    },
    fail: () => {
      messages.value.push({ role: 'ai', content: '网络较繁忙，请稍后重试。' })
    },
    complete: () => {
      loading.value = false
      scrollToBottom()
    }
  })
}

const scrollToBottom = () => {
  nextTick(() => {
    scrollIntoView.value = 'msg-' + (messages.value.length - 1)
  })
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
}
.chat-header {
  background: linear-gradient(135deg, #007aff, #5856d6);
  padding: 16px;
  color: #fff;
}
.header-title {
  font-size: 18px;
  font-weight: bold;
  display: block;
}
.header-sub {
  font-size: 12px;
  opacity: 0.8;
  margin-top: 4px;
}
.chat-scroll {
  flex: 1;
  padding: 16px;
}
.msg-item {
  display: flex;
  margin-bottom: 16px;
}
.msg-item.user {
  flex-direction: row-reverse;
}
.msg-avatar {
  width: 36px;
  height: 36px;
  border-radius: 18px;
  background: #e5e5ea;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}
.msg-bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 16px;
  margin: 0 8px;
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.msg-item.user .msg-bubble {
  background: #007aff;
  color: #ffffff;
}
.msg-text {
  font-size: 14px;
  line-height: 1.5;
  white-space: pre-wrap;
}
.chat-input-bar {
  display: flex;
  padding: 12px 16px;
  background: #ffffff;
  border-top: 1px solid #e5e5ea;
}
.input-field {
  flex: 1;
  height: 40px;
  background: #f2f2f7;
  border-radius: 20px;
  padding: 0 16px;
  font-size: 14px;
}
.send-btn {
  margin-left: 12px;
  height: 40px;
  line-height: 40px;
  background: #007aff;
  color: #fff;
  font-size: 14px;
  border-radius: 20px;
  padding: 0 20px;
}
</style>
