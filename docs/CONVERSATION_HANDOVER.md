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

## 4. 后续 Agent 接手指南 (System Directives)
如果你是新开启对话的 AI 助手，请遵循以下规则与上下文：
1. **无需再次搭建基础框架**：所有的基础 CRUD、鉴权、WebSocket、Flowable 工作流已在本项目中全面打通，你可以直接在现有框架上二次开发。
2. **测试驱动安全底线**：后端的任何业务修改，请务必运行 `mvn clean test` 验证是否破坏了 `PatrolInspectionServiceImplTest` 和 `MockWechatSecurityUtilTest` 的联动或安全阈值。
3. **全局文档同步**：所有的接口新增需严格加上 `@Tag` 和 `@Operation` OpenAPI 注解。
4. **知识图谱检索**：本项目高度依赖 `codebase-memory-mcp` 的知识图谱，你可以通过 MCP graph tools 进行 `search_graph` 查找任何遗忘的 Entity 或 Controller。

---
**[The End of Context]** 
*新对话的 Agent 读取到这里即可完全掌握海星智联项目状态，无缝进入新的开发或维护流中。*
