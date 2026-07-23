<template>
  <div class="apple-app-container" :class="[themeClass]">
    <!-- 1. 苹果 macOS 极简透亮毛玻璃顶栏 (TopBar) -->
    <header class="apple-header">
      <!-- 左侧：专属海星智慧校园设计 LOGO 徽标 -->
      <div class="brand-zone">
        <div class="starlink-logo-box">
          <svg class="starlink-logo-svg" viewBox="0 0 36 36" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="36" height="36" rx="10" fill="url(#logo-grad)"/>
            <path d="M18 6L21.4 13.5L29.5 14.4L23.4 19.9L25.1 27.8L18 23.7L10.9 27.8L12.6 19.9L6.5 14.4L14.6 13.5L18 6Z" fill="#FFFFFF" stroke="rgba(255,255,255,0.4)" stroke-width="1.2"/>
            <circle cx="18" cy="17" r="2.5" fill="#FFD60A"/>
            <defs>
              <linearGradient id="logo-grad" x1="0" y1="0" x2="36" y2="36" gradientUnits="userSpaceOnUse">
                <stop stop-color="#0071E3"/>
                <stop offset="1" stop-color="#42A5F5"/>
              </linearGradient>
            </defs>
          </svg>
        </div>
        <div class="brand-title">
          <span class="title-text">海星智慧校园</span>
          <span class="sub-badge">PRO</span>
        </div>
      </div>

      <!-- 顶栏中央：苹果 Sequoia 风格全局搜索框 (Global Search) -->
      <div class="apple-search-bar">
        <span class="search-icon">🔍</span>
        <input 
          type="text" 
          v-model="searchQuery" 
          @keyup.enter="handleSearch"
          placeholder="搜索幼儿姓名、教职工、OA审批或安防单号 (⌘K)" 
          class="search-input" 
        />
      </div>

      <!-- 顶栏右侧：高对比度管理员与快捷控件 -->
      <div class="apple-status-zone">
        <div class="user-pill">
          <span class="avatar-dot"></span>
          <span class="user-name">园长 (系统管理员)</span>
        </div>
        
        <!-- 🔔 消息与提醒中心按钮 (带有高亮未读红点) -->
        <button class="icon-circle-btn" @click="showNoticeModal = true" title="事件提醒">
          🔔
          <span v-if="unreadCount > 0" class="badge-dot">{{ unreadCount }}</span>
        </button>

        <!-- ⚙️ 系统偏好与设置按钮 -->
        <button class="icon-circle-btn" @click="showSettingsModal = true" title="偏好设置">
          ⚙️
        </button>
      </div>
    </header>

    <!-- 2. 主体区：左侧极简导航 + 右侧毛玻璃工作区 -->
    <div class="apple-body">
      <!-- 左侧 macOS Sidebar 独立统一导航栏 -->
      <aside class="apple-sidebar">
        <div class="nav-section">
          <div class="section-label">校园业务架构</div>
          <nav class="sidebar-nav">
            <button 
              v-for="item in sidebarMenuItems"
              :key="item.path"
              class="nav-pill"
              :class="{ active: activeRoute === item.path }"
              @click="navigate(item.path)"
            >
              <span class="pill-icon">{{ item.icon }}</span>
              <span class="pill-title">{{ item.title }}</span>
            </button>
          </nav>
        </div>

        <!-- 下部：苹果 iOS 风格立体九宫格快捷工具箱 -->
        <div class="apple-shortcut-box">
          <div class="section-label">快捷应用中心</div>
          <div class="app-grid">
            <div 
              v-for="app in shortcutApps" 
              :key="app.name" 
              class="app-icon-card"
              @click="navigate(app.path)"
            >
              <div class="icon-wrapper" :style="{ background: app.bg }">
                <span>{{ app.icon }}</span>
              </div>
              <span class="app-name">{{ app.name }}</span>
            </div>
          </div>
        </div>
      </aside>

      <!-- 右侧：工作区 View Container -->
      <main class="apple-workspace">
        <div class="workspace-glass-panel">
          <router-view />
        </div>
      </main>
    </div>

    <!-- 3. 苹果极简页脚 Statusbar -->
    <footer class="apple-footer">
      <span> 海星智联智慧校园 · Apple Liquid Glass Edition</span>
      <div class="footer-meta">
        <span class="status-indicator">🟢 系统服务与 WebSocket 网关长连接正常</span>
        <span>|</span>
        <span>v2.5.0 Sequoia</span>
      </div>
    </footer>

    <!-- 4. 🔔 事件提醒与消息中心 (Modal 弹窗) -->
    <el-dialog v-model="showNoticeModal" title="🔔 园区实时事件与预警提醒中心" width="560px" class="apple-modal">
      <div class="notice-list">
        <div v-for="item in noticeList" :key="item.id" class="notice-item-card" :class="item.type">
          <div class="notice-top">
            <span class="notice-type-tag">{{ item.typeText }}</span>
            <span class="notice-time">{{ item.time }}</span>
          </div>
          <div class="notice-title">{{ item.title }}</div>
          <div class="notice-content">{{ item.content }}</div>
        </div>
      </div>
      <template #footer>
        <button class="apple-btn-secondary" @click="clearNotices">一键全部已读</button>
        <button class="apple-btn-primary" @click="showNoticeModal = false">关闭</button>
      </template>
    </el-dialog>

    <!-- 5. ⚙️ 园区系统偏好与参数设置 (Modal 弹窗: 支持白天/晚上/跟随系统) -->
    <el-dialog v-model="showSettingsModal" title="⚙️ 园区系统偏好与主题设置" width="620px" class="apple-modal">
      <el-form :model="settingsForm" label-width="160px">
        <el-form-item label="外观界面主题">
          <el-radio-group v-model="settingsForm.themeMode" style="display: flex; gap: 10px; flex-wrap: wrap;">
            <el-radio label="light">☀️ 白天模式</el-radio>
            <el-radio label="dark">🌙 晚上/深色模式</el-radio>
            <el-radio label="system">💻 跟随系统</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="AI 大模型平台">
          <el-select v-model="settingsForm.aiPlatform" placeholder="请选择大模型平台" style="width: 100%;">
            <el-option label="通义千问 (Qwen-Max)" value="qwen" />
            <el-option label="智谱清言 (ChatGLM)" value="zhipu" />
            <el-option label="百川智能 (Baichuan)" value="baichuan" />
            <el-option label="月之暗面 (Kimi)" value="moonshot" />
            <el-option label="本地部署模型 (Ollama/LM Studio)" value="local" />
            <el-option label="其他自定义 (API URL/KEY)" value="custom" />
          </el-select>
        </el-form-item>
        <el-form-item label="API 接口地址" v-if="settingsForm.aiPlatform === 'custom' || settingsForm.aiPlatform === 'local'">
          <el-input v-model="settingsForm.customApiUrl" placeholder="例如: http://localhost:11434/api/generate" />
        </el-form-item>
        <el-form-item label="API Key 凭证" v-if="settingsForm.aiPlatform === 'custom'">
          <el-input v-model="settingsForm.customApiKey" type="password" placeholder="请输入 API Key 凭证" show-password />
        </el-form-item>
        <el-form-item label="晨检发热警告阈值">
          <el-input-number v-model="settingsForm.feverTemp" :precision="1" :step="0.1" :min="36.5" :max="39.0" />
          <span class="unit-text">℃</span>
        </el-form-item>
        <el-form-item label="访客滞留告警时长">
          <el-input-number v-model="settingsForm.overtimeMinutes" :min="5" :max="60" />
          <span class="unit-text">分钟</span>
        </el-form-item>
        <el-form-item label="月度缺勤退费算子">
          <el-input-number v-model="settingsForm.refundRate" :min="10" :max="50" />
          <span class="unit-text">元/天 (满5天生效)</span>
        </el-form-item>
        <el-form-item label="WebSocket 班牌广播">
          <el-switch v-model="settingsForm.wsEnable" active-text="开启0延时心跳" />
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="apple-btn-secondary" @click="showSettingsModal = false">取消</button>
        <button class="apple-btn-primary" @click="saveSettings">保存参数与主题设置</button>
      </template>
    </el-dialog>

    <!-- 6. 🔍 搜索结果中心 (Modal 弹窗) -->
    <el-dialog v-model="showSearchModal" title="🔍 全域搜索结果" width="560px" class="apple-modal">
      <div v-if="searchResults.length === 0" style="text-align: center; color: #94a3b8; padding: 20px;">
        未找到相关数据
      </div>
      <div v-else class="notice-list">
        <div v-for="(item, index) in searchResults" :key="index" class="notice-item-card" style="cursor: pointer" @click="goToResult(item.path)">
          <div class="notice-top">
            <span class="notice-type-tag" style="color: #0071e3">{{ item.category }}</span>
          </div>
          <div class="notice-title">{{ item.text }}</div>
          <div class="notice-content">点击前往关联模块 👉</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';

