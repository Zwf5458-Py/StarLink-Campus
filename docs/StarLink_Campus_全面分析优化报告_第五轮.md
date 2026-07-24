# 海星智联智慧校园 --- 全面分析优化报告（第五轮）

**项目：** StarLink Campus 幼儿园智慧校园系统 **分析日期：** 2026-07-24
**分析方法：** MCP 知识图谱（91,122 节点 / 350,245 边）+ 全量代码审查
**分析范围：** 后端 32 个 Controller、31 个 Service 实现、19 个 Flyway
迁移、前端三端（admin-ui 23 视图 / app 11 页面 / board-ui 3 组件）、7
个测试文件

------------------------------------------------------------------------

## 一、项目架构总览

### 1.1 技术栈

后端采用 Spring Boot 3.2.0 + MyBatis-Plus 3.5.5 + MySQL 8.0 + Redis +
Sa-Token 1.37.0，工作流引擎为 Flowable 7.0.0，AI 网关对接阿里云
DashScope（qwen-turbo 文本 / qwen-vl-max 多模态），微信生态集成
weixin-java-mp 和 weixin-java-pay（WxJava 4.6.0）。

前端包含三端：管理后台 admin-ui（Vue 3 + Element Plus + Pinia +
Vite），家长小程序 app（uni-app），智慧班牌 board-ui（Vue 3 + Vite +
WebSocket）。

### 1.2 代码规模

  -------------------------------------------------------------
  维度                           数量
  ------------------------------ ------------------------------
  后端 Controller                32 个

  后端 Service 接口              32 个

  后端 Service 实现              31 个

  Flyway 迁移                    V1 \~ V19（19 个）

  数据库业务表                   36 张（+ 20 张 RuoYi 系统表）

  实体类                         36 个

  admin-ui 视图                  23 个

  admin-ui API 模块              12 个

  app 页面                       11 个

  board-ui 组件                  3 个

  单元测试文件                   7 个
  -------------------------------------------------------------

------------------------------------------------------------------------

## 二、第四轮问题修复确认

  ------------------------------------------------------------------------------------------------------------------------------------------------
  序号        第四轮问题               本轮状态         说明
  ----------- ------------------------ ---------------- ------------------------------------------------------------------------------------------
  1           通知中心使用硬编码数据   ✅ 已修复        NotificationController 已对接 KgNoticeService → kg_notice 表，支持未读列表和已读标记

  2           AI 内容安全审核仍为 Mock ⚠️ 部分改善      AiContentSecurityServiceImpl 已有测试覆盖，但非 Mock 模式下仍仅 sleep 后返回 true

  3           微信 SDK                 ⚠️ 部分推进      新增
              仅框架无业务流程                          WxPaymentController（支付下单+回调）、WxMessageServiceImpl（模板消息），但凭据仍为占位值

  4           招生漏斗统计全表加载     ✅ 已修复        EnrollmentServiceImpl 改用 countByStatus() 聚合查询 + ConcurrentHashMap 缓存

  5           全局搜索混入 Mock 数据   ✅ 已修复        SearchController 已改为纯数据库查询（4 个 Mapper LIKE 查询 + LIMIT 5）

  6           直播服务仍为占位         ❌ 未变          KgLiveStreamController 仍返回 100% 硬编码数据，LiveStreamService 无实现

  7           家长端缺少请假申请页面   ✅ 已修复        app 新增 leave/leave.vue 页面，对接 /kindergarten/oa/submit

  8           AI 助手日志 modelName    ❌ 未变          saveLog() 仍写死 "DeepSeek-V3"
              硬编码                                    
  ------------------------------------------------------------------------------------------------------------------------------------------------

**修复率：4/8 完全修复，2/8 部分推进，2/8 未变。**

------------------------------------------------------------------------

## 三、本轮新发现 --- 安全问题（P0）

### 3.1 微信支付回调无签名验证（严重）

WxPaymentController 的 `/payment/notify/wechat`
端点接收微信支付异步通知后，仅打印日志即返回
SUCCESS，未做任何签名校验、订单验证或数据库更新。生产环境下攻击者可伪造支付回调，将未支付订单标记为已支付。

**建议：** 使用 WxPayService.parseOrderNotifyResult()
验证签名，校验订单金额一致性，更新 kg_payment 表状态，实现幂等处理。

### 3.2 系统配置接口暴露 API 密钥（严重）

