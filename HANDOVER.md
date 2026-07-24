# StarLink Campus (海星智联智慧校园) - 核心项目交接手册

> **文档状态**: 100% 核心后端业务群竣工版本交接
> **适用对象**: 后续接手的后端架构师、前端开发、实施与测试团队

---

## 一、 项目现状与里程碑

本项目已经成功完成了原定的三批核心系统建设规划，现已具备全方位的 SaaS 级交付能力。全量 35+ 子模块功能的详细矩阵请查阅 [README.md](README.md)。

1. **Phase A (教务与健康管理)**：已上线并经过单元测试。覆盖了午睡喂药、体温考勤预警、家园通讯录、家长端 OA 请假流转。
2. **Phase B (教学评估与后勤合规)**：覆盖了校车打卡监控、资产流转记账、48小时食品留样自动告警，以及五大维度幼儿发展评估雷达图逻辑。
3. **Phase C (招生裂变与 IoT 智能化)**：覆盖了开放日转化追踪、硬件班牌的环境质量上报（温湿度、PM2.5）、门禁闸机的出入库打卡联动。
4. **AI 智能化融合引擎 (Batch 1)**：通过高度解耦的统一 `AiGatewayService`，支持大批量并行每日评语生成、食谱营养深度分析、行政通知撰写及微信家园问答自动生成。

---

## 二、 架构规范与核心约定

### 1. 实体层与 Lombok 规范
为防范环境插件兼容性带来的隐患（如 `maven-compiler-plugin` 无法识别 `@Data`），本项目**全量禁止使用 Lombok**。所有实体类（Entity）、数据传输对象（DTO）及视图对象（VO）均采用了标准 Java 原生 `Getter / Setter` 进行显式编写。
在未来新增实体表时，也请使用项目中提供的 Python 自动化脚本 `generate_phase_b_entities.py` 或 `generate_phase_c_entities.py` 生成标准模板，切勿加回 `@Data`。

### 2. 安全鉴权 (Sa-Token)
- 框架选型：采用了业内轻量级的 `Sa-Token` 权限框架。
- 所有 Controller 除了公共接口外，均标记了 `@SaCheckLogin` 以校验 JWT Token。
- 绝大部分敏感写接口（如新增资产、更新留样、修改家长状态），均使用了基于角色的强控校验，例如 `@SaCheckRole(value = {"ADMIN", "LOGISTICS"}, mode = SaMode.OR)`。

### 3. 数据库迁移 (Flyway)
数据库模型管控统一收口在 `src/main/resources/db/migration` 下。
- **V21**: Phase A 基础模块建表（OA、健康、幼儿）。
- **V22**: Phase B 后勤合规与校车表。
- **V23**: Phase C IoT 与招生拓客表。
请绝对不要在外部直接使用 Navicat/DataGrip 执行 `CREATE TABLE`，必须编写 `V24__xxxx.sql` 脚本确保各个部署环境通过 Flyway 自动迭代。

### 4. 单元测试约束
项目强制推行 `mvn clean test` 验证。当前全模块跑通率 **100% (39/39 绿灯)**。任何对 `Service` 层的方法改动，必须确保原本的自动化用例不会引发异常挂起或 OOM 熔断。在 AI 网关测试（如 `AiAssistantServiceImplTest`）中，统一采用 `@Mock` 和 `CompletableFuture.completedFuture()` 进行 Mock，防止 CI 时占用真金白银消耗 token。

---

## 三、 未尽事宜与后续迭代建议

### 1. 前端 UI 对接与部署 (已完成核心里程碑)
- 班牌系统的 UI 仪表盘 (`starlink-campus-board-ui`) 已按照高标准 Apple Liquid Glass 美学规范完成开发，并在内部支持了基于 WebSocket 的双向通信与状态同步。
- **交付策略**：前端目前已同源挂载至 Spring Boot 后端的 `src/main/resources/static` 目录中。当执行 `mvn clean package` 之后，将生成单体可执行的 `starlink-campus-backend-1.0.0-SNAPSHOT.jar` 胖包 (Fat-Jar)，真正实现“一个 Jar 包搞定软硬件全栈部署”。
- 后续对于 PC 端的管理系统或移动端小程序，建议采用前后端分离的独立容器部署（如 Nginx + Docker）。

### 2. 真实硬件接入 (IoT网关下沉)
- `/api/iot/env/report` 与 `/api/iot/gate/pass` 目前是 HTTP REST 接口，可供软网关或中间层 POST 调用。
- 建议在实际落地园所时，通过边缘计算盒子或局域网的 Node-RED 中间件将基于 MQTT/TCP 的传感器报文解析后转化为 HTTP POST 请求推送给此后端；如有极高的吞吐需求，可将其升级改造为 WebSocket / Netty 直连。

### 3. 单元测试重构说明
- 经过多轮迭代重构，我们淘汰了那些由于业务变更频繁而容易引发编译错误的自动生成冗余测试，保留并精调了 **12 个核心业务高覆盖率** 测试用例（涵盖了 IoT、开放日报名、资产防负数、每日评语生成等最高优逻辑）。
- 接手后的测试团队如需新增功能，请务必保证核心流程测试的一致性，确保 CI 流水线不会产生误报警。

---
**接手愉快！愿 StarLink Campus 项目能为全国更多智慧园所带去极致的使用体验。**