const router = useRouter();
const route = useRoute();

const searchQuery = ref('');
const showNoticeModal = ref(false);
const showSettingsModal = ref(false);
const showSearchModal = ref(false);
const searchResults = ref([]);

const activeRoute = computed(() => route.path);

const noticeList = ref([]);
const unreadCount = computed(() => noticeList.value.length);

const settingsForm = ref({
  themeMode: 'light',
  aiPlatform: 'qwen',
  customApiUrl: '',
  customApiKey: '',
  feverTemp: 37.3,
  overtimeMinutes: 15,
  refundRate: 20,
  wsEnable: true
});

const fetchNotices = async () => {
  try {
    const res = await request.get('/notification/unread');
    if (res && res.data) {
      noticeList.value = res.data;
    }
  } catch (error) {
    console.error("加载消息中心失败", error);
  }
};

const fetchSettings = async () => {
  try {
    const res = await request.get('/kindergarten/system/config');
    if (res && res.data) {
      settingsForm.value = { ...settingsForm.value, ...res.data };
    }
  } catch (error) {
    console.error("加载系统配置失败", error);
  }
};

onMounted(() => {
  fetchNotices();
  fetchSettings();
});

const themeClass = computed(() => {
  if (settingsForm.value.themeMode === 'dark') {
    return 'dark-theme';
  } else if (settingsForm.value.themeMode === 'system') {
    const hours = new Date().getHours();
    return (hours >= 19 || hours < 7) ? 'dark-theme' : 'light-theme';
  }
  return 'light-theme';
});