SystemConfigController 的 GET 接口将 Redis 中存储的完整配置（含
customApiKey）直接返回给客户端，且无任何认证注解。任何未登录用户均可读取
AI API 密钥并篡改系统配置。

**建议：** 添加 \@SaCheckLogin + 角色校验；GET
响应中对敏感字段脱敏（仅显示后 4 位）；PUT 接口增加输入白名单校验。

### 3.3 五个新 Controller 均无认证（高危）

NotificationController、SearchController、SystemConfigController、WxPaymentController、KgLiveStreamController
均缺少 \@SaCheckLogin 或等效认证注解，所有端点对外公开。其中
SearchController 可搜索全部学生和教职工数据，KgLiveStreamController
暴露监控摄像头信息。

**建议：** 统一添加 Sa-Token 认证注解；SearchController
增加数据权限过滤（教师仅搜本班学生）；监控接口限制为管理员角色。

### 3.4 微信支付下单使用硬编码 Mock 数据（高危）

WxPaymentController 在 wxPayService 为 null
或异常时返回伪造的支付参数（appId="wx_starlink_mock_appid"，paySign="MOCK_PAY_SIGNATURE_OK"），前端会误认为支付成功。openId
回退值为 "oMockUserOpenId123456"，spbillCreateIp 固定为 "127.0.0.1"。

**建议：** 移除 Mock
回退逻辑，凭据未配置时直接返回错误码；从请求上下文获取真实客户端 IP。

------------------------------------------------------------------------

## 四、本轮新发现 --- 架构与代码质量问题（P1）

### 4.1 三张业务表缺少 Flyway 迁移（严重）

kg_article（文章）、kg_class_circle（班级圈）、kg_notice（通知中心）三张表有对应的实体类和
Mapper，但没有任何 Flyway 迁移脚本创建它们。kg_notice 表由
KgNoticeServiceImpl 在运行时通过 JdbcTemplate 执行 CREATE TABLE IF NOT
EXISTS 创建，这违反了 Schema 即代码的原则。kg_article 和 kg_class_circle
在首次访问时将直接报错。

**建议：** 新增 V20 迁移脚本创建这三张表；移除 KgNoticeServiceImpl
中的运行时 DDL 代码。

### 4.2 通知系统存在两套重叠实体（设计缺陷）

项目中同时存在 KgNotification（kg_notification 表，由
NotificationServiceImpl 管理，面向定向通知）和 KgNotice（kg_notice
表，由 KgNoticeServiceImpl
管理，面向通知中心），两者功能高度重叠但互不关联。KgNotification 的
is_read 是单条记录级别的布尔值，无法追踪多收件人的已读状态；而 RuoYi 的
sys_notice + sys_notice_read 已有正确的多用户已读设计。

**建议：** 统一为一套通知模型，引入 kg_notice_read
关联表记录每个用户的已读状态；废弃冗余实体。

### 4.3 KgNoticeServiceImpl 运行时 DDL + 硬编码种子数据（反模式）

该服务在每次调用 getUnreadNoticeList() 和 markAsRead() 时都执行
ensureTableExists()（CREATE TABLE IF NOT EXISTS），且表为空时自动插入 3
条硬编码演示通知。查询失败时 catch 块会再次调用 initDefaultNotices()
并重试，可能导致重复插入。

**建议：** 表结构由 Flyway 管理；种子数据放入迁移脚本；移除运行时 DDL
和自动种子逻辑。

### 4.4 SearchController 绕过 Service 层（架构违规）

SearchController 直接注入 4 个
Mapper（KgStudentMapper、KgStaffMapper、KgOaApprovalMapper、KgPatrolRecordMapper）执行查询，未经过
Service 层。OA 和巡查搜索的异常被 catch (Exception ignore)
静默吞掉。响应格式使用手动构建的 Map 而非项目统一的 R 包装。

**建议：** 创建 SearchService 封装跨模块搜索逻辑；使用 R
统一响应；移除静默异常捕获。

### 4.5 WxMessageServiceImpl 模板 ID 为占位符

sendHealthAlertNotice 和 sendOaApprovalNotice 使用的模板 ID 分别为
"HEALTH_ALERT_TEMP_ID" 和 "OA_APPROVAL_TEMP_ID"，不是真实的微信模板
ID。当 wxMpService 为 null 时静默返回 true（成功），掩盖了发送失败。

**建议：** 模板 ID 从 application.yml 配置读取；wxMpService 为 null
时返回 false 并记录警告日志。

