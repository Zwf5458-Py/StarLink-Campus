<template>
  <view class="app-container">
    <!-- 1. 顶部身份切换 (家长模式 ↔ 教师模式) -->
    <view class="identity-switch-bar">
      <view class="switch-pill">
        <view 
          class="pill-item" 
          :class="{ active: userRole === 'PARENT' }"
          @click="userRole = 'PARENT'"
        >
          <text>👨‍👩‍👧 家长模式</text>
        </view>
        <view 
          class="pill-item" 
          :class="{ active: userRole === 'TEACHER' }"
          @click="userRole = 'TEACHER'"
        >
          <text>👩‍🏫 教师工作台</text>
        </view>
      </view>
    </view>

    <!-- 2. 家长模式 (Parent Mode View) -->
    <view v-if="userRole === 'PARENT'" class="role-view">
      <!-- 孩子今日晨检与出勤状态卡片 -->
      <view class="child-status-card">
        <view class="child-header">
          <view class="avatar-box">👦</view>
          <view class="child-info">
            <text class="child-name">张小明</text>
            <text class="child-class">海星幼儿园 · 大(1)班</text>
          </view>
          <view class="status-tag green">
            <text>已安全入园</text>
          </view>
        </view>

        <view class="health-metrics-row">
          <view class="metric-item">
            <text class="m-label">今日晨检体温</text>
            <text class="m-val green-text">36.5 ℃</text>
          </view>
          <view class="metric-item">
            <text class="m-label">入园打卡时间</text>
            <text class="m-val">08:15:20</text>
          </view>
          <view class="metric-item">
            <text class="m-label">过敏源预警</text>
            <text class="m-val warning-text">无安全隐患</text>
          </view>
        </view>
      </view>

      <!-- 快捷工具：二维码接送卡 + 家长在线请假 -->
      <view class="quick-tools-grid">
        <view class="tool-btn-card blue" @click="showPassCodeModal = true">
          <text class="tool-icon">🎫</text>
          <text class="tool-title">出入接送二维码</text>
          <text class="tool-sub">刷码安全离园</text>
        </view>
        <view class="tool-btn-card yellow" @click="handleLeaveApply">
          <text class="tool-icon">📝</text>
          <text class="tool-title">家长在线请假</text>
          <text class="tool-sub">病假/事假报备</text>
        </view>
      </view>

      <!-- 今日园所健康食谱卡片 -->
      <view class="recipe-card">
        <view class="card-title">
          <text class="icon">🥗</text>
          <text class="title-text">今日园所健康食谱</text>
        </view>
        <view class="recipe-detail">
          <view class="meal-row">
            <text class="m-name">早餐</text>
            <text class="m-food">皮蛋瘦肉粥 + 蒸包</text>
          </view>
          <view class="meal-row highlight">
            <text class="m-name">午餐</text>
            <text class="m-food">红烧小排 + 炒青菜 + 虾仁豆腐</text>
          </view>
          <view class="meal-row">
            <text class="m-name">点心</text>
            <text class="m-food">新鲜香蕉 + 低脂牛奶</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 3. 教师工作台模式 (Teacher Mode View) -->
    <view v-else class="role-view">
      <view class="teacher-dashboard-card">
        <text class="card-header-title">👩‍🏫 大(1)班 班主任工作台</text>
        <view class="teacher-stats">
          <view class="t-stat-item">
            <text class="t-val">27/28</text>
            <text class="t-lbl">今日实到人数</text>
          </view>
          <view class="t-stat-item">
            <text class="t-val warning">1 人</text>
            <text class="t-lbl">请假避险</text>
          </view>
          <view class="t-stat-item">
            <text class="t-val success">0 人</text>
            <text class="t-lbl">晨检发热预警</text>
          </view>
        </view>
      </view>

      <!-- 教师快捷操作按钮组 -->
      <view class="teacher-actions-grid">
        <view class="action-btn-card" @click="handleBatchCheckIn">
          <text class="btn-icon">🩺</text>
          <text class="btn-text">一键体温补录</text>
        </view>
        <view class="action-btn-card" @click="handlePublishCircle">
          <text class="btn-icon">📷</text>
          <text class="btn-text">发布班级圈照片</text>
        </view>
        <view class="action-btn-card" @click="handleStaffGpsPunch">
          <text class="btn-icon">📍</text>
          <text class="btn-text">教职工GPS打卡</text>
        </view>
        <view class="action-btn-card" @click="handleOaApprove">
          <text class="btn-icon">📝</text>
          <text class="btn-text">园务OA审批</text>
        </view>
      </view>
    </view>

    <!-- 接送二维码 Modal 弹窗 -->
    <view v-if="showPassCodeModal" class="modal-mask">
      <view class="modal-box">
        <text class="modal-title">🚸 张小明 家长接送通行码</text>
        <view class="qrcode-placeholder">
          <text class="qr-code-text">PASS-8890214</text>
        </view>
        <text class="modal-sub">请向园区接送门禁机出示此码</text>
        <button class="close-btn" @click="showPassCodeModal = false">关闭</button>
      </view>
    </view>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const userRole = ref('PARENT'); // PARENT, TEACHER
