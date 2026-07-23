# 海星智联 (StarLink Campus) 历史对话交接文档

> **创建时间**: 2026-07-23  
> **文档目标**: 为后续的全新 AI 对话提供全局上下文记忆，确保新 Agent 接手时能够 0 耗时理解项目全貌、架构演进和已完成的工作。

---

## 1. 项目基本信息
- **项目名称**: 海星智联智慧校园系统 (StarLink Campus)
- **绝对路径**: `/Users/oraclez/Desktop/zwf/StarLink Campus`
- **项目构成**: 
  - `starlink-campus-backend` (Java 后端)
  - `starlink-campus-admin-ui` (Vue 3 管理后台)
  - `starlink-campus-board-ui` (Vue 3 智慧班牌前端)
  - `starlink-campus-app` (UniApp 移动端小程序)

## 2. 核心架构与技术栈
- **后端框架**: Java 17/26 + Spring Boot 3.2.x + MyBatis-Plus
- **权限与认证**: Sa-Token (轻量级 RBAC 与 SSO)
- **工作流引擎**: Flowable 7.x (负责处理请假、加班等园务 OA 审批闭环)
- **实时通信**: Spring WebSocket (用于管理大屏和 26 台智慧班牌的双向实时消息/状态下发)
- **接口文档**: `springdoc-openapi-starter-webmvc-ui` (Swagger 3 规范，访问路径 `/swagger-ui/index.html`)
- **前端规范**: Vue 3 + 组合式 API + Glass-morphism (毛玻璃苹果风UI)。

## 3. 开发阶段与完成度 (目前进度：100% 完结)
通过之前对话的深度合作，项目经历了五个核心开发阶段，现已全部打通并交付：

1. **第一阶段：基建与重构**
   - 彻底移除了原先裸写在 Controller 中的硬编码，建立了完整的 `Service` 业务层，修复了由于全表扫描引发的 OOM 风险。
2. **第二阶段：Flowable 引擎接入**
   - 将 OA 审批的 Mock 数据替换为真实的 Flowable `RuntimeService` 和 `TaskService`，实现了流程发起到签收、拒绝的真实闭环。
3. **第三阶段：安全容灾与业务深化**
   - 上线了微信级敏感词/涉黄图像安全防护 `MockWechatSecurityUtil`。
   - 实现智能安保巡检系统，巡检一旦异常（如消防栓破损）会自动下发 `KgRepairOrder` 维修工单。
4. **第四阶段：前端生态大一统**
   - 班牌端 `App.vue` 成功拆解为 `WeatherWidget.vue`、`AttendanceCard.vue` 等积木组件。
   - UniApp 完成真实 API 联调（班级圈 `circle`、通知 `notice`）。
5. **第五阶段：自动化质量防护网构建**
   - 编写了核心业务的 JUnit 5 + Mockito 单元测试。
   - **坑点记录**：在 Java 26 下，Mockito 的 `spy` 代理对 MyBatis-Plus 的 `ServiceImpl` 存在拦截冲突，已通过重构 Service 内部直接调用 `Mapper` 解决。当前所有测试 `mvn clean test` 100% 绿灯。
6. **第六阶段：核心业务模块补全 (24 大功能完全闭环)**
   - 补全了 9 大幼儿园核心业务模块：学生成长档案、在线缴费与账单、接送人安全管理、每周食谱管理、教学周计划、问卷调查与满意度、招生管理、教职工薪酬查询、视频监控接口占位。
   - 完成了对应的前端管理后台页面（8 个 View）及小程序移动端页面（5 个 Page）的业务闭环与全链路联调。