const sidebarMenuItems = [
  { path: '/', title: '园所概览看板', icon: '📊' },
  { path: '/permission', title: '角色与权限管理', icon: '🛡️' },
  { path: '/system', title: '智慧校园平台', icon: '🏛️' },
  { path: '/portal', title: '校园微官网', icon: '🌐' },
  { path: '/oa', title: '园务OA审批中心', icon: '📝' },
  { path: '/student', title: '幼儿档案与过敏源', icon: '👶' },
  { path: '/attendance', title: '刷脸考勤与缺勤退费', icon: '📅' },
  { path: '/patrol', title: '安防巡检与工单', icon: '🛡️' },
  { path: '/visitor', title: '访客预约与滞留', icon: '🎫' },
  { path: '/health', title: '晨检体温与食谱', icon: '🩺' },
  { path: '/family', title: '家园共育平台', icon: '🌟' },
  { path: '/interest', title: '拓展活动: 兴趣托育班', icon: '🎨' },
  { path: '/bigscreen', title: '8大数智监管大屏', icon: '🖥️' }
];

const shortcutApps = [
  { name: '权限管理', icon: '🛡️', bg: 'linear-gradient(135deg, #af52de, #8e44ad)', path: '/permission' },
  { name: '平台系统', icon: '🏛️', bg: 'linear-gradient(135deg, #007aff, #0056b3)', path: '/system' },
  { name: '微官网', icon: '🌐', bg: 'linear-gradient(135deg, #5ac8fa, #32ade6)', path: '/portal' },
  { name: 'OA审批', icon: '📝', bg: 'linear-gradient(135deg, #ffcc00, #e6b800)', path: '/oa' },
  { name: '每周食谱', icon: '🥗', bg: 'linear-gradient(135deg, #af52de, #8e44ad)', path: '/health' },
  { name: '安防巡检', icon: '🛡️', bg: 'linear-gradient(135deg, #34c759, #30b04c)', path: '/patrol' },
  { name: '访客登记', icon: '🎫', bg: 'linear-gradient(135deg, #ff9500, #ff5e00)', path: '/visitor' },
  { name: '班级圈', icon: '📷', bg: 'linear-gradient(135deg, #ff2d55, #d81b43)', path: '/family' },
  { name: '数智大屏', icon: '📊', bg: 'linear-gradient(135deg, #1d1d1f, #434344)', path: '/bigscreen' }
];

const navigate = (path) => {
  router.push(path);
};

const handleSearch = async () => {
  if (searchQuery.value) {
    try {
      const res = await request.get(`/search?q=${encodeURIComponent(searchQuery.value)}`);
      if (res && res.data) {
        searchResults.value = res.data;
        showSearchModal.value = true;
      }
    } catch (error) {
      ElMessage.error('全域检索失败');
    }
  }
};

