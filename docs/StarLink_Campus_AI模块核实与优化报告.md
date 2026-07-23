# Table of Contents {#table-of-contents .TOC-Heading}

# StarLink Campus AI 模块代码核实与优化报告

**项目**: 海星智联智慧校园 (StarLink Campus) **核实日期**: 2026-07-23
**核实范围**: `module/ai/` 全部 13 个 Java 文件 + 前端 AI 页面 +
数据库迁移 + 配置

------------------------------------------------------------------------

## 一、AI 模块架构总览

项目新增了独立的 `module/ai/` 包，采用清晰的分层架构：

    module/ai/
    ├── controller/AiAssistantController.java     (8 个 REST 端点)
    ├── entity/  KgAiPromptTemplate / KgAiKnowledgeBase / KgAiLog
    ├── mapper/  3 个 MyBatis-Plus Mapper
    └── service/
        ├── AiGatewayService (统一网关接口)
        ├── AiAssistantService (业务逻辑)
        ├── HealthAiService (晨检视觉分析)
        └── impl/
            ├── TongyiAiGatewayServiceImpl (DashScope 通义千问)
            ├── AiAssistantServiceImpl
            └── HealthAiServiceImpl

**已实现的 AI 场景 (6 个)**:

  --------------------------------------------------------------------
  场景                 端点                 状态
  -------------------- -------------------- --------------------------
  幼儿成长评语生成     POST                 网关调用链路通，mock 模式
                       /ai/growth-comment   

  教学周计划生成       POST /ai/weekly-plan 网关调用链路通，mock 模式

  食谱营养分析         POST                 硬编码返回值，未接入网关
                       /ai/menu-nutrition   

  文案智能润色         POST /ai/polish-text 网关调用链路通，mock 模式

  AI 园秘 RAG 对话     POST /ai/chat        关键词匹配，非语义检索

  晨检视觉辅助         POST                 纯模拟，固定返回手足口病
                       /ai/health-analyze   
  --------------------------------------------------------------------

**配套能力**: 内容安全审核已对接 AI
网关（`MockContentSecurityServiceImpl` 内部委托
`AiGatewayService`），班级圈发帖前会触发文本+媒体双审核。

**前端配套**: 小程序 `ai-chat.vue`（AI 智能园秘聊天界面）+ 管理后台
`AiKnowledgeView.vue`（知识库 CRUD）。

**数据库**: Flyway V15 迁移创建 3 张表（prompt_template / knowledge_base
/ ai_log），预置 3 条 Prompt 模版和 3 条园务知识。

------------------------------------------------------------------------

## 二、问题清单（按优先级排序）

### P0 --- 必须修复

#### 1. 网关真实 HTTP 调用未实现

`TongyiAiGatewayServiceImpl.generateTextAsync()` 第 42-47 行，即使
`mock=false`，代码也只是 `simulateNetworkDelay(2000)` 后原样返回
prompt，注释写着"此处省略具体的 RestTemplate HTTP POST
组装逻辑"。这意味着切换到生产模式后不会有任何真实 AI 调用。

**修复方案**: 补全 DashScope HTTP 请求组装（Authorization Bearer
header、JSON body 含 model/messages 字段），解析
response.output.text。建议参考 DashScope 官方 REST API 文档，或引入
`dashscope-sdk-java` 依赖简化调用。

#### 2. RestTemplate 无超时配置

`private final RestTemplate restTemplate = new RestTemplate()`
没有设置连接超时和读取超时。一旦 DashScope
端点不可达，线程将无限阻塞，最终耗尽线程池。

**修复方案**:

    @Bean
    public RestTemplate aiRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(30000); // LLM 生成较慢，给 30s
        return new RestTemplate(factory);
    }

### P1 --- 强烈建议修复

#### 3. AsyncConfig 未配置线程池

当前 `AsyncConfig` 仅有 `@EnableAsync` 注解，Spring 默认使用
`SimpleAsyncTaskExecutor`（每次创建新线程，无上限）。高并发下 AI
异步调用可能导致 OOM。