### 4.6 高频查询表缺少索引

以下表在最常用的过滤列上缺少索引，数据量增长后将显著影响查询性能：

  -------------------------------------------------------------------------
  表                      缺少索引                 影响场景
  ----------------------- ------------------------ ------------------------
  kg_student_attendance   (student_id,             每日签到去重查询
                          attendance_date)         

  kg_student_attendance   (class_id,               班级日考勤统计
                          attendance_date)         

  kg_morning_check        (student_id, check_date) 学生健康历史查询

  kg_staff_attendance     (staff_id,               教职工考勤查询
                          attendance_date)         

  kg_visitor_record       visit_date / status      访客列表筛选

  kg_patrol_record        patrol_staff_id /        巡查历史查询
                          patrol_time              

  kg_notification         is_read / target_type    未读通知查询

  kg_oa_approval          status / applicant_id    待审批列表
  -------------------------------------------------------------------------

**建议：** 新增 V20+ 迁移脚本添加上述复合索引。

### 4.7 docker-compose.yml 配置错误

MySQL 初始化卷挂载引用了不存在的 ../sql/01_schema_kindergarten.sql
文件；MySQL root 密码（root_password_123）与 application.yml
默认值（root123456）不一致；CompreFace 使用了非官方独立镜像。

**建议：** 移除失效的 SQL 卷挂载（Flyway
已管理迁移）；统一密码配置；使用 CompreFace 官方 docker-compose
部署方案。

### 4.8 V18/V19 迁移脚本具有破坏性

V18 执行 DELETE FROM kg_class 后重新插入，V19 执行 DELETE FROM
kg_student 后重新插入，均使用 SET FOREIGN_KEY_CHECKS = 0
绕过外键约束。在有真实数据的生产环境中执行将导致数据丢失。

**建议：** 将种子数据迁移改为 INSERT IGNORE 或 INSERT ... ON DUPLICATE
KEY UPDATE；生产环境不应包含 DELETE 语句。

------------------------------------------------------------------------

## 五、前端三端功能覆盖分析

### 5.1 覆盖矩阵

  -------------------------------------------------------------------------------
  后端模块        admin-ui                   app（小程序）        board-ui
  --------------- -------------------------- -------------------- ---------------
  数据看板        ✅ 完整                    ---                  ---

  学生管理        ✅ CRUD + 人脸同步         ---                  ---

  学生考勤        ✅ 签到/统计/退费/导出     仅退费计算           仅签到

  教职工考勤      ✅ 完整                    ---                  ---

  健康晨检        ✅ 完整 + AI 视觉          ---                  ---

  巡查管理        ⚠️ 仅列表 + 报修           ---                  ---

  访客管理        ⚠️ 无审批/拒绝操作         ---                  ---

  OA 审批         ✅ 列表/审批/拒绝          ✅ 请假提交          ---

  Flowable 流程   ❌ 未对接                  ---                  ---

  班级圈          ✅ 完整                    ✅                   ---
                                             完整（路径不匹配）   

  通讯录          ✅ 完整                    ---                  ---

  文章/门户       ⚠️ 仅列表 + 新增           ✅ 列表 + 签收       ---

  课程管理        ✅ 完整                    ---                  ---

  通知中心        ⚠️ 仅获取未读              ---                  ---

  系统配置        ✅ 设置面板                ---                  ---

  全局搜索        ✅ 搜索栏                  ---                  ---

  成长档案        ⚠️ 列表 + AI 评语          ✅ 只读时间线        ---

  费用管理        ⚠️ 仅列表                  ⚠️ 未支付 + 历史     ---

  每周食谱        ⚠️ 仅列表                  ✅ 只读              ---

  每周计划        ⚠️ 仅列表                  ✅ 只读              ---

  问卷调查        ⚠️ 仅列表                  ✅ 列表 + 答题       ---

  招生管理        ⚠️ 仅列表                  ---                  ---

  工资条          ⚠️ 仅列表                  ---                  ---

  接送管理        ⚠️ 仅列表                  ---                  ---

  AI 助手         ⚠️                         ✅ 聊天              ---
                  部分（评语/润色/知识库）                        

  微信支付        ❌ 无                      ❌ 模拟支付          ---

  直播监控        ❌ 无                      ❌ 无                ---

  文件上传        ✅ 成长档案用              ---                  ---

  班牌配置        ---                        ---                  ✅ 完整
  -------------------------------------------------------------------------------

### 5.2 前端关键问题