const goToResult = (path) => {
  showSearchModal.value = false;
  router.push(path);
};

const clearNotices = () => {
  noticeList.value = [];
  ElMessage.success('已全部标记为已读！');
};

const saveSettings = async () => {
  try {
    await request.put('/kindergarten/system/config', settingsForm.value);
    showSettingsModal.value = false;
    const modeText = settingsForm.value.themeMode === 'light' ? '☀️ 白天模式' : settingsForm.value.themeMode === 'dark' ? '🌙 晚上深色模式' : '💻 跟随系统模式';
    ElMessage.success(`🎉 参数已成功保存至服务端！当前界面外观已应用: ${modeText}`);
  } catch (error) {
    ElMessage.error('保存设置失败');
  }
};
</script>

<style>
/* 全局苹果 SF Pro 字体与极简质感 */
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, "SF Pro Text", "SF Pro Display", "PingFang SC", "Helvetica Neue", sans-serif;
  background: #eef2f7;
  color: #0f172a;
  -webkit-font-smoothing: antialiased;
  transition: background-color 0.3s ease;
}

.apple-app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
  background: radial-gradient(circle at top left, #f8fafc, #e2e8f0);
  transition: all 0.3s ease;
}

/* 🌙 深色/晚上主题模式样式 (Liquid Dark Glass) */
.apple-app-container.dark-theme {
  background: radial-gradient(circle at top left, #0f172a, #1e1b4b);
  color: #f8fafc;
}
.apple-app-container.dark-theme .apple-header {
  background: rgba(15, 23, 42, 0.8);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
.apple-app-container.dark-theme .brand-title { color: #ffffff; }
.apple-app-container.dark-theme .apple-search-bar { background: rgba(255, 255, 255, 0.08); border-color: rgba(255, 255, 255, 0.15); }
.apple-app-container.dark-theme .search-input { color: #ffffff; }
.apple-app-container.dark-theme .user-pill { background: rgba(255, 255, 255, 0.1); color: #ffffff; border-color: rgba(255, 255, 255, 0.2); }
.apple-app-container.dark-theme .icon-circle-btn { background: rgba(255, 255, 255, 0.15); color: #ffffff; border-color: rgba(255, 255, 255, 0.2); }
.apple-app-container.dark-theme .apple-sidebar { background: rgba(15, 23, 42, 0.7); border-color: rgba(255, 255, 255, 0.1); }
.apple-app-container.dark-theme .nav-pill { color: #cbd5e1; }
.apple-app-container.dark-theme .workspace-glass-panel { background: rgba(15, 23, 42, 0.75); border-color: rgba(255, 255, 255, 0.12); color: #ffffff; }

/* 1. 苹果顶栏 (Translucent Apple TopBar) */
.apple-header {
  height: 56px;
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  z-index: 1000;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
}

.brand-zone { display: flex; align-items: center; gap: 10px; }
.starlink-logo-box { width: 34px; height: 34px; display: flex; align-items: center; justify-content: center; }
.starlink-logo-svg { width: 100%; height: 100%; filter: drop-shadow(0 4px 8px rgba(0, 113, 227, 0.3)); }

.brand-title { display: flex; align-items: center; gap: 6px; font-weight: 800; font-size: 17px; color: #0f172a; letter-spacing: -0.3px; }
.sub-badge { font-size: 10px; background: linear-gradient(135deg, #0071e3, #42a5f5); color: #fff; padding: 1px 6px; border-radius: 8px; font-weight: 700; }

.apple-search-bar { display: flex; align-items: center; gap: 8px; background: rgba(0, 0, 0, 0.04); border: 1px solid rgba(0, 0, 0, 0.06); border-radius: 12px; padding: 6px 14px; width: 400px; transition: all 0.2s ease; }
.apple-search-bar:focus-within { background: #ffffff; border-color: #0071e3; box-shadow: 0 0 0 3px rgba(0, 113, 227, 0.15); }
.search-icon { font-size: 13px; color: #94a3b8; }
.search-input { border: none; background: transparent; outline: none; width: 100%; font-size: 13px; color: #0f172a; }

.apple-status-zone { display: flex; align-items: center; gap: 10px; }
.user-pill { display: flex; align-items: center; gap: 8px; background: rgba(255, 255, 255, 0.9); border: 1px solid rgba(0, 0, 0, 0.08); padding: 5px 12px; border-radius: 20px; font-size: 13px; font-weight: 600; color: #0f172a; box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04); }
.avatar-dot { width: 8px; height: 8px; background: #34c759; border-radius: 50%; }
.icon-circle-btn { position: relative; width: 34px; height: 34px; border-radius: 50%; border: 1px solid rgba(0, 0, 0, 0.08); background: #ffffff; cursor: pointer; display: flex; align-items: center; justify-content: center; font-size: 15px; transition: all 0.2s ease; }
.icon-circle-btn:hover { background: #f1f5f9; transform: scale(1.05); }
.badge-dot { position: absolute; top: -2px; right: -2px; background: #ff3b30; color: #fff; font-size: 10px; font-weight: bold; padding: 1px 5px; border-radius: 10px; border: 1.5px solid #fff; }

.apple-body { flex: 1; display: flex; overflow: hidden; padding: 12px; gap: 12px; }

.apple-sidebar { width: 240px; background: rgba(255, 255, 255, 0.65); backdrop-filter: blur(20px); border: 1px solid rgba(255, 255, 255, 0.8); border-radius: 16px; display: flex; flex-direction: column; justify-content: space-between; padding: 14px; box-shadow: 0 8px 30px rgba(0, 0, 0, 0.04); }
.section-label { font-size: 11px; font-weight: 700; color: #94a3b8; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 8px; padding-left: 6px; }

.sidebar-nav { display: flex; flex-direction: column; gap: 3px; }
.nav-pill { display: flex; align-items: center; gap: 10px; border: none; background: transparent; padding: 8px 12px; border-radius: 10px; font-size: 13px; font-weight: 600; color: #334155; cursor: pointer; text-align: left; transition: all 0.2s ease; }
.nav-pill:hover { background: rgba(0, 113, 227, 0.08); color: #0071e3; }
.nav-pill.active { background: #0071e3; color: #ffffff; box-shadow: 0 4px 12px rgba(0, 113, 227, 0.3); }

.apple-shortcut-box { background: rgba(248, 250, 252, 0.8); border: 1px solid rgba(0, 0, 0, 0.05); border-radius: 14px; padding: 10px; }
.app-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; }
.app-icon-card { display: flex; flex-direction: column; align-items: center; gap: 4px; cursor: pointer; transition: transform 0.2s ease; }
.app-icon-card:hover { transform: translateY(-3px); }
.icon-wrapper { width: 36px; height: 36px; border-radius: 10px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 16px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15); }
.app-name { font-size: 10px; font-weight: 600; color: #475569; }

.apple-workspace { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.workspace-glass-panel { flex: 1; background: rgba(255, 255, 255, 0.75); backdrop-filter: blur(25px); border: 1px solid rgba(255, 255, 255, 0.9); border-radius: 16px; padding: 20px; overflow: auto; box-shadow: 0 10px 40px rgba(0, 0, 0, 0.05); }

.apple-footer { height: 28px; background: rgba(255, 255, 255, 0.6); border-top: 1px solid rgba(0, 0, 0, 0.06); display: flex; justify-content: space-between; align-items: center; padding: 0 20px; font-size: 11px; color: #64748b; font-weight: 500; }
.footer-meta { display: flex; gap: 10px; }
.status-indicator { color: #34c759; font-weight: 600; }

.notice-list { display: flex; flex-direction: column; gap: 10px; }
.notice-item-card { padding: 12px 14px; border-radius: 12px; background: rgba(0,0,0,0.03); border: 1px solid rgba(0,0,0,0.06); }
.notice-item-card.danger { border-left: 4px solid #ff3b30; background: rgba(255, 59, 48, 0.05); }
.notice-item-card.warning { border-left: 4px solid #ffbd2e; background: rgba(255, 189, 46, 0.05); }
.notice-top { display: flex; justify-content: space-between; font-size: 11px; margin-bottom: 4px; }
.notice-type-tag { font-weight: bold; }
.notice-time { color: #94a3b8; }
.notice-title { font-size: 13px; font-weight: bold; color: #0f172a; margin-bottom: 2px; }
.notice-content { font-size: 12px; color: #475569; }

.unit-text { margin-left: 8px; font-size: 12px; color: #64748b; }
.apple-btn-primary { background: #0071e3; color: #fff; border: none; padding: 8px 16px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; }
.apple-btn-secondary { background: #e2e8f0; color: #334155; border: none; padding: 8px 16px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; margin-right: 8px; }
</style>
