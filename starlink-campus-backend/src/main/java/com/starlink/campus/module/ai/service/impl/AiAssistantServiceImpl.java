package com.starlink.campus.module.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.ai.entity.KgAiKnowledgeBase;
import com.starlink.campus.module.ai.entity.KgAiLog;
import com.starlink.campus.module.ai.entity.KgAiPromptTemplate;
import com.starlink.campus.module.ai.mapper.KgAiKnowledgeBaseMapper;
import com.starlink.campus.module.ai.mapper.KgAiLogMapper;
import com.starlink.campus.module.ai.mapper.KgAiPromptTemplateMapper;
import com.starlink.campus.module.ai.service.AiAssistantService;
import com.starlink.campus.module.ai.service.AiGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    private static final Logger log = LoggerFactory.getLogger(AiAssistantServiceImpl.class);

    @Autowired
    private KgAiPromptTemplateMapper promptTemplateMapper;

    @Autowired
    private KgAiKnowledgeBaseMapper knowledgeBaseMapper;

    @Autowired
    private KgAiLogMapper aiLogMapper;

    @Autowired
    private AiGatewayService aiGatewayService;

    @Override
    public String generateGrowthComment(Long studentId, String keywords, String semester) {
        log.info("AI 生成成长评语请求: studentId={}, keywords={}, semester={}", studentId, keywords, semester);
        if (keywords != null && keywords.length() > 200) {
            return "【系统提示】关键词过长，请精简后重试。";
        }
        LambdaQueryWrapper<KgAiPromptTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgAiPromptTemplate::getTemplateCode, "GROWTH_COMMENT_V1");
        KgAiPromptTemplate template = promptTemplateMapper.selectOne(wrapper);
        String systemPrompt = "你是一名资深幼儿园教师。请根据用户提供的表现标签，为学期末的学生成长档案撰写温馨专业的评语。";
        String userPrompt = (template != null) 
            ? template.getPromptPattern().replace("{keywords}", keywords).replace("{semester}", semester)
            : "学期：" + semester + "\n关键词：" + keywords;
        try {
            String generatedComment = aiGatewayService.generateTextAsync(systemPrompt, userPrompt).get(5, java.util.concurrent.TimeUnit.SECONDS);
            saveLog("GROWTH_COMMENT", userPrompt, generatedComment, 180);
            return generatedComment;
        } catch (Exception e) {
            log.error("调用大模型生成评语失败", e);
            return "【系统提示】AI 助手暂时繁忙，建议人工填写。";
        }
    }

    @Override
    public String generateWeeklyPlan(String theme, String targetAge) {
        log.info("AI 生成教学周计划: theme={}, targetAge={}", theme, targetAge);
        if (theme != null && theme.length() > 100) return "【系统提示】主题过长，请精简。";
        String systemPrompt = "你是一名幼儿园教学主任。请根据提供的主题生成一份结构清晰的教学周计划，包含五大领域(健康、语言、社会、科学、艺术)的简要建议。";
        String userPrompt = String.format("主题：“%s”，班级：%s", theme, targetAge);
        try {
            String planResult = aiGatewayService.generateTextAsync(systemPrompt, userPrompt).get(5, java.util.concurrent.TimeUnit.SECONDS);
            saveLog("WEEKLY_PLAN", theme + " | " + targetAge, planResult, 220);
            return planResult;
        } catch (Exception e) {
            log.error("调用大模型生成周计划失败", e);
            return "【系统提示】AI 周计划生成失败，请稍后重试。";
        }
    }

    @Override
    public Map<String, Object> analyzeMenuNutrition(List<String> dishes) {
        log.info("AI 校验食谱营养: dishes={}", dishes);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("dishes", dishes);
        if (dishes == null || dishes.isEmpty()) {
            result.put("error", "食谱为空");
            return result;
        }
        String systemPrompt = "你是一名高级儿童营养师。请深度分析用户提供的幼儿园食谱。并严格输出合法且不带markdown包裹的纯JSON格式数据。\n" +
                "必须包含以下字段：\n" +
                "1. proteinScore (整数0-100)\n" +
                "2. vitaminScore (整数0-100)\n" +
                "3. calorieLevel (字符串，如'适中'/'偏高'/'偏低')\n" +
                "4. macroRatio (字符串，三大营养素大概比例，如'碳水50% 蛋白30% 脂肪20%')\n" +
                "5. suggestions (字符串数组，提供3-5条明确改进建议)\n" +
                "6. aiEvaluation (字符串，总体评价)";
        String userPrompt = "食谱列表：" + String.join(", ", dishes);
        try {
            String jsonResult = aiGatewayService.generateTextAsync(systemPrompt, userPrompt).get(15, java.util.concurrent.TimeUnit.SECONDS);
            jsonResult = jsonResult.replace("```json", "").replace("```", "").trim();
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> aiMap = mapper.readValue(jsonResult, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>(){});
            result.putAll(aiMap);
            saveLog("MENU_NUTRITION", userPrompt, jsonResult, 250);
        } catch (Exception e) {
            log.error("AI 校验食谱营养失败或返回非合法 JSON", e);
            result.put("proteinScore", 80);
            result.put("vitaminScore", 80);
            result.put("calorieLevel", "未知");
            result.put("macroRatio", "计算失败");
            result.put("suggestions", Arrays.asList("AI 引擎暂时无法分析，请人工核验。"));
            result.put("aiEvaluation", "系统繁忙，降级返回");
            saveLog("MENU_NUTRITION", userPrompt, "【AI 失败】降级处理", 50);
        }
        return result;
    }

    @Override
    public String chatWithKnowledgeBase(String question) {
        if (!StringUtils.hasText(question)) return "您好！我是海星智联 AI 智能园秘...";
        if (question.length() > 200) return "【系统提示】您的问题过长，请精简后重试。";
        LambdaQueryWrapper<KgAiKnowledgeBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgAiKnowledgeBase::getStatus, "启用");
        List<KgAiKnowledgeBase> kbList = knowledgeBaseMapper.selectList(wrapper);
        KgAiKnowledgeBase matchedKb = null;
        for (KgAiKnowledgeBase kb : kbList) {
            if (question.contains(kb.getTitle()) || (kb.getTags() != null && Arrays.stream(kb.getTags().split(",")).anyMatch(question::contains))) {
                matchedKb = kb;
                break;
            }
        }
        String answer;
        if (matchedKb != null) {
            answer = String.format("🤖 【海星 AI 园秘权威解答 - %s】\n\n%s\n\n(来源：%s 规则文档)", matchedKb.getTitle(), matchedKb.getContent(), matchedKb.getCategory());
        } else {
            answer = String.format("🤖 您询问的问题：“%s”。建议您提前在微信小程序发起申请。如需了解详细规则，也可随时向我咨询！", question);
        }
        saveLog("RAG_CHAT", question, answer, 120);
        return answer;
    }

    @Override
    public String polishText(String text) {
        if (!StringUtils.hasText(text)) return text;
        if (text.length() > 500) return "【系统提示】输入文本过长，请控制在500字以内。";
        String systemPrompt = "请作为幼儿园教师，将用户的关键词润色为一段温馨、专业、口语化且适合发送给家长的通知。要求语句通顺，情感真挚，不要包含多余自我介绍。";
        try {
            String polished = aiGatewayService.generateTextAsync(systemPrompt, text).get(5, java.util.concurrent.TimeUnit.SECONDS);
            saveLog("TEXT_POLISH", text, polished, 100);
            return polished;
        } catch (Exception e) {
            return "【AI 润色失败】" + text;
        }
    }

    @Override
    public Page<KgAiKnowledgeBase> listKnowledge(String category, Integer pageNum, Integer pageSize) {
        Page<KgAiKnowledgeBase> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<KgAiKnowledgeBase> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(category)) wrapper.eq(KgAiKnowledgeBase::getCategory, category);
        wrapper.orderByDesc(KgAiKnowledgeBase::getCreateTime);
        return knowledgeBaseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean addKnowledge(KgAiKnowledgeBase kb) {
        if (kb.getCreateTime() == null) kb.setCreateTime(LocalDateTime.now());
        if (!StringUtils.hasText(kb.getStatus())) kb.setStatus("启用");
        return knowledgeBaseMapper.insert(kb) > 0;
    }

    @Override
    public boolean deleteKnowledge(Long id) {
        return knowledgeBaseMapper.deleteById(id) > 0;
    }

    // ==========================================
    // 第一批增强新增功能
    // ==========================================

    @Override
    public List<Map<String, Object>> generateBatchDailyComments(List<Map<String, Object>> studentDataList) {
        log.info("AI 批量生成每日评语请求，学生数量: {}", studentDataList.size());
        
        String systemPrompt = "你是一名负责且充满爱的幼师。请根据提供的每个幼儿的一日表现数据（包含饮食、午休、情绪、活动表现等标签），" +
                "为每个孩子分别生成一段50字左右的专属每日评语。必须直接以合法的JSON数组返回，每个对象包含 studentId (Long) 和 comment (String) 字段。不要有Markdown语法和额外文字。";

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String userPrompt = mapper.writeValueAsString(studentDataList);

            // TODO: In a real large batch, we might chunk this or process concurrently per student.
            // For simple implementation, assuming the payload is small enough for one AI call:
            String jsonResult = aiGatewayService.generateTextAsync(systemPrompt, userPrompt).get(15, java.util.concurrent.TimeUnit.SECONDS);
            jsonResult = jsonResult.replace("```json", "").replace("```", "").trim();

            List<Map<String, Object>> resultList = mapper.readValue(jsonResult, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>(){});
            saveLog("BATCH_DAILY_COMMENT", "批量生成数: " + studentDataList.size(), "生成成功", 300);
            return resultList;

        } catch (Exception e) {
            log.error("AI 批量生成评语失败", e);
            saveLog("BATCH_DAILY_COMMENT", "批量生成", "【AI 失败】" + e.getMessage(), 0);
            // Fallback: Return simple formatted string for each
            return studentDataList.stream().map(data -> {
                Map<String, Object> fallbackMap = new HashMap<>();
                fallbackMap.put("studentId", data.get("studentId"));
                fallbackMap.put("comment", "今日表现：饮食" + data.get("diet") + "，睡眠" + data.get("sleep") + "，情绪" + data.get("emotion") + "。（AI生成失败，此为默认模板）");
                return fallbackMap;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public String generateParentMessageReplyDraft(String parentQuestion, String category) {
        log.info("AI 生成家长消息回复草稿: category={}, question={}", category, parentQuestion);
        String systemPrompt = "你是一名沟通能力极强的优秀幼儿园班主任。面对家长的消息，请撰写一段专业、礼貌、温和且能有效安抚或解答家长疑问的回复草稿。" +
                "要求：体现同理心、避免生硬冷漠、直接给出回复的正文，不需要自我介绍，字数在100-200字之间。";
        String userPrompt = String.format("场景类型：%s\n家长原话：%s", category, parentQuestion);
        try {
            String draft = aiGatewayService.generateTextAsync(systemPrompt, userPrompt).get(8, java.util.concurrent.TimeUnit.SECONDS);
            saveLog("PARENT_REPLY_DRAFT", userPrompt, draft, 150);
            return draft;
        } catch (Exception e) {
            log.error("调用大模型生成回复草稿失败", e);
            return "【AI 助手】感谢您的反馈，老师已收到您的消息，稍后将由主班老师详细回复您。";
        }
    }

    @Override
    public Map<String, String> generateNoticeDraft(String keyPoints, String targetAudience) {
        log.info("AI 生成通知草稿: audience={}, keyPoints={}", targetAudience, keyPoints);
        Map<String, String> result = new HashMap<>();
        String systemPrompt = "你是一名资深幼教行政主管。请根据提供的通知要点，撰写一份符合幼儿园调性的正式通知。" +
                "请严格按JSON格式返回，包含字段：1. title (通知标题) 2. content (通知正文段落，包括问候语、事项、注意事项和落款温馨语)。不含任何markdown代码块。";
        String userPrompt = String.format("通知对象：%s\n通知核心要点：%s", targetAudience, keyPoints);
        
        try {
            String jsonResult = aiGatewayService.generateTextAsync(systemPrompt, userPrompt).get(10, java.util.concurrent.TimeUnit.SECONDS);
            jsonResult = jsonResult.replace("```json", "").replace("```", "").trim();
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, String> aiMap = mapper.readValue(jsonResult, new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>(){});
            result.putAll(aiMap);
            saveLog("NOTICE_DRAFT", userPrompt, jsonResult, 200);
        } catch (Exception e) {
            log.error("AI 撰写通知失败", e);
            result.put("title", "【重要通知】");
            result.put("content", "亲爱的家长们/同事们：\n\n关于：\n" + keyPoints + "\n\n请您知悉。 (AI生成失败，请人工补充完善)");
            saveLog("NOTICE_DRAFT", userPrompt, "【AI 失败】", 50);
        }
        return result;
    }

    @org.springframework.beans.factory.annotation.Value("${ai.model:qwen-turbo}")
    private String defaultModelName;

    private void saveLog(String sceneType, String prompt, String result, Integer tokens) {
        try {
            KgAiLog logEntity = new KgAiLog();
            logEntity.setSceneType(sceneType);
            logEntity.setPrompt(prompt);
            logEntity.setResult(result);
            logEntity.setTokensUsed(tokens);
            logEntity.setModelName(defaultModelName);
            try {
                if (cn.dev33.satoken.stp.StpUtil.isLogin()) {
                    logEntity.setUserId(cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong());
                    logEntity.setUserType("USER");
                }
            } catch (Exception ignore) {}
            logEntity.setCreateTime(LocalDateTime.now());
            aiLogMapper.insert(logEntity);
        } catch (Exception e) {
            log.error("保存 AI 日志记录失败", e);
        }
    }
}