7. **第七阶段：AI 自动化辅助套件与合规网关建立 (全系统 25 大模块完结)**
   - **架构设计**：设计并实现 `AiGatewayService` 屏蔽大模型提供商通信细节，引入 `@Async` 与 `CompletableFuture` 异步非阻塞执行模式及 3-5 秒熔断降级逻辑，在 `application.yml` 内配置 `mock` 开关。
   - **高价值场景 1（晨检视觉分析）**：新增 `HealthAiService` 与 `HealthAiServiceImpl`，前端 `HealthMonitorView.vue` 支持启动 AI 视觉检测手足口病（口腔疱疹、手掌红点）及眼部异常。
   - **高价值场景 2（内容安全网关重构）**：将 `MockContentSecurityServiceImpl` 从本地关键词升级为调用大模型 `checkTextSecurityAsync` / `checkMediaSecurityAsync` 做语义级违规过滤。
   - **高价值场景 3（智能评语/排版润色）**：前端在 `GrowthRecordView.vue` 增加“AI一键评语生成”，在 `FamilyCooperationView.vue` 班级圈增加“AI智能润色”，后端由 `polishText` 异步驱动。
   - **高价值场景 4（食谱营养分析）**：后端 `analyzeMenuNutrition` 提供营养达标评估与建议。
   - **智能园秘 RAG 知识库**：构建 `KgAiKnowledgeBase` 体系，前端完成管理端 `AiKnowledgeView.vue` 与微信小程序 `pages/ai-chat/ai-chat.vue` 全链路对话闭环。
8. **第八阶段：短期优化与中期完善研发 (架构闭环与性能演进)**
   - **短期性能与防御**：
     - 多模态响应解析防护 NPE，Redis 限流切换为 Lua 原子脚本。
     - Dashboard 缓存键引入 60s TTL 自动刷新；全表扫描重构为 SQL 聚合 (Health/Contact)。
     - 消息中心 `/notification/unread`、全局搜索 `/search`、系统设置 `/system/config` 彻底对接真实后端 API。
   - **中期业务完善**：
     - **问卷透视**：通过 MySQL `JSON_EXTRACT` + `GROUP BY` 下推聚合，消除答卷全量内存读取的 OOM 隐患。
     - **视频档案**：`KgGrowthRecord` 扩展 `video_url`，管理端 `GrowthRecordView.vue` 支持 MP4 视频沉浸式预览与上传。
     - **微信生态**：引入 `weixin-java-mp` 与 `weixin-java-pay` 依赖，搭建 `WxMpConfiguration` 及 `WxPayConfiguration` 微信能力总线。
     - **家校 OA 联动**：小程序申请请假通过后，自动在 `KgStudentAttendance` 插入 `LEAVE` 考勤记录。
     - **测试网构建**：新增 `AiContentSecurityServiceImplTest` 及 `SurveyServiceImplTest`，所有 24 个 JUnit 用例 100% 通过。
   - **项目资产同步**：
     - 全量代码知识图谱使用 `codebase-memory-mcp` CLI 重新建立（90,957 节点，349,607 边）。
     - 代码提交至 Git 并同步 Push 到 `origin/main`。

## 4. 后续 Agent 接手指南 (System Directives)
如果你是新开启对话的 AI 助手，请遵循以下规则与上下文：
1. **无需再次搭建基础框架**：所有的基础 CRUD、鉴权、WebSocket、Flowable 工作流已在本项目中全面打通，你可以直接在现有框架上二次开发。
2. **测试驱动安全底线**：后端的任何业务修改，请务必运行 `mvn clean test` 验证是否破坏了 `PatrolInspectionServiceImplTest` 和 `MockWechatSecurityUtilTest` 的联动或安全阈值。
3. **全局文档同步**：所有的接口新增需严格加上 `@Tag` 和 `@Operation` OpenAPI 注解。
4. **知识图谱检索**：本项目高度依赖 `codebase-memory-mcp` 的知识图谱，你可以通过 MCP graph tools 进行 `search_graph` 查找任何遗忘的 Entity 或 Controller。

---
**[The End of Context]** 
*新对话的 Agent 读取到这里即可完全掌握海星智联项目状态，无缝进入新的开发或维护流中。*