**API 路径不匹配（会导致 404）：**

  ------------------------------------------------------------------------------------
  前端调用路径                         后端实际路径                      所在端
  ------------------------------------ --------------------------------- -------------
  /kindergarten/class-circle/list      /kindergarten/circle/list         app

  /kindergarten/class-circle/publish   /kindergarten/circle/post         app

  /kindergarten/contact/list           /api/kindergarten/contacts/list   admin-ui
  ------------------------------------------------------------------------------------

**其他前端问题：**

admin-ui 无登录页面（request.ts 中硬编码 admin/admin123
自动登录）；无路由守卫；Pinia 已注册但无 store 文件；7
个视图（费用、接送、食谱、计划、问卷、招生、工资）的核心操作按钮显示"开发中"；StudentManageView
的编辑/删除为非功能桩代码；GrowthRecordView 的提交/删除逻辑被注释。

app 首页（index.vue）全部为静态数据，无真实 API 调用；GPS 打卡仅弹 Toast
未调用 uni.getLocation；支付完全模拟；无直播页面；无接送码验证页面。

board-ui 天气、食谱、教师信息、明星学生均为硬编码数据；签到结果使用原生
alert() 而非样式化弹窗。

------------------------------------------------------------------------

## 六、测试覆盖分析

### 6.1 现有测试（7 个文件，24 个测试用例）

  --------------------------------------------------------------------------------------------------------
  测试文件                           覆盖模块          用例数        质量
  ---------------------------------- ----------------- ------------- -------------------------------------
  StudentAttendanceServiceImplTest   学生考勤          4             良好 ---
                                                                     含退费阈值、更新/插入逻辑、边界条件

  KgArticleServiceImplTest           文章管理          5             良好 --- 分类查询、评论审批、分类
                                                                     CRUD

  VisitorServiceImplTest             访客管理          5             良好 ---
                                                                     通行证生成、审批状态机、超时检测

  AiContentSecurityServiceImplTest   AI 内容安全       4             良好 --- 安全/不安全/空值/超时回退

  SurveyServiceImplTest              问卷统计          1             合格 --- 多题统计 + 选项分布

  PatrolInspectionServiceImplTest    巡查管理          3             良好 ---
                                                                     任务生成、正常提交、异常触发报修

  DashboardServiceImplTest           数据看板          2             良好 --- 正常聚合 + 零学生边界
  --------------------------------------------------------------------------------------------------------

### 6.2 零覆盖的高风险模块

  ----------------------------------------------------------------------------------------
  模块                                         风险等级             原因
  -------------------------------------------- -------------------- ----------------------
  FeeServiceImpl（费用）                       高                   涉及金额计算

  SalarySlipServiceImpl（工资）                高                   涉及薪资数据

  HealthServiceImpl（健康）                    高                   安全关键（发热检测）

  PickupPersonServiceImpl（接送）              高                   儿童安全

  KgOaApprovalServiceImpl（审批）              高                   核心业务流程

  KgRoleServiceImpl /                          高                   安全关键
  KgMenuServiceImpl（权限）                                         

  KgStaffAttendanceServiceImpl（教职工考勤）   高                   关联薪资

  KgStudentServiceImpl（学生）                 中                   核心 CRUD

  EnrollmentServiceImpl（招生）                中                   业务统计

  NotificationServiceImpl /                    中                   通知可靠性
  KgNoticeServiceImpl                                               
  ----------------------------------------------------------------------------------------

**整体测试覆盖率：22.6%（7/31 个 Service 实现）。**

------------------------------------------------------------------------

## 七、功能补充建议

### 7.1 短期（1-2 周）--- 安全与基础修复

1.  **添加认证注解** --- 为 5 个新 Controller 统一添加
    \@SaCheckLogin，SearchController 增加数据权限过滤
2.  **修复支付回调** --- 实现微信签名验证、订单校验、kg_payment
    状态更新、幂等处理
3.  **补建缺失表** --- 新增 V20 迁移创建
    kg_article、kg_class_circle、kg_notice 表
4.  **修复 API 路径不匹配** --- 统一 app 和 admin-ui 的 API
    调用路径与后端一致
5.  **添加高频索引** --- 为考勤、晨检、通知、审批等表添加复合索引
6.  **系统配置脱敏** --- GET 响应中 API 密钥仅显示后 4 位，添加认证

### 7.2 中期（2-4 周）--- 功能完善

