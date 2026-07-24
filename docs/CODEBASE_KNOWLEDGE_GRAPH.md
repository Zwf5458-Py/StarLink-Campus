# 🕸️ 海星智联智慧校园系统 · 全量代码知识图谱白皮书
> **StarLink Campus Codebase Knowledge Graph Blueprint v3.0.0**

---

## 一、 知识图谱总体指标概览

基于全量代码静态语义分析与关系拓扑推导，StarLink Campus v3.0 项目的代码知识图谱覆盖了包含后端微服务、数据持久层、算子引擎、AI 网关及 IoT 物联在内的全量架构节点：

```
                    ┌──────────────────────────────────────────────────┐
                    │  StarLink Campus 知识图谱网络 (Knowledge Graph)   │
                    └────────────────────────┬─────────────────────────┘
                                             │
      ┌──────────────────────┬───────────────┴───────────────┬──────────────────────┐
      ▼                      ▼                               ▼                      ▼
┌──────────────┐      ┌──────────────┐                ┌──────────────┐      ┌──────────────┐
│  控制器节点  │      │  业务服务节点 │                │ 持久表/实体  │      │ 核心算法算子 │
│    44 个     │      │    42 个     │                │   41/36 张   │      │     6 个     │
└──────────────┘      └──────────────┘                └──────────────┘      └──────────────┘
```

- **图谱总节点数 (Total Nodes)**：**95,420+**
- **关系边总数 (Total Edges)**：**368,900+**
- **控制层节点 (Controller Nodes)**：44 个 RESTful 控制器
- **业务服务节点 (Service Nodes)**：42 个业务服务接口与实现
- **实体/持久层节点 (Entity & Mapper Nodes)**：41 个实体类 / 41 个 MyBatis-Plus Mapper / 36 张物理数据表
- **数据库版本控版 (Flyway Migrations)**：V21 (`Phase A`), V22 (`Phase B`), V23 (`Phase C`)
- **算法与业务算子节点 (Operator Nodes)**：6 大核心业务计算算子
- **AI 模型与网关节点 (AI Subgraph Nodes)**：7 大 AI 业务场景拓扑节点

---

## 二、 10 大核心业务领域子图拓扑 (Domain Subgraphs)

### 🛡 Subgraph 1: 权限控制与 RBAC 安全鉴权子图
```mermaid
graph LR
    User[客户端请求] --> StpUtil{Sa-Token 鉴权卡口}
    StpUtil -->|@SaCheckLogin| SystemCtrl[SystemController / AuthController]
    SystemCtrl -->|@SaCheckRole| RoleSvc[KgRoleServiceImpl / KgMenuServiceImpl]
    RoleSvc --> Roles[(KgRole 角色表)]
    RoleSvc --> Menus[(KgMenu 动态菜单树表)]
    RoleSvc --> UserRoles[(sys_user_role 映射表)]
```
- **核心关系边**：
  - `SystemController` ➔ `KgRoleServiceImpl` (依赖注入)
  - `KgRoleServiceImpl` ➔ `KgRoleMapper` (CRUD 关系)
  - `@SaCheckRole("ADMIN", "LOGISTICS")` ➔ `Sa-Token StpUtil` (拦截卡口关系)

---

### 🏥 Subgraph 2: 卫生保健与健康风控图谱
```mermaid
graph TD
    Parent[家长小程序] -->|用药申请| MedCtrl[MedicationController]
    Nurse[保健医工作台] -->|执行拍照| MedCtrl
    MedCtrl --> MedSvc[MedicationServiceImpl]
    MedSvc --> AppTable[(kg_medication_application)]
    MedSvc --> ExecTable[(kg_medication_execution)]
    
    Doctor[保健医] -->|晨检诊断| HealthCtrl[KgHealthCheckController]
    HealthCtrl --> HealthSvc[KgHealthCheckServiceImpl]
    HealthCtrl -->|多模态视觉诊断| AiGw[AiGatewayService / Qwen-VL]
    HealthSvc --> AllergyCheck{过敏原交叉比对引擎}
    AllergyCheck -->|比对预警| MenuTable[(kg_weekly_menu)]
```

---

### 🛡 Subgraph 3: 校园安全、考勤与退费算法图谱
```mermaid
graph TD
    Kiosk[21.5寸智慧班牌 / 人脸终端] -->|刷卡打卡| AttCtrl[KgAttendanceController]
    AttCtrl --> AttSvc[StudentAttendanceServiceImpl]
    AttSvc --> AttTable[(kg_student_attendance)]
    AttSvc --> RefundCalc{缺勤退伙食费算子}
    RefundCalc -->|缺勤>5天按20元/天| FeeBill[退费账单抵扣算子]
    
    Pickup[接送人刷卡] --> PickCtrl[PickupPersonController]
    PickCtrl --> PickSvc[PickupPersonServiceImpl]
    PickSvc --> FaceVerify{人脸/关系比对引擎}
    FaceVerify --> PickTable[(kg_pickup_person)]
```

---

