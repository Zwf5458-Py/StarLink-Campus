# 📘 海星智联智慧校园系统 · 权威技术架构白皮书
> **StarLink Campus Technical Architecture Whitepaper v3.0.0 Release**

---

## 目录
1. [一、概述与总体设计理念](#一概述与总体设计理念)
2. [二、系统整体架构图 (Architecture Diagram)](#二系统整体架构图-architecture-diagram)
3. [三、后端架构与 44 个微服务控制器映射](#三后端架构与-44-个微服务控制器映射)
4. [四、统一 AI 智能化融合网关架构](#四统一-ai-智能化融合网关架构)
5. [五、IoT 物联网与智能硬件接入架构](#五iot-物联网与智能硬件接入架构)
6. [六、Sa-Token 权限鉴权与 RBAC 矩阵](#六sa-token-权限鉴权与-rbac-矩阵)
7. [七、核心业务算法与精细化算子](#七核心业务算法与精细化算子)
8. [八、数据库与 Flyway 版本控制 (25+ 核心业务表)](#八数据库与-flyway-版本控制-25-核心业务表)
9. [九、前端 Liquid Glass 设计系统与移动端架构](#九前端-liquid-glass-设计系统与移动端架构)
10. [十、容器编排与 Docker 运维规约](#十容器编排与-docker-运维规约)

---

## 一、概述与总体设计理念

海星智联智慧校园系统（StarLink Campus）是一套专为学前教育（幼儿园）与智慧校园打造的工业级软硬件一体化 SaaS 级系统。系统设计遵循以下核心工程理念：

1. **高可用与低延时 (High Availability & Low Latency)**：
   - 终端与服务端之间采用基于 Spring WebSocket 的网关长连接广播，消息下发延时控制在 50ms 以内；
   - 前端集成离线渐进退避机制（Offline Fallback），确保在网络瞬断时界面依然优雅稳定运行。

2. **数据驱动与精细化算子 (Data-Driven & Business Operators)**：
   - **全员幼儿过敏源档案与每周食谱交叉比对引擎**：自动预警潜在食物过敏风险；
   - **缺勤退费算子**：月累计缺勤天数 > 5 天，按 20 元/天 自动计算并下扣退还伙食费；
   - **48小时食品留样预警算子**：留样时间达到 46-48 小时自动触发销毁告警；
   - **资产库存加减计算**：入库、领用、归还、报废实时计算可用与总库存。

3. **原生规范与环境防护 (No-Lombok Standard)**：
   - 为防范 CI/CD 及多 JDK 版本下注解处理器的隐藏 Bug，项目全量采用 Java 原生规范编写 Getter/Setter，杜绝隐式反射风险。

4. **极简与现代视觉美学 (Modern Liquid Glass System)**：
   - 全盘引入 Apple macOS Sequoia / iOS Liquid Glass 极简立体毛玻璃与高对比度按钮设计系统，彻底告别传统政企软件的沉闷感。

---

## 二、系统整体架构图 (Architecture Diagram)

```mermaid
graph TD
    subgraph 终端与多端接入层 (Client Layer)
        A1[21.5寸 RK3288 智慧班牌 UI] -->|WebSocket 长连接| B1(API Gateway 统一网关)
        A2[苹果 Liquid Glass Web 管理后台] -->|HTTP/REST API| B1
        A3[微信小程序 starlink-campus-app] -->|HTTP/REST/JSAPI| B1
        A4[IoT 环境传感器 / 智能闸机硬件] -->|HTTP POST / MQTT| B1
    end

    subgraph 核心应用与服务层 (Application Layer)
        B1 --> C1[Spring Boot 3.2 核心业务引擎]
        C1 --> C2[Sa-Token 权限鉴权与 RBAC 模块]
        C1 --> C3[Flowable 工作流审批引擎]
        C1 --> C4[WebSocket 班牌广播与消息催读]
        C1 --> C5[健康风控/留样/退费/资产算子]
    end

    subgraph 统一 AI 智能化融合网关 (AI Engine Layer)
        C1 --> D1[AiGatewayService 统一 API 网关]
        D1 --> D11[Qwen-VL 多模态视觉模型 - 晨检诊断]
        D1 --> D12[Qwen-Turbo 文本生成模型 - 评语/通知/回复]
        D1 --> D13[LLM JSON 结构化分析 - 食谱营养]
        D1 --> D14[RAG 本地向量知识库 - 7x24 智能园秘]
        D1 --> D15[AI 内容与多媒体安全审核引擎]
    end

    subgraph 存储与持久化层 (Storage & Persistence)
        C1 --> E1[Redis 7.0 缓存/Session/分布式锁]
        C1 --> E2[MinIO / 本地切片对象存储]
        C1 --> E3[(MySQL 8.0 数据库 - Flyway V21-V23)]
    end
```

---

## 三、后端架构与 44 个微服务控制器映射

系统后端包含 **44 个 RESTful 控制器**，按功能领域划分如下：

### 1. 权限与系统控制
- `SystemController` (`/system/**`)：角色列表、RBAC 授权绑定、菜单树下发。
- `AuthController` (`/auth/**`)：Sa-Token 登录、令牌解密、用户信息获取与登出。
- `SystemConfigController` (`/system/config/**`)：发热门槛、退费标准等全局参数配置。

### 2. 卫生保健与健康风控
- `KgHealthCheckController` (`/api/kindergarten/health/**`)：晨/午检体温表及过敏源检查。
- `MedicationController` (`/api/medication/**`)：家长喂药申请、教师接药、保健医给药拍照。
- `HealthArchiveController` (`/api/health-archive/**`)：定期体检档案与生长发育曲线。

### 3. 安全、考勤与物联网
- `KgAttendanceController` (`/attendance/**`)：幼儿打卡、考勤月报、退伙食费结算。
- `PickupPersonController` (`/pickup/**`)：接送人审核、人脸/IC卡绑定与防尾随。
- `KgPatrolController` (`/patrol/**`)：24+ 巡更点打卡、防伪水印与报修单转化。
- `KgVisitorController` (`/visitor/**`)：访客二维码预约、签到与 15 分钟滞留警报。
- `SchoolBusController` (`/api/school-bus/**`)：校车路线排班与上下车打卡。
- `IotDeviceController` (`/api/iot/**`)：教室 PM2.5/CO2 环境采集与智能闸机通行回传。
- `KgStaffAttendanceController` (`/staff-attendance/**`)：教职工 GPS 与刷脸排班考勤。

### 4. 食安与后勤合规
- `CanteenSafetyController` (`/api/canteen/safety/**`)：48小时留样倒计时预警与销毁、供应商黑名单。
- `AssetController` (`/api/asset/**`)：教具/固定资产入库、领用、归还与报废算子。
- `WeeklyMenuController` (`/weekly-menu/**`)：每周食谱与过敏原比对。
- `FeeController` & `WxPaymentController` (`/fee/**`, `/wx-payment/**`)：账单发布、微信 JSAPI 支付。
- `SalarySlipController` (`/salary/**`)：教职工月度工资条加密查询。

### 5. 园务办公与 Flowable
- `KgOaController` & `FlowableProcessController` (`/oa/**`, `/process/**`)：流程定义、待办/已办节点追踪。
- `NotificationController` (`/notification/**`)：通知发布、已读/未读统计与一键催读。
- `KgArticleController` (`/article/**`)：微官网新闻发布与分享统计。

### 6. 教学评估与学生发展
- `AssessmentController` (`/api/assessment/**`)：《指南》五大领域打分与雷达图计算。
- `GrowthRecordController` (`/growth/**`)：多媒体成长档案与照片/视频时间轴。
- `WeeklyPlanController` & `KgCourseController` (`/plan/**`, `/course/**`)：教学周计划与班级课程。
- `SurveyController` (`/survey/**`)：问卷调查与 MySQL `JSON_EXTRACT` 下推聚合。
- `GraduateController` (`/api/graduate/**`)：毕业生去向档案与校友录。

### 7. 招生拓客与 AI 助手
- `EnrollmentGrowthController` (`/api/enrollment-growth/**`)：开放日活动与老带新转介绍裂变。
- `AiAssistantController` (`/kindergarten/ai/**`)：批量评语、食谱分析、回复草稿、智能通知、RAG 园秘。
- `SearchController` (`/search/**`)：`⌘K` 全局高亮秒级搜索。

---

## 四、统一 AI 智能化融合网关架构

项目抽象了 `AiGatewayService` 网关服务，实现了高可用、解耦的大模型调用：

1. **并行批量评语算法**：前端上报结构化 `List<StudentPerformanceVO>`，AI 网关通过异步并行或批量 Prompt 聚合，输出标准 JSON 数组，大幅提升生成吞吐量。
2. **多模态视觉诊断管道**：接入 Qwen-VL 等多模态模型，对保健医上传的患处图片进行卷积特征提取，输出疑似诊断与置信度。
3. **内容安全风控中间件**：在用户发帖或生成评语时，自动触发 `checkTextSecurityAsync` 与 `checkMediaSecurityAsync` 异步文本/图像审查，违规即时拦截。

---

## 五、IoT 物物联网与智能硬件接入架构

1. **智慧班牌 (21.5寸 Android 7.1 RK3288)**：通过 WebSocket 与后端保持心跳，收到紧急广播指令后 50ms 内弹屏展示。
2. **环境质量传感器**：班牌/传感器每 5 分钟向 `/api/iot/env/report` POST 报文（温度、湿度、CO2、PM2.5）。当 $\text{PM2.5} > 100$ 或 $\text{CO2} > 1000\text{ppm}$ 时，系统自动标记 `warningTriggered = 1` 并触发控制警报。
3. **智能防尾随闸机**：物理通道闸机在通行瞬间向 `/api/iot/gate/pass` 上传抓拍图像、通行方向与人员 ID。

---

## 六、Sa-Token 权限鉴权与 RBAC 矩阵

针对不同用户角色，系统在接口层配置了严格的 `@SaCheckRole`：

| 角色代码 (`RoleCode`) | 角色名称 | 可访问接口示例 | 授权操作权限 (`ActionPerms`) |
| :--- | :--- | :--- | :--- |
| `ROLE_ADMIN` | 园长 (系统管理员) | 全接口无限制 | `student:*`, `oa:*`, `attendance:*`, `ai:*`, `security:*` |
| `ROLE_DIRECTOR` | 副园长 / 教务主任 | 教学周计划、OA终审、考勤退费审核 | `oa:approve`, `oa:final_approve`, `attendance:refund` |
| `ROLE_TEACHER` | 班主任 / 任课教师 | 幼儿档案、打卡、成长记录、评语生成 | `student:add`, `student:edit`, `oa:submit`, `attendance:check` |
| `ROLE_DOCTOR` | 保健医生 / 保育员 | 晨检体温、AI手足口诊断、留样销毁 | `ai:health_vision`, `student:edit`, `health:destroy` |
| `ROLE_SECURITY` | 后勤安防管理员 | 巡检打卡、访客签到、维保工单 | `security:dispatch`, `patrol:check` |
| `ROLE_FINANCE` | 财务人员 | 退伙食费结算、在线收银、账单核销 | `attendance:refund`, `fee:writeoff` |

---

## 七、核心业务算法与精细化算子

### 1. 缺勤退伙食费算法
当月累计缺勤天数 > 5 天时，算子自动生效：
$$\text{退费金额} = \max(0, \text{当月应出勤天数} - \text{实际出勤天数}) \times 20\text{元/天}$$

### 2. 食品留样倒计时预警算法
令留样时间为 $T_{sample}$，当前时间为 $T_{now}$，差值为 $\Delta T = T_{now} - T_{sample}$：
$$\text{Sample Status} = \begin{cases} \text{NORMAL}, & \Delta T < 46\text{小时} \\ \text{WARNING (需立即销毁)}, & 46\text{小时} \le \Delta T \le 48\text{小时} \\ \text{VIOLATION (已超时违规)}, & \Delta T > 48\text{小时 且 未销毁} \end{cases}$$

---

## 八、数据库与 Flyway 版本控制 (25+ 核心业务表)

所有数据库变更由 Flyway 统一管控：

1. **V21 (Phase A 核心基石)**：
   - `kg_medication_application`, `kg_medication_execution`（喂药申请与执行表）
   - `kg_nap_record`（午睡监控表）
   - `kg_daily_report`（一日表现日报表）
   - `kg_feedback_ticket`（家园意见反馈表）
   - `kg_physical_exam`（幼儿体检档案表）
2. **V22 (Phase B 合规与评估)**：
   - `kg_development_assessment`（3-6岁指南五维评估表）
   - `kg_food_sample`, `kg_food_supplier`（48小时留样与供应商表）
   - `kg_school_bus`, `kg_bus_record`（校车及上下车打卡表）
   - `kg_asset_item`, `kg_asset_record`（固定资产与进销存表）
3. **V23 (Phase C IoT 与招生拓客)**：
   - `kg_open_day_event`（校园开放日活动表）
   - `kg_referral_record`（老带新转介绍裂变表）
   - `kg_graduate_record`（毕业生去向追踪表）
   - `kg_environment_monitor`（教室环境质量监测表）
   - `kg_smart_gate_record`（智能闸机通行日志表）

---

## 九、前端 Liquid Glass 设计系统与移动端架构

- **视觉规范**：引入 macOS Sequoia 高透毛玻璃（Glassmorphism）、微光渐变边框、高对比度图标与矢量 5 角金黄海星 Logo。
- **快捷键**：支持全局 `⌘K` 唤起秒级高亮搜索框。
- **跨端微信小程序 (`starlink-campus-app`)**：支持 `👨‍👩‍👧 家长模式` ↔ `👩‍🏫 教师工作台` 双模式无缝切换。

---

## 十、容器编排与 Docker 运维规约

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    ports: ["3306:3306"]
    environment:
      MYSQL_DATABASE: starlink_campus
  redis:
    image: redis:7.0-alpine
    ports: ["6379:6379"]
  backend:
    build: ../starlink-campus-backend
    ports: ["8080:8080"]
    depends_on: [mysql, redis]
  admin-ui:
    build: ../starlink-campus-admin-ui
    ports: ["80:80"]
  board-ui:
    build: ../starlink-campus-board-ui
    ports: ["3000:3000"]
```

---
© 2026 海星智联科技有限公司 版权所有