7.  **统一通知模型** --- 合并 KgNotification 和 KgNotice 为一套，引入
    kg_notice_read 多用户已读表
8.  **admin-ui 桩代码补全** ---
    完成费用、接送、食谱、计划、问卷、招生、工资 7 个视图的 CRUD 操作
9.  **app 首页对接真实 API** ---
    替换静态数据为实际的儿童状态、教师统计、GPS 打卡
10. **Flowable 流程前端对接** --- admin-ui 对接
    FlowableProcessController 的流程发起和任务办理
11. **board-ui 动态数据** --- 食谱从 /kindergarten/menu/list
    获取，天气对接真实 API
12. **AI 内容安全真实接入** --- 替换 Mock 为阿里云绿网或 DashScope
    安全审核接口
13. **微信登录对接** --- 实现 wx.login() → code2Session →
    用户绑定完整链路

### 7.3 长期（1-2 月）--- 生态扩展

14. **微信学费缴纳** --- 基于 WxPay
    实现完整支付流程（下单→支付→回调→退费）
15. **直播监控** --- 对接海康/大华摄像头 RTMP 推流，前端增加监控墙页面
16. **数据导出** --- 考勤、健康、成长档案等支持 Excel/PDF 导出
17. **测试覆盖提升** --- 优先为费用、工资、健康、接送、审批、权限 6
    个高风险模块补充单元测试，目标覆盖率 60%+
18. **生产部署方案** --- 修复 docker-compose、添加 Nginx 反向代理、配置
    HTTPS、环境变量模板（.env.example）
19. **家长端扩展** --- 增加接送码验证、实时直播查看、在线缴费页面
20. **运营数据分析** ---
    招生转化漏斗可视化、考勤趋势报表、健康异常预警看板

------------------------------------------------------------------------

## 八、项目完成度评估

  --------------------------------------------------------------------------------
  模块                                完成度               较第四轮变化
  ----------------------------------- -------------------- -----------------------
  基础业务（学生/教职工/考勤/班级）   95%                  ---

  OA 审批工作流                       90%                  ---

  AI 智能助手                         88%                  ↑ 新增健康 AI 视觉分析

  数据看板                            90%                  ---

  健康管理                            90%                  ---

  问卷统计                            90%                  ---

  通知中心                            70%                  ↑ 已对接数据库（↑30%）

  全局搜索                            80%                  ↑ 移除 Mock
                                                           数据（↑10%）

  系统配置                            75%                  ↑ 新增（需安全加固）

  微信生态集成                        40%                  ↑ 支付/消息框架（↑10%）

  安全与运维                          75%                  ↓ 新 Controller
                                                           无认证（↓10%）

  前端 admin-ui                       70%                  --- 7 个视图仍为桩代码

  前端 app                            65%                  ↑ 新增请假页面

  前端 board-ui                       60%                  --- 多为硬编码数据

  测试覆盖                            23%                  ---

  数据库完整性                        80%                  ↓ 发现 3 张缺失表

  智慧课堂/直播                       15%                  ---

  招生管理                            75%                  ---
  --------------------------------------------------------------------------------

**整体项目完成度：约 82%**（较第四轮 93%
下调，因本轮采用更严格的审查标准，将前端桩代码、缺失迁移、安全隐患、测试覆盖等纳入评估）

------------------------------------------------------------------------

## 九、总结

本轮通过 MCP 知识图谱对 91,122 个代码节点和 350,245
条关系边进行全量分析，结合逐文件审查，发现了 4 个 P0
级安全问题（支付回调无验证、API 密钥暴露、5 个 Controller 无认证、支付
Mock 回退）、8 个 P1 级架构问题（3 张缺失表、通知模型重叠、运行时
DDL、缺少索引等），以及前端三端与后端 API 的覆盖差距。

积极方面：第四轮提出的通知中心硬编码、招生全表加载、搜索 Mock
数据、家长端请假页面等问题已修复；新增的 HealthAiServiceImpl
实现了真实的 AI 视觉分析集成；WxPaymentController 和
WxMessageServiceImpl 搭建了微信支付的框架。

最紧迫的工作是安全加固（认证 + 支付验证 +
密钥脱敏）和数据库完整性修复（补建 3 张缺失表 + 添加索引）。其次是前端 7
个桩代码视图的补全和 API
路径不匹配的修复。测试覆盖率（22.6%）需要在中期提升到 60%
以上，优先覆盖费用、工资、健康、接送、审批、权限等高风险模块。
