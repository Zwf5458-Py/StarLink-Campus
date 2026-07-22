<template>
  <view class="my-container">
    <!-- 用户个人名片卡 -->
    <view class="user-profile-card">
      <view class="avatar-large">👨‍👩‍👧</view>
      <view class="user-meta">
        <text class="u-name">张建国 (张小明 家长) - 验证</text>
        <text class="u-phone">139 **** 5678</text>
        <text class="u-badge">海星大(1)班 认证家长</text>
      </view>
    </view>

    <!-- 缺勤伙食费退费明细单 -->
    <view class="section-card">
      <text class="section-title">📅 月度缺勤退还伙食费明细</text>
      <view class="refund-box">
        <view class="ref-row">
          <text class="r-lbl">7 月份应到天数</text>
          <text class="r-val">22 天</text>
        </view>
        <view class="ref-row">
          <text class="r-lbl">实际缺勤天数</text>
          <text class="r-val warning">6 天 (触发退费算子)</text>
        </view>
        <view class="ref-row highlight">
          <text class="r-lbl">自动退还伙食费</text>
          <text class="r-val green">￥ 120.00 元</text>
        </view>
      </view>
    </view>

    <!-- 个人设置功能列表 -->
    <view class="menu-list">
      <view class="menu-item" @click="handleAction('绑定的幼儿档案')">
        <text class="m-title">👦 绑定的幼儿档案 (张小明)</text>
        <text class="m-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleAction('过敏源避险档案')">
        <text class="m-title">⚠️ 过敏源避险档案 (芒果, 海鲜)</text>
        <text class="m-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleAction('接送通行码记录')">
        <text class="m-title">🎫 历史接送通行码记录</text>
        <text class="m-arrow">›</text>
      </view>
    </view>

    <button class="logout-btn" @click="handleLogout">退出登录</button>
  </view>
</template>

<script setup>
const handleAction = (name) => {
  uni.showToast({ title: `查看 ${name}`, icon: 'none' });
};

const handleLogout = () => {
  uni.showToast({ title: '已安全退出登录', icon: 'success' });
};
</script>

<style scoped>
.my-container { min-height: 100vh; background: #f8fafc; padding: 16px; box-sizing: border-box; }

.user-profile-card { background: linear-gradient(135deg, #0071e3, #42a5f5); color: #fff; border-radius: 20px; padding: 20px; display: flex; gap: 14px; align-items: center; margin-bottom: 16px; box-shadow: 0 8px 24px rgba(0,113,227,0.3); }
.avatar-large { font-size: 40px; }
.user-meta { display: flex; flex-direction: column; gap: 4px; }
.u-name { font-size: 17px; font-weight: bold; }
.u-phone { font-size: 12px; opacity: 0.8; }
.u-badge { font-size: 10px; background: rgba(255,255,255,0.2); padding: 2px 8px; border-radius: 8px; font-weight: bold; width: fit-content; }

.section-card { background: #ffffff; border-radius: 16px; padding: 16px; margin-bottom: 16px; box-shadow: 0 4px 16px rgba(0,0,0,0.03); }
.section-title { font-size: 14px; font-weight: bold; color: #0f172a; margin-bottom: 12px; display: block; }
.refund-box { display: flex; flex-direction: column; gap: 8px; }
.ref-row { display: flex; justify-content: space-between; font-size: 13px; color: #475569; }
.ref-row.highlight { font-weight: bold; padding-top: 6px; border-top: 1px dashed #e2e8f0; }
.r-val.warning { color: #d97706; font-weight: bold; }
.r-val.green { color: #34c759; font-size: 16px; }

.menu-list { background: #ffffff; border-radius: 16px; overflow: hidden; margin-bottom: 20px; box-shadow: 0 4px 16px rgba(0,0,0,0.03); }
.menu-item { display: flex; justify-content: space-between; align-items: center; padding: 14px 16px; border-bottom: 1px solid #f1f5f9; font-size: 13px; font-weight: 600; color: #0f172a; }
.menu-item:last-child { border-bottom: none; }
.m-arrow { font-size: 18px; color: #cbd5e1; }

.logout-btn { background: #ff3b30; color: #fff; border: none; padding: 10px; border-radius: 12px; font-size: 14px; font-weight: bold; }
</style>