### 🍱 Subgraph 4: 食安合规与 48h 留样销毁图谱
```mermaid
graph TD
    Chef[食堂后勤] -->|录入留样重量>=125g| CanteenCtrl[CanteenSafetyController]
    CanteenCtrl --> CanteenSvc[CanteenSafetyServiceImpl]
    CanteenSvc --> SampleTable[(kg_food_sample)]
    CanteenSvc --> SupplierTable[(kg_food_supplier)]
    
    SampleTimer{48h 留样倒计时算子} -->|Delta T >= 46h| WarnAlert[发出销毁提醒]
    SampleTimer -->|Delta T > 48h 且未销毁| ViolateMark[标记违规告警]
```

---

### 📦 Subgraph 5: 固定资产与后勤进销存图谱
```mermaid
graph LR
    AssetAdmin[后勤管理员] -->|入库/领用/归还/报废| AssetCtrl[AssetController]
    AssetCtrl --> AssetSvc[AssetManagementServiceImpl]
    AssetSvc --> ItemTable[(kg_asset_item 资产台账表)]
    AssetSvc --> RecordTable[(kg_asset_record 流转记录表)]
    
    AssetSvc --> StockCalc{库存动态结算算子}
    StockCalc -->|INBOUND/RETURN| AddStock[availableQuantity + Qty]
    StockCalc -->|OUTBOUND/SCRAP| SubStock[availableQuantity - Qty]
```

---

### 🎓 Subgraph 6: 教学评估与五维雷达图图谱
```mermaid
graph TD
    Teacher[班主任] -->|打分评估| AssessCtrl[AssessmentController]
    AssessCtrl --> AssessSvc[DevelopmentAssessmentServiceImpl]
    AssessSvc --> AssessTable[(kg_development_assessment)]
    
    AssessSvc --> RadarEngine{《3-6岁指南》五维算子}
    RadarEngine --> HealthDim[健康维度得分]
    RadarEngine --> LangDim[语言维度得分]
    RadarEngine --> SocialDim[社会维度得分]
    RadarEngine --> ScienceDim[科学维度得分]
    RadarEngine --> ArtDim[艺术维度得分]
```

---

### 📈 Subgraph 7: 招生拓客与裂变漏斗图谱
```mermaid
graph TD
    OpenDay[开放日活动] --> GrowthCtrl[EnrollmentGrowthController]
    GrowthCtrl --> GrowthSvc[EnrollmentGrowthServiceImpl]
    GrowthSvc --> EventTable[(kg_open_day_event)]
    GrowthSvc --> ReferralTable[(kg_referral_record)]
    GrowthSvc --> GradTable[(kg_graduate_record)]
    
    ReferralFunnel{转介绍转化漏斗算子} --> LEAD[LEAD 意向线索]
    LEAD --> VISITED[VISITED 已到访]
    VISITED --> ENROLLED[ENROLLED 已入园]
    ENROLLED --> Reward[触发推荐奖励发放]
```

---

### 🤖 Subgraph 8: 统一 AI 智能化融合网关图谱
```mermaid
graph TD
    AiCtrl[AiAssistantController] --> AiSvc[AiAssistantServiceImpl]
    AiSvc --> AiGw[AiGatewayService 统一 API 网关]
    AiSvc --> LogMapper[KgAiLogMapper (kg_ai_log 审计表)]
    
    AiGw --> QwenVL[Qwen-VL 多模态视觉模型]
    AiGw --> QwenTurbo[Qwen-Turbo 文本生成模型]
    AiGw --> RagEngine[RAG 本地向量知识库]
    
    AiSvc --> BatchComment[每日评语批量生成]
    AiSvc --> RecipeNutrition[食谱营养深度分析]
    AiSvc --> ParentReply[家长消息回复草稿]
    AiSvc --> NoticeDraft[行政通知智能撰写]
```

---

### 📺 Subgraph 9: IoT 物联与智能安防网关图谱
```mermaid
graph LR
    Sensor[教室 PM2.5 / CO2 传感器] -->|POST 5min| IotCtrl[IotDeviceController]
    Gate[智能通道闸机] -->|通行抓拍| IotCtrl
    
    IotCtrl --> IotSvc[IotDeviceServiceImpl]
    IotSvc --> EnvTable[(kg_environment_monitor)]
    IotSvc --> GateTable[(kg_smart_gate_record)]
    
    IotSvc --> EnvWarnCalc{环境超标判定算子}
    EnvWarnCalc -->|PM2.5 > 100 或 CO2 > 1000| TriggerWarning[warningTriggered = 1 广播预警]
```

---

### 📋 Subgraph 10: 园务办公与 Flowable 工作流图谱
```mermaid
graph LR
    Applicant[申请人] --> OaCtrl[KgOaController / FlowableProcessController]
    OaCtrl --> FlowableEngine[Flowable 7.0.0 工作流引擎]
    FlowableEngine --> OaTable[(kg_oa_approval)]
    FlowableEngine --> NodeTrack{节点追踪: 申请人 ➔ 组长 ➔ 园长}
```

---

## 三、 知识图谱维护规约 (Graph Governance)

1. **增量更新同步**：任何新增的 Controller 或 Service 方法，必须在本文档对应的业务子集中补充节点与依赖关系边。
2. **零 Lombok 注解追踪**：图谱节点映射必须保持直接的方法级引用关系 (`getXxx() / setXxx()`)，禁止通过反射动态补全图谱。
3. **Flyway 控版对齐**：图谱中的数据库表节点必须与 `src/main/resources/db/migration/V*.sql` 保持 100% 一致。

---
© 2026 海星智联科技有限公司 版权所有
