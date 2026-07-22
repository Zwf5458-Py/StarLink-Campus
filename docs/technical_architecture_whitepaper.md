# 📘 海星智联智慧校园系统 · 权威技术架构白皮书
> **StarLink Campus Technical Architecture Whitepaper v2.5.0 Sequoia**

---

## 目录
1. [一、概述与总体设计理念](#一概述与总体设计理念)
2. [二、系统整体架构图 (Architecture Diagram)](#二系统整体架构图-architecture-diagram)
3. [三、后端微服务与持久层架构](#三后端微服务与持久层架构)
4. [四、AI 人脸识别与核心业务算法算子](#四ai-人脸识别与核心业务算法算子)
5. [五、前端设计系统与苹果 Liquid Glass 架构](#五前端设计系统与苹果-liquid-glass-架构)
6. [六、移动端小程序与 21.5 寸智慧班牌架构](#六移动端小程序与-215-寸智慧班牌架构)
7. [七、数据库与存储架构 (15 大业务表)](#七数据库与存储架构-15-大业务表)
8. [八、安全合规与隐私保护](#八安全合规与隐私保护)
9. [九、容器部署与运维规约](#九容器部署与运维规约)

---

## 一、概述与总体设计理念

海星智联智慧校园系统（StarLink Campus）是一套专为学前教育（幼儿园）与智慧校园打造的工业级软硬件一体化系统。系统设计遵循以下核心工程理念：

1. **高可用与低延时 (High Availability & Low Latency)**：
   - 终端与服务端之间采用基于 Spring WebSocket 的网关长连接广播，消息下发延时控制在 50ms 以内；
   - 前端集成离线渐进退避机制（Offline Fallback），确保在网络瞬断时界面依然优雅稳定运行。

2. **数据驱动与智能比对 (Data-Driven & AI Inference)**：
   - 内置**全员幼儿过敏源档案与每周食谱交叉比对引擎**，自动预警潜在食物过敏风险；
   - 内置**缺勤退费算子**（月累计缺勤天数 > 5 天，按 20 元/天 自动计算退还伙食费）。

3. **极简与现代视觉美学 (Modern Design System)**：
   - 全盘引入 Apple macOS Sequoia / iOS Liquid Glass 极简立体毛玻璃与高对比度按钮设计系统，彻底告别传统政企软件的沉闷感。

---

## 二、系统整体架构图 (Architecture Diagram)

```mermaid
graph TD
    subgraph 终端与接入层 (Client Layer)
        A1[21.5寸 RK3288 智慧班牌 UI] -->|WebSocket 长连接| B1(API Gateway 路由)
        A2[苹果 Liquid Glass 管理后台] -->|HTTP/REST API| B1
        A3[微信小程序 starlink-campus-app] -->|HTTP/REST API| B1
    end

    subgraph 网关与应用服务层 (Application Layer)
        B1 --> C1[Spring Boot 3.2 核心业务服务]
        C1 --> C2[Sa-Token 安全鉴权模块]
        C1 --> C3[WebSocket 网关广播服务]
        C1 --> C4[晨检与过敏源交叉比对引擎]
        C1 --> C5[月度缺勤退费算子服务]
        C1 --> C6[园务 OA 流程审批引擎]
    end

    subgraph AI识别与组件层 (AI & Service Layer)
        C1 --> D1[CompreFace 私有化人脸识别服务]
        C1 --> D2[Redis 7.0 实时缓存与会话]
        C1 --> D3[MinIO 海量照片对象存储]
    end

    subgraph 持久化数据层 (Data Layer)
        C1 --> E1[(MySQL 8.0 核心业务数据库)]
    end
```

---

## 三、后端微服务与持久层架构

### 1. 核心技术选型
- **Spring Boot 3.2.0**：基于 Java 17 强类型语言构建，无缝兼容高版本 JDK 编译；
- **MyBatis-Plus 3.5.5**：标准化 `BaseMapper<T>` 与 `ServiceImpl<M, T>` 模式，实现安全的物理表 ORM 映射；
- **Sa-Token 1.37.0**：轻量级 Java 权限认证框架，提供分布式 Session 会话管理。

### 2. 核心 REST 控制器规范
- **`KgStudentController`** (`/api/kindergarten/student/**`)：幼儿档案 CRUD、过敏源比对。
- **`KgAttendanceController`** (`/api/kindergarten/attendance/**`)：刷脸/刷卡打卡、体温判定、缺勤退费计算。
- **`KgHealthCheckController`** (`/api/kindergarten/health/**`)：晨检体温表、食谱过敏源比对引擎。
- **`KgOaController`** (`/api/kindergarten/oa/**`)：流程审批（请假/采购/补卡节点追踪）。
- **`KgPatrolController`** (`/api/kindergarten/patrol/**`)：安防巡检打卡、防伪水印、自动转维修工单。
- **`KgVisitorController`** (`/api/kindergarten/visitor/**`)：通行二维码、超时 15 分钟滞留告警。
- **`KgBoardController`** (`/api/kg/board/**`)：智慧班牌模式下发与 WebSocket 广播。

---

## 四、AI 人脸识别与核心业务算法算子

### 1. 退费算子
$$\text{退费金额} = \max(0, 22 - \text{当月出勤天数}) \times 20\text{元/天} \quad (\text{缺勤天数} > 5\text{生效})$$

### 2. 过敏源比对算法
将 `kg_student.allergies` 文本切分为集合 $S_{allergies}$，与今日食谱食材集合 $S_{ingredients}$ 取交集：
$$\text{Risk Alert} = S_{allergies} \cap S_{ingredients} \neq \emptyset$$

---

## 五、前端设计系统与苹果 Liquid Glass 架构

1. **专属品牌 LOGO**：矢量 SVG 蓝色渐变底座 + 5 角金黄海星 Badge。
2. **全局搜索框 (⌘K)**：支持全系统秒级检索幼儿、教职工、单号。
3. **🔔 事件提醒与 ⚙️ 偏好设置**：内置发热预警、OA 待审、滞留告警弹窗与参数阀值控制。
4. **全量 8 大动态图表**：折线趋势图、环形饼图、对比柱状图、条形图与平滑曲线图。

---

## 六、移动端小程序与 21.5 寸智慧班牌架构

- **微信小程序 (`starlink-campus-app`)**：基于 Vue 3 / Uni-App，支持 `👨‍👩‍👧 家长模式` ↔ `👩‍🏫 教师工作台` 一键切换，集成动态二维码接送卡与 GPS 考勤打卡。
- **21.5寸智慧班牌 (`starlink-campus-board-ui`)**：运行轻量级 Android WebView Kiosk 容器，展示班级排期、健康食谱、教职名片、本周之星与全园紧急广播。

---

## 七、数据库与存储架构 (15 大业务表)

1. `kg_campus_info`：园区信息表
2. `kg_class`：班级信息表
3. `kg_student`：幼儿档案表 (含 `allergies` 过敏源)
4. `kg_staff`：教职工档案表
5. `kg_student_attendance`：考勤记录表
6. `kg_morning_check`：晨检记录表
7. `kg_class_board_config`：智慧班牌配置表
8. `kg_visitor_record`：访客记录表
9. `kg_patrol_record`：安防巡检表
10. `kg_repair_order`：后勤维修工单表
11. `kg_notification`：通知公告表
12. `kg_course`：托育拓展活动表
13. `kg_oa_approval`：园务 OA 审批表

---

## 八、容器部署与运维规约

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    ports: ["3306:3306"]
  redis:
    image: redis:7.0-alpine
    ports: ["6379:6379"]
  backend:
    build: ../starlink-campus-backend
    ports: ["8080:8080"]
  admin-ui:
    build: ../starlink-campus-admin-ui
    ports: ["80:80"]
  board-ui:
    build: ../starlink-campus-board-ui
    ports: ["3000:3000"]
```

---
© 2026 海星智联科技有限公司 版权所有