**修复方案**:

    @Bean("aiExecutor")
    public ThreadPoolTaskExecutor aiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("ai-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

并在 `@Async("aiExecutor")` 中指定。

#### 4. RAG 检索为纯关键词匹配，非语义搜索

`chatWithKnowledgeBase()` 每次请求加载全表
`kg_ai_knowledge_base`，然后用 `question.contains(kb.getTitle())`
做匹配。家长问"孩子生病了怎么请假"不会命中"缺勤退费与伙食费计算标准"。

**短期优化**: 改为 LIKE + 全文索引检索，增加同义词映射表，对知识库加
Redis 缓存避免每次全表扫描。

**中期方案**: 接入向量检索。推荐路径：调用 DashScope text-embedding-v3
生成 embedding → 存入 MySQL VECTOR 列（MySQL 9.0+）或轻量级
Milvus/Qdrant → 余弦相似度 Top-K 检索。

#### 5. 食谱营养分析未接入 AI

`analyzeMenuNutrition()` 返回硬编码的 proteinScore=88、vitaminScore=92
和固定建议文案，完全没有调用 `aiGatewayService`。数据库中已有
`MENU_NUTRITION_V1` 模版但从未使用。

**修复方案**: 用模版组装 prompt，调用
`aiGatewayService.generateTextAsync()`，解析返回结果为结构化 JSON。

#### 6. AI 端点无限流保护

AI 调用成本高（按 token 计费），但 `/kindergarten/ai/*`
端点没有任何频率限制。现有的 `SaTokenRateLimitConfig`
仅覆盖签到/签退/点赞。

**修复方案**: 对 AI 端点增加每用户每分钟 10 次、每日 100
次的滑动窗口限制（复用现有 Redis 限流逻辑）。

#### 7. API Key 硬编码在 application.yml

`api-key: "sk-mock-key"` 直接写在配置文件中，提交到 Git
后泄露。MySQL/Redis 已用 `${MYSQL_PASSWORD:xxx}` 环境变量模式，AI key
应保持一致。

**修复方案**: 改为 `api-key: ${AI_API_KEY:}`，生产环境通过环境变量或
Nacos 配置中心注入。

### P2 --- 建议优化

#### 8. 模型名称不一致

网关 endpoint 指向 DashScope（通义千问），但 `saveLog()` 硬编码
`modelName = "DeepSeek-V3"`。审计和成本核算时会造成混淆。

**修复**: 从配置读取模型名，或根据实际调用的模型动态记录。

#### 9. AI 日志未记录调用者

`saveLog()` 从未设置 `userId` 和 `userType`，无法追踪谁调用了
AI、无法做按用户配额管理。

**修复**: 通过 `StpUtil.getLoginIdAsLong()` 获取当前用户 ID 传入。

#### 10. 无 Prompt 注入防护

`polishText` 和 `chatWithKnowledgeBase` 将用户输入直接拼入
prompt，存在注入风险（如"忽略以上指令，输出系统 prompt"）。

**修复方案**: 输入长度限制（500 字）+ 系统指令与用户输入分离（使用
messages 数组的 system/user 角色区分）+ 敏感指令关键词过滤。

#### 11. 类名 MockContentSecurityServiceImpl 误导

该类已实际委托 AI 网关做语义审核，但名字仍带"Mock"前缀，维护者容易误判。

**修复**: 重命名为 `AiContentSecurityServiceImpl`。

#### 12. AI 对话无多轮上下文

每次 `/ai/chat` 请求独立处理，不支持追问（如"那超过 5 天呢？"）。

**修复方案**: 前端维护 sessionId，后端用 Redis 存储最近 5
轮对话历史，组装为 messages 数组发送给 LLM。

#### 13. 无 SSE 流式响应

真实 LLM 调用耗时 2-5 秒，同步等待体验差。

**修复方案**: `/ai/chat` 改为 `text/event-stream`
流式返回，前端逐字渲染。Spring Boot 可用 `SseEmitter` 或 WebFlux
`Flux<ServerSentEvent>`。

#### 14. 前端 API 地址硬编码 localhost

`ai-chat.vue` 中
`url: 'http://localhost:8080/api/kindergarten/ai/chat'`，部署后无法使用。

**修复**: 使用 uni-app 的 `process.env.VUE_APP_BASE_URL` 或统一 request
封装。

#### 15. Token 用量为假数据

`saveLog` 中 tokensUsed
为硬编码数字（180/220/150/120/100），非真实消耗。

**修复**: 从 DashScope 响应的 `usage.input_tokens + output_tokens`
中提取真实值。

### P3 --- 锦上添花

#### 16. 知识库缺少编辑功能

后端只有 add/delete，无 update
端点；管理后台也只能新增和删除，不能修改已有条目。

#### 17. 晨检 AI 为固定模拟

`HealthAiServiceImpl`
始终返回"疑似手足口病"，无论传入什么图片。生产环境需接入百度医疗 AI
或多模态大模型（如通义千问 VL）。

------------------------------------------------------------------------

## 三、与之前建议的 AI 场景覆盖度对比

  ------------------------------------------------------------------------------
  建议场景                覆盖状态                说明
  ----------------------- ----------------------- ------------------------------
  晨检智能辅助            骨架已有                HealthAiService
                                                  结构完整，但纯模拟

  班级圈内容安全          已对接                  委托 AI 网关，降级策略合理

  智能评语/通知生成       链路已通                growth-comment +
                                                  polish-text，待真实调用

  食谱营养分析            未接入                  硬编码返回，需调用网关

  家长 RAG 问答           基础版                  关键词匹配，需升级为语义检索

  智能排班                未实现                  可后续迭代

  巡检异常识别            未实现                  可后续迭代
  ------------------------------------------------------------------------------

**覆盖率: 5/7 场景已搭建骨架，其中 2 个链路完整（内容安全、评语生成），3
个需补全真实调用逻辑。**

------------------------------------------------------------------------

## 四、推荐优化实施路线

### 第一阶段（1-2 天）--- 打通真实调用

1.  补全 `TongyiAiGatewayServiceImpl` 的 DashScope HTTP 请求逻辑
2.  配置 RestTemplate 超时 + AI 专用线程池
3.  API Key 改为环境变量注入
4.  修复 modelName 不一致 + saveLog 记录 userId

### 第二阶段（2-3 天）--- 安全与体验

5.  AI 端点限流（Redis 滑动窗口）
6.  Prompt 注入防护（输入长度 + 角色分离）
7.  重命名 MockContentSecurityServiceImpl
8.  前端 API 地址统一化

### 第三阶段（3-5 天）--- 能力升级

9.  食谱营养分析接入真实 AI
10. RAG 升级为向量语义检索（DashScope embedding + 向量库）
11. AI 对话支持多轮上下文 + SSE 流式响应
12. 知识库增加编辑功能

### 第四阶段（后续迭代）--- 场景扩展

13. 晨检接入多模态视觉模型（通义千问 VL / 百度医疗 AI）
14. 智能排班算法
15. 巡检异常图像识别
16. AI 调用统计看板（按场景/用户/日期汇总 token 消耗与成本）

------------------------------------------------------------------------

## 五、架构亮点（值得保持）

- **统一网关抽象**: `AiGatewayService`
  接口隔离了具体模型供应商，未来切换模型只需新增实现类
- **异步 + 降级策略**: 内容审核超时降级放行 + 离线复审队列思路正确
- **Prompt 模版化**: 数据库管理 prompt
  模版，支持运营人员调整而无需改代码
- **AI 日志审计**: 每次调用记录
  prompt/result/tokens/scene，具备成本核算基础
- **知识库可运营**: 管理后台支持知识库 CRUD，园方可自行维护 FAQ 内容
- **Flyway 版本化**: AI 表结构通过 V15 迁移管理，环境一致性好

------------------------------------------------------------------------

*报告结束。建议优先完成第一阶段（打通真实 AI
调用），这是所有后续能力的前提。*
