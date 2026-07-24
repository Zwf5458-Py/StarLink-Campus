# 🌐 海星智联智慧校园与幼儿园综合管理平台 (StarLink Campus)

> **全栈技术选型**：Spring Boot 3.2 + MyBatis-Plus + Sa-Token (RBAC) + Vue 3 + Vite 5 + Uni-App (微信小程序) + Flowable 工作流 + Apple Liquid Glass 美学设计  
> **应用场景**：学前教育（幼儿园）、K-12 智慧校园、食品安全监管、后勤合规、AI 智慧园区与物联网联动

![海星智联数智监管 8 大动态图表看板](docs/images/dashboard_preview.png)
*(注：如预览图未正常显示，请查看 `docs/images/` 目录下的系统设计图)*

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/Vue.js-3.4.0-blue.svg)](https://vuejs.org/)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.5-orange.svg)](https://baomidou.com/)
[![Flyway](https://img.shields.io/badge/Flyway-V21--V23-red.svg)](https://flywaydb.org/)
[![Sa-Token](https://img.shields.io/badge/Sa--Token-RBAC-blue.svg)](https://sa-token.cc/)
[![Apple Glass UI](https://img.shields.io/badge/UI%20Design-Liquid%20Glass-blueviolet.svg)](https://apple.com)
[![License](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)](LICENSE)

---

## 📌 平台总体架构与版本声明

**海星智联智慧校园系统 (StarLink Campus)** 是一套专为高标准学前教育与智慧校园打造的软硬件一体化 SaaS 级解决方案。系统全盘符合教育部《3-6岁儿童学习与发展指南》、市场监管总局《餐饮服务食品安全操作规范》以及公安机关校园安防标准。

项目后端基于 **Spring Boot 3.2** 构建，底层采用严格的 **Flyway 数据库版本迁移控制 (V21-V23)**，全面剔除 Lombok 依赖，采用标准 Java JavaBean 架构；前台引入 **Apple macOS Sequoia / iOS Liquid Glass 极简高透毛玻璃视觉体系**；无线端打通微信小程序双端（家长端/教师端）与 21.5 寸 Android 智慧班牌。

系统目前已达到 **100% 建设完毕状态**，后端包含了 **44 个后端微服务控制器 (Controllers)**，全套 39 项自动化单元测试绿灯通过 (`BUILD SUCCESS`)。

---

## 🧠 AI 大模型应用全景与核心价值分析

项目深度融合了大语言模型（LLM）与多模态视觉 AI 模型，通过统一的 `AiGatewayService` 抽象网关接入，实现了 AI 技术在园区教务、健康防线、食安合规、家园沟通与行政办公中的全面赋能。

```
                    ┌────────────────────────────────────────────────────────┐
                    │            海星智联 AI 统一通信网关 (AiGatewayService)     │
                    └───────────────────────────┬────────────────────────────┘
                                                │
         ┌──────────────────────┬───────────────┴───────────────┬──────────────────────┐
         ▼                      ▼                               ▼                      ▼
┌──────────────────┐  ┌──────────────────┐            ┌──────────────────┐  ┌──────────────────┐
│   多模态视觉模型   │  │   LLM 文本生成模型 │            │ LLM JSON 结构化分析 │  │ RAG 向量知识库模型 │
│ (如 Qwen-VL 等)  │  │ (如 Qwen-Turbo)  │            │ (Prompt 约束解析)  │  │ (嵌入式向量检索)   │
└────────┬─────────┘  └────────┬─────────┘            └────────┬─────────┘  └────────┬─────────┘
         │                     │                               │                     │
         ▼                     ▼                               ▼                     ▼
 📸 晨检患处视觉诊断     📝 批量评语生成                🥗 食谱营养深度分析     🤖 智能园秘 7x24 客服
 🛡 班级圈图片安全审核   💬 家长回复草稿                📊 五维发展评估推导     📖 园区规章制度问答
                        ✉️ 行政通知智能撰写
```

### 🎯 7 大 AI 核心应用场景与业务价值矩阵

| AI 应用场景 | 采用模型/技术 | 业务作用描述 | 核心落地价值 (ROI) |
| :--- | :--- | :--- | :--- |
| **1. 晨检视觉医疗辅助诊断** | **多模态视觉模型** (Qwen-VL) | 保健医拍摄幼儿手掌、口腔或眼睛照片，AI 自动提取特征并进行比对研判。 | 辅助识别手足口病（口腔疱疹、手掌红斑）及红眼病，输出疑似症状与置信度打分，**筑牢入园第一道传染病拦截防线**。 |
| **2. 每日评语批量生成** | **LLM 文本生成模型** (Qwen-Turbo) | 教师勾选全班幼儿一日表现标签（饮食、午睡、情绪、活动），AI 并行批量生成结构化评语。 | 将教师期末/每日编写评语的时间**缩短 90%**，由“人工手写”转变为“AI生成+人工微调”，极大释放教务生产力。 |
| **3. 食谱营养真实分析** | **LLM JSON 结构化解析** | 分析每日菜谱，自动推算蛋白质分数、维生素分数、热量水平及三大营养素比例。 | 解决厨师/后勤人员缺乏儿童营养学专业知识的痛点，给出专家级改进建议，并**联动过敏原档案进行自动交叉警报**。 |
| **4. 家长消息智能回复草稿** | **幼教沟通心理学 Prompt** | 针对请假、发烧问询、碰撞质疑等高敏感家长消息，AI 自动撰写温和、得体且符合标准的回复草稿。 | 规范家园沟通口径，**缓解新手教师沟通焦虑**，有效避免因沟通不当引发的家园误解与矛盾。 |
| **5. 行政通知智能撰写** | **结构化公文生成引擎** | 输入活动关键词（如“端午放假三天、注意防溺水”），自动扩展为包含标题、正文、注意事项及落款的标准通知。 | 规范园区行政公文输出格式，确保表达温馨、得体，**提升园区行政办公效率 80%**。 |
| **6. 智能园秘 RAG 知识库问答** | **RAG 向量检索与问答** | 基于园区作息时间、退费规则、请假规定、接送制度等本地知识库文档提供智能答疑。 | 打造 **7x24 小时在线智能客服**，解答家长常见疑问，大幅降低教职工重复答疑的人力成本。 |
| **7. 内容安全与风控审核** | **语义及多模态风控模型** | 对家园共育班级圈发帖、评论进行语义级文本违规检查与图片多模态审核。 | 杜绝违规言论、敏感图片及不良信息在校园生态中传播，**确保平台合规与网络安全**。 |

---

## 🏛 全量 10 大核心业务板块与 44+ 子模块详细列表

```
 StarLink Campus 核心板块全景
 ├── 1. 权限控制与系统管理板块 (/system, /auth)
 ├── 2. 卫生保健与健康风控板块 (/health, /medication, /health-archive)
 ├── 3. 校园安全与考勤物联板块 (/attendance, /pickup, /patrol, /visitor, /school-bus, /smart-gate, /staff-attendance)
 ├── 4. 后勤精细化与食安合规板块 (/canteen, /asset, /fee, /salary, /weekly-menu)
 ├── 5. 园务办公与 Flowable 工作流板块 (/oa, /flowable, /notification, /article)
 ├── 6. 教学评估与学生发展板块 (/growth, /weekly-plan, /survey, /assessment, /graduate, /course)
 ├── 7. 招生营销与口碑裂变板块 (/enrollment, /enrollment-growth)
 ├── 8. 统一 AI 智能化融合网关板块 (/kindergarten/ai)
 ├── 9. 全局搜索、文件与交互工具板块 (/search, /file, /feedback, /contact)
 └── 10. 班牌终端与大屏数智监管板块 (/bigscreen, starlink-campus-board-ui, /iot)
```

---

### 🛡 1. 权限控制与系统管理板块 (`SystemController`, `AuthController`, `SystemConfigController`)

本板块是系统的核心基石，基于 **Sa-Token** 提供细粒度 RBAC 权限控制与多租户园区管理。

- **1.1 角色与权限控制中心 (`/system/role/*`)**
  - **内置 6 大精细化角色**：
    1. **园长 (ROLE_ADMIN)**：具备最高管理权限、全功能与大屏看板数据接入。
    2. **副园长/教务主任 (ROLE_DIRECTOR)**：负责教务教学、OA 流程审定、考勤退费审核与周计划批准。
    3. **班主任/任课教师 (ROLE_TEACHER)**：负责本班幼儿档案、班级圈发布、打卡与家园共育。
    4. **保健医/保育员 (ROLE_DOCTOR)**：负责晨/午检体温、AI 手足口诊断、食谱管理与喂药执行。
    5. **后勤安防管理员 (ROLE_SECURITY)**：负责校园巡检工单、维保、访客核验与滞留警报。
    6. **财务人员 (ROLE_FINANCE)**：负责缺勤退伙食费结算、缴费账单及在线收银对账。
  - **自定义角色与权限绑定**：支持动态创建角色、编辑角色描述、分配可访问的菜单树节点，以及绑定按钮级别的操作权限 (如 `student:add`, `student:export`, `oa:approve`, `attendance:refund`, `ai:health_vision`, `security:dispatch`)。
- **1.2 动态菜单树与页面权限 (`/system/menu/tree`)**
  - 提供模块、页面、按钮和大屏组件的四级树形节点控制。用户登录后根据分配的角色权限卡口下发菜单树。
- **1.3 身份认证与 Sa-Token 鉴权 (`/auth/*`)**
  - 支持手机号密码登录、微信小程序一键授权登录 (`/auth/login`)。
  - 获取当前登录用户详细信息、角色集合与操作权限列表 (`/auth/info`)。
  - 登出及全局 Token 强制失效 (`/auth/logout`)。
- **1.4 园区组织机构树 (`/system/dept/tree`)**
  - 支持“园区 ➔ 年级（大/中/小/托班）➔ 班级 ➔ 行政部门”的树形组织节点管理。
- **1.5 全局系统配置中心 (`SystemConfigController`)**
  - **参数动态调控**：晨检发热拦截门槛（默认 37.3℃）、月缺勤退伙食费算子（默认 20元/天）、食品留样销毁倒计时（默认 46小时）、微信 AppID/AppSecret 配置等。

---

### 🏥 2. 卫生保健与健康风控板块 (`KgHealthCheckController`, `MedicationController`, `HealthArchiveController`)

打造入园第一道健康防线，涵盖日常健康防护的方方面面。

- **2.1 晨检/午检体温监控与发热拦截**
  - **红外测温对接**：支持硬件及班牌上报体温，体温 ≥ 37.3℃ 自动拦截，生成隔离留观记录并实时通知保健医与家长。
  - **视觉 AI 辅助研判**：集成视觉 AI，辅助诊断手足口病（口腔疱疹、手掌红斑）与红眼病。
- **2.2 喂药管理全流程留痕 (每日高频应用)**
  - **家长提交申请**：家长在小程序填报药品名称、剂量、用药时间（如饭后30分钟）、储存方式与注意事项。
  - **教师接药确认**：班主任在教师端接药核对，电子确认接药。
  - **保健医按时执行**：保健医/班主任按时给药并拍照留痕，家长端实时收到服药通知。
- **2.3 幼儿疫苗接种与自动预警**
  - **档案管理**：记录一类/二类疫苗接种历史（如手足口 EV71、流感疫苗、百白破等）。
  - **到期自动提醒**：根据年龄自动推算应接种疫苗，自动向家长推送打针提醒，一键生成班级接种率统计。
- **2.4 幼儿体检档案 (`HealthArchiveController`)**
  - 支持入园体检及学期定期体检（身高、体重、视力、口腔龋齿、血红蛋白等），自动对比 WHO/国家儿童标准生成生长发育曲线。

---

### 🛡 3. 校园安全与考勤物联板块 (`KgAttendanceController`, `PickupPersonController`, `KgPatrolController`, `KgVisitorController`, `SchoolBusController`, `IotDeviceController`, `KgStaffAttendanceController`)

实现“人-车-门-巡”全维度的物理与逻辑安防。

- **3.1 幼儿考勤与缺勤退伙食费算法 (`KgAttendanceController`)**
  - **打卡识别**：支持刷脸、IC 卡、动态二维码打卡。
  - **退费自动结算**：月度缺勤天数 > 5 天时，按 20元/天 自动计算并生成退伙食费账单。
- **3.2 授权接送人安全审核 (`PickupPersonController`)**
  - **人脸/IC卡绑定**：每位幼儿绑定多名家长/委托接送人，记录身份证件与关系。
  - **防尾随与审核**：未审核人员刷卡禁止放行，打卡成功时弹屏比对接送人与幼儿关系。
- **3.3 校园安防巡检与工单联动 (`KgPatrolController`)**
  - **巡更打卡**：预设 24+ 巡更点位二维码/NFC，带防伪时间戳水印。
  - **自动转报修工单**：巡检发现设备故障时，一键转换为后勤维修工单。
- **3.4 访客二维码预约与滞留告警 (`KgVisitorController`)**
  - **线上预约**：访客提前填报事由，管理端审核生成入园通行码。
  - **超时滞留告警**：访客入园超过 15 分钟未签离，触发安防系统红色滞留警报。
- **3.5 智能校车路线与打卡防遗漏 (`SchoolBusController`)**
  - **路线排班**：维护校车接送路线、站点、司机及随车教师。
  - **打卡人数核对**：上下车刷卡，随车教师手机端实时核对人数，杜绝遗漏校车隐患。
- **3.6 智能闸机与通行数据联动 (`IotDeviceController`)**
  - 实时接收物理通道闸机上报的人脸/IC卡通行日志，区分幼儿、教工与访客。
- **3.7 教职工 GPS / 刷脸考勤 (`KgStaffAttendanceController`)**
  - 支持教职工 GPS 定位打卡与班级人脸打卡，支持多班次排班与出勤率统计。

---

### 📦 4. 后勤精细化与食安合规板块 (`CanteenSafetyController`, `AssetController`, `FeeController`, `SalarySlipController`, `WeeklyMenuController`)

保障园区食安合规与资产全生命周期流转。

- **4.1 48小时食堂食品留样与预警 (`CanteenSafetyController`)**
  - **留样记录**：录入留样重量（≥125g）、留样人、留样冰箱编号及留样时间。
  - **48小时倒计时预警**：留样时间达到 46-48 小时自动发出销毁提醒；超过 48 小时未销毁标记违规。
  - **销毁审批**：双人签字确认销毁并记录销毁人与销毁时间。
- **4.2 供应商资质与黑名单管理**
  - **资质档案**：记录供应商营业执照、食品经营许可证及到期时间。
  - **证照过期拦截**：证照到期自动锁定采购，支持黑名单一键拦截。
- **4.3 每周食谱与过敏原比对 (`WeeklyMenuController`)**
  - 支持每日多餐别菜谱，自动与学生过敏原档案进行交叉比对与警告提示。
- **4.4 固定资产与教具进销存 (`AssetController`)**
  - 登记教具、玩具、电子设备与办公用品。在入库、领用、归还、报废时，自动更新可用库存与总库存。
- **4.5 在线缴费与账单管理 (`FeeController`, `WxPaymentController`)**
  - 支持保教费、伙食费账单发布，对接微信 JSAPI 支付，提供漏斗式收缴率统计。
- **4.6 教职工薪酬加密查询 (`SalarySlipController`)**
  - 教职工专属通道，月度工资条明细一键加密查阅。

---

### 📋 5. 园务办公与 Flowable 工作流板块 (`KgOaController`, `FlowableProcessController`, `NotificationController`, `KgArticleController`)

自动化办公与信息透明化管理。

- **5.1 Flowable 工作流与多级审批 (`FlowableProcessController`, `KgOaController`)**
  - 支持请假、报销、采购、用印申请，可视化展示 `申请人 ➔ 节点组长 ➔ 园长` 审批链路与退回/转办。
- **5.2 园务日程与会议排期**
  - 维护全园周历、教研会议排期与重大活动协同。
- **5.3 公文通知与微信催读 (`NotificationController`)**
  - 行政通知下发，实时统计教师/家长的已读与未读状态，支持一键催读与公众号模板推送。
- **5.4 校园微官网与新闻发布 (`KgArticleController`)**
  - 校园微官网新闻动态发布、浏览量/微信分享统计、评论审核。

---

### 🎓 6. 教学评估与学生发展板块 (`GrowthRecordController`, `WeeklyPlanController`, `SurveyController`, `AssessmentController`, `GraduateController`, `KgCourseController`)

- **6.1 3-6 岁儿童发展评估 (`AssessmentController`)**
  - 基于《3-6岁儿童学习与发展指南》，对健康、语言、社会、科学、艺术五大领域进行学期打分，自动生成五维雷达图。
- **6.2 多媒体成长档案 (支持视频) (`GrowthRecordController`)**
  - 按时间轴展示幼儿在园照片、精彩视频，学期末支持一键导出成长手册。
- **6.3 班级课程与周计划 (`KgCourseController`, `WeeklyPlanController`)**
  - 教师在线编辑并发布本周主题目标、五大领域活动安排及户外锻炼计划。
- **6.4 问卷调查与 SQL 聚合优化 (`SurveyController`)**
  - 支持单选、多选、打分与文本建议，采用 MySQL `JSON_EXTRACT` 下推聚合算法避免高并发 OOM。
- **6.5 毕业生去向追踪档案 (`GraduateController`)**
  - 记录毕业生升入的公立/私立/国际小学名称及联系方式，分析毕业去向分布。

---

### 📈 7. 招生营销与口碑裂变板块 (`EnrollmentController`, `EnrollmentGrowthController`)

- **7.1 招生意向流转 (`EnrollmentController`)**
  - 招生线索填报与意向转化漏斗。
- **7.2 校园开放日活动 (`EnrollmentGrowthController`)**
  - 开放日试听课发布、名额容量限制、预约占位与现场扫码核销。
- **7.3 老带新转介绍裂变 (`EnrollmentGrowthController`)**
  - 推荐关系绑定、转化链路 (`LEAD ➔ VISITED ➔ ENROLLED ➔ REJECTED`) 追踪与推荐返佣状态管理。

---

### 🤖 8. 统一 AI 智能化融合网关板块 (`AiAssistantController`)

将 AI 深度融入业务场景，降本增效。

- **8.1 统一大模型 API 网关 (`AiGatewayService`)**
  - 基于 `@Async` 与超时熔断机制，统一收口通义千问、百度 AI 等合规模型。
  - 全量日志审计 (`kg_ai_log`)，记录场景、Prompt、Token 消耗与用户 ID。
- **8.2 每日评语批量生成 (`POST /batch-daily-comments`)** [第一批新增]
  - 上报全班幼儿饮食、午睡、情绪等表现，大模型批量输出温馨专属评语 JSON。
- **8.3 食谱营养深度分析 (`POST /menu-nutrition`)** [第一批增强]
  - 分析食谱蛋白质、维生素得分、热量水平、三大营养素比例，并给出专家改进建议。
- **8.4 家长消息智能回复草稿 (`POST /parent-reply-draft`)** [第一批新增]
  - 针对请假、发烧、碰撞等家长咨询，自动撰写得体、温和且符合规范的回复文本草稿。
- **8.5 行政通知智能撰写 (`POST /notice-draft`)** [第一批新增]
  - 输入活动关键词，一键生成带有标题、正文、注意事项及落款的标准通知。
- **8.6 智能园秘 RAG 知识库问答 (`POST /chat`)**
  - 基于园区作息、退费规则、接送规定，在线解答家长疑问。
- **8.7 AI 晨检视觉诊断与内容安全**
  - 辅助识别手足口病患处图像，并对家园共育发帖进行语义及图片敏感词审核。

---

### 🔍 9. 全局搜索、文件与交互工具板块 (`SearchController`, `FileUploadController`, `FeedbackController`, `ContactController`)

- **9.1 全局快捷高亮搜索 (`SearchController`)**
  - 支持 `⌘K` 快捷键，一键跨模块检索学生、教师、OA单据、安防单号及后勤资产。
- **9.2 多媒体文件对象存储 (`FileUploadController`)**
  - 支持本地及 MinIO 切片上传，生成带有绝对路径的图片与视频 URL。
- **9.3 家长意见反馈与跟进 (`FeedbackController`)**
  - 收集家长投诉与建议，提供园方处理跟进与回复流转。
- **9.4 园区加密通讯录 (`ContactController`)**
  - 维护教职工与家长加密通讯录，支持安全虚拟呼叫。

---

### 📺 10. 班牌终端与大屏数智监管板块 (`DashboardController`, `KgBoardController`, `KgClassBoardConfigController`, `starlink-campus-board-ui`)

- **10.1 21.5 寸智慧班牌硬件终端 UI (`starlink-campus-board-ui`)**
  - WebSocket 0 延时接收管理后台指令，展示班级文化、今日食谱、考勤统计、成长之星与紧急广播。
- **10.2 IoT 教室环境质量监测 (`IotDeviceController`)**
  - 接收班牌/传感器上报的教室温度、湿度、CO2 浓度及 PM2.5 浓度，超标时自动预警。
- **10.3 数智监管 8 大动态图表大屏看板 (`DashboardController`)**
  - 提供全园考勤趋势折线图、膳食营养占比饼图、各班发热对比柱状图、安防巡检完成率条形图等 8 大看板。

---

## 🛠 技术选型与部署启动指南

### 1. 软件依赖栈

| 依赖组件 | 推荐版本 | 作用 |
| :--- | :--- | :--- |
| **JDK** | OpenJDK 17 / 21 | 后端运行环境 |
| **MySQL** | 8.0+ | 核心关系型数据库 (Flyway 管理 schema) |
| **Redis** | 7.0+ | 缓存、Sa-Token 鉴权 Token 存储与分布式锁 |
| **Node.js** | v18.0.0+ | 前端构建环境 |
| **Maven** | 3.8+ | 后端依赖构建工具 |

### 2. 数据库初始化 (Flyway)

项目内置 Flyway 自动化迁移。只需在 MySQL 中创建空数据库 `starlink_campus`：
```sql
CREATE DATABASE IF NOT EXISTS `starlink_campus` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
启动后端服务时，Flyway 将自动依次执行脚本完成表结构与基础数据初始化：
- `V21__schema_phase_a.sql` (教务、健康、考勤基础表)
- `V22__schema_phase_b.sql` (留样、供应商、校车、资产台账、发展评估表)
- `V23__schema_phase_c.sql` (开放日、转介绍、毕业生、IoT环境、智能闸机表)

### 3. 项目运行步骤

#### ① 启动后端服务 (`starlink-campus-backend`)
```bash
cd starlink-campus-backend
# 编译并运行单元测试
mvn clean test
# 启动 Spring Boot 应用
mvn spring-boot:run
```
- 后端 API 端口：`http://localhost:8080/api`
- Swagger 文档：`http://localhost:8080/api/doc.html`

#### ② 启动 PC 管理后台 (`starlink-campus-admin-ui`)
```bash
cd starlink-campus-admin-ui
npm install
npm run dev
```
- 后台访问地址：`http://localhost`

#### ③ 启动智慧班牌终端 UI (`starlink-campus-board-ui`)
```bash
cd starlink-campus-board-ui
npm install
npm run dev
```
- 班牌 UI 访问地址：`http://localhost:3000`

#### ④ 启动微信小程序 (`starlink-campus-app`)
使用 **微信开发者工具** 打开目录：`/Users/oraclez/Desktop/zwf/StarLink Campus/starlink-campus-app`

---

## 📖 补充项目文档导航

- 📘 [项目交接与开发红线手册](HANDOVER.md)
- 📙 [全量功能交付与验证记录](walkthrough.md)
- 📗 [权威技术架构白皮书](docs/technical_architecture_whitepaper.md)
- 📕 [21.5寸智慧班牌沉浸壳配置指南](docs/board_kiosk_guide.md)

---
© 2026 海星智联科技有限公司 版权所有
