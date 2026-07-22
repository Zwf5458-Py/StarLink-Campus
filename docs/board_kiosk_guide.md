# 🖥️ 4.5 班牌端：Web 班牌 + WebView 容器 (全量配置与部署指南)

## 📌 方案背景与架构设计

针对 **26 台 21.5 寸 RK3288 + Android 7.1 硬件终端**，传统自研 Android 原生 App 存在升级维护难、版本不一的问题。本系统采用**全 Web 技术栈 (Vue 3 + WebView/Kiosk 沉浸容器)**，后端集成 CompreFace 私有化人脸识别 API 与 WebSocket 长连接广播网关。

---

## 🌟 细项功能覆盖对照明细表

| 序号 | 4.5 截图规范功能 | 对应班牌 UI 模块 (`starlink-campus-board-ui/App.vue`) | 技术与后端 API 支撑 | 完善状态 |
| :--- | :--- | :--- | :--- | :--- |
| **1** | **班级管理 & 班风** | 顶部 Banner `大(1)班 - 葵花班 (教室 A101)` + Slogan | `KgClassBoardConfig.java` | **✅ 100% 完善** |
| **2** | **通知发送 / 广播** | `🔔 园区最新通知公告浮层` + 紧急广播模式 | `WebSocketServer.java` 网关下发 | **✅ 100% 完善** |
| **3** | **请假 & 考勤实时** | `📊 今日考勤与体温检测` (27/28 人出勤进度条) | `StudentAttendanceServiceImpl.java` | **✅ 100% 完善** |
| **4** | **人脸识别 / 校园卡** | `📸 刷脸/校园卡打卡` + 额温检测与感应弹窗 | CompreFace 向量比对 + JNI 串口监听 | **✅ 100% 完善** |
| **5** | **课表 (活动排期)** | `📅 今日班级活动排期` (时间轴与当前高亮) | `scheduleList` 实时更新 | **✅ 100% 完善** |
| **6** | **天气 & 湿度** | `☀️ 26℃ 晴朗 \| 湿度 65% \| PM2.5 12 优` | 实时网络 Weather API 注入 | **✅ 100% 完善** |
| **7** | **时间与日期** | 动态数字时钟 `22:20:45` 包含星期农历 | JS RequestAnimationFrame | **✅ 100% 完善** |
| **8** | **签到与成长之星** | `⭐ 本周成长之星与风采` (文明小能手) | 班级圈互动与考勤联动 | **✅ 100% 完善** |

---

## 🛠️ RK3288 Android 7.1 WebView 极速开机自启部署

### 1. 安装 Android WebView 沉浸容器包
在 26 台 21.5 寸 RK3288 终端上安装轻量级 Kiosk 应用（例如 Fully Single App Kiosk 或 自研 SimpleWebViewActivity）：

```java
public class MainActivity extends Activity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        // 加载智慧班牌全 Web 页面
        mWebView.loadUrl("http://192.168.1.100:3000");
    }
}
```

### 2. Chrome Kiosk 命令行（Linux/Android 系统）
```bash
chrome --kiosk --noerrdialogs --disable-infobars --check-for-update-interval=31536000 http://localhost:3000
```