const showPassCodeModal = ref(false);

const handleLeaveApply = () => {
  uni.showToast({ title: '请假表单已调起', icon: 'none' });
};

const handleBatchCheckIn = () => {
  uni.showToast({ title: '班级体温一键补录成功', icon: 'success' });
};

const handlePublishCircle = () => {
  uni.showToast({ title: '调起微信相机/相册', icon: 'none' });
};

const handleStaffGpsPunch = () => {
  uni.showToast({ title: 'GPS 定位成功: 园区打卡合规', icon: 'success' });
};

const handleOaApprove = () => {
  uni.showToast({ title: '调起 OA 审批列表', icon: 'none' });
};
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background-color: #f8fafc;
  padding: 16px;
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, "SF Pro Text", sans-serif;
}

/* 身份切换 */
.identity-switch-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}
.switch-pill {
  display: flex;
  background: rgba(0, 0, 0, 0.06);
  padding: 4px;
  border-radius: 20px;
}
.pill-item {
  padding: 6px 16px;
  border-radius: 16px;
  font-size: 13px;
  font-weight: bold;
  color: #64748b;
}
.pill-item.active {
  background: #0071e3;
  color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 113, 227, 0.3);
}

.child-status-card {
  background: #ffffff;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
  margin-bottom: 14px;
}
.child-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 14px;
}
.avatar-box { font-size: 32px; }
.child-info { display: flex; flex-direction: column; }
.child-name { font-size: 18px; font-weight: bold; color: #0f172a; }
.child-class { font-size: 12px; color: #94a3b8; }
.status-tag.green { margin-left: auto; background: rgba(52, 199, 89, 0.15); color: #248a3d; padding: 4px 10px; border-radius: 10px; font-size: 12px; font-weight: bold; }

.health-metrics-row { display: flex; justify-content: space-around; background: #f8fafc; border-radius: 12px; padding: 10px; }
.metric-item { display: flex; flex-direction: column; align-items: center; }
.m-label { font-size: 11px; color: #94a3b8; margin-bottom: 4px; }
.m-val { font-size: 14px; font-weight: bold; color: #0f172a; }
.green-text { color: #34c759; }
.warning-text { color: #38bdf8; }

.quick-tools-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 14px; }
.tool-btn-card { background: #ffffff; border-radius: 16px; padding: 14px; display: flex; flex-direction: column; gap: 4px; box-shadow: 0 4px 16px rgba(0,0,0,0.03); }
.tool-btn-card.blue { border-left: 4px solid #0071e3; }
.tool-btn-card.yellow { border-left: 4px solid #ffbd2e; }
.tool-icon { font-size: 24px; }
.tool-title { font-size: 14px; font-weight: bold; color: #0f172a; }
.tool-sub { font-size: 11px; color: #94a3b8; }

.recipe-card { background: #ffffff; border-radius: 16px; padding: 16px; box-shadow: 0 4px 16px rgba(0,0,0,0.03); }
.card-title { display: flex; align-items: center; gap: 6px; font-size: 15px; font-weight: bold; margin-bottom: 12px; }
.meal-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px dashed #f1f5f9; font-size: 13px; }
.meal-row.highlight { color: #34c759; font-weight: bold; }
.m-name { color: #64748b; }
.m-food { color: #0f172a; }

.teacher-dashboard-card { background: linear-gradient(135deg, #0071e3, #38bdf8); color: #fff; border-radius: 16px; padding: 18px; margin-bottom: 14px; box-shadow: 0 8px 24px rgba(0, 113, 227, 0.3); }
.card-header-title { font-size: 16px; font-weight: bold; margin-bottom: 14px; display: block; }
.teacher-stats { display: flex; justify-content: space-around; }
.t-stat-item { display: flex; flex-direction: column; align-items: center; }
.t-val { font-size: 22px; font-weight: bold; }
.t-lbl { font-size: 11px; opacity: 0.8; }

.teacher-actions-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.action-btn-card { background: #ffffff; border-radius: 14px; padding: 16px; display: flex; align-items: center; gap: 12px; box-shadow: 0 4px 16px rgba(0,0,0,0.03); }
.btn-icon { font-size: 24px; }
.btn-text { font-size: 13px; font-weight: bold; color: #0f172a; }

.modal-mask { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.7); display: flex; align-items: center; justify-content: center; z-index: 999; }
.modal-box { background: #ffffff; border-radius: 20px; padding: 24px; text-align: center; width: 80%; }
.modal-title { font-size: 16px; font-weight: bold; margin-bottom: 16px; display: block; }
.qrcode-placeholder { background: #f1f5f9; padding: 30px; border-radius: 12px; margin-bottom: 12px; }
.qr-code-text { font-family: monospace; font-size: 20px; font-weight: bold; color: #0071e3; }
.modal-sub { font-size: 12px; color: #94a3b8; margin-bottom: 16px; display: block; }
.close-btn { background: #0071e3; color: #fff; border: none; padding: 10px 24px; border-radius: 12px; font-size: 14px; font-weight: bold; }
</style>
