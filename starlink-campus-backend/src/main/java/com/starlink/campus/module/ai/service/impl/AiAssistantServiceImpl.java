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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.starlink.campus.module.ai.service.AiGatewayService;

import java.time.LocalDateTime;
import java.util.*;

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

        // 查找模版
        LambdaQueryWrapper<KgAiPromptTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgAiPromptTemplate::getTemplateCode, "GROWTH_COMMENT_V1");
        KgAiPromptTemplate template = promptTemplateMapper.selectOne(wrapper);

        String systemPrompt = "你是一名资深幼儿园教师。请根据用户提供的表现标签，为学期末的学生成长档案撰写温馨专业的评语。";
        String userPrompt = (template != null) 
            ? template.getPromptPattern().replace("{keywords}", keywords).replace("{semester}", semester)
            : "学期：" + semester + "\n关键词：" + keywords;

        try {
            // 调用网关获取真实的 AI 生成内容（同步等待最多5秒）
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
        
        if (theme != null && theme.length() > 100) {
            return "【系统提示】主题过长，请精简。";
        }
        
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

        String systemPrompt = "你是一名高级儿童营养师。请分析用户提供的幼儿园食谱，并仅输出合法的 JSON 格式数据。JSON 必须包含以下字段：\n" +
                "1. proteinScore (整数，蛋白质得分0-100)\n" +
                "2. vitaminScore (整数，维生素得分0-100)\n" +
                "3. calorieLevel (字符串，热量水平描述，如'适中')\n" +
                "4. suggestions (字符串数组，至少提供3条专业营养建议)\n" +
                "5. aiEvaluation (字符串，总体评价，如'优秀')\n" +
                "绝不能包含任何 Markdown 标记 (如 ```json) 或其他文字。";
        String userPrompt = "食谱列表：" + String.join(", ", dishes);

        try {
            String jsonResult = aiGatewayService.generateTextAsync(systemPrompt, userPrompt).get(10, java.util.concurrent.TimeUnit.SECONDS);
            // 简单清理可能存在的 markdown 标记
            jsonResult = jsonResult.replace("```json", "").replace("```", "").trim();
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> aiMap = mapper.readValue(jsonResult, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>(){});
            result.putAll(aiMap);
            saveLog("MENU_NUTRITION", userPrompt, jsonResult, 200);
        } catch (Exception e) {
            log.error("AI 校验食谱营养失败或返回非合法 JSON", e);
            result.put("proteinScore", 80);
            result.put("vitaminScore", 80);
            result.put("calorieLevel", "未知");
            result.put("suggestions", Arrays.asList("AI 引擎暂时无法分析，请人工核验。"));
            result.put("aiEvaluation", "系统繁忙，降级返回");
            saveLog("MENU_NUTRITION", userPrompt, "【AI 失败】降级处理", 50);
        }

        return result;
    }

    @Override
    public String chatWithKnowledgeBase(String question) {
        log.info("AI 智能园秘 RAG 对话: question={}", question);

        if (!StringUtils.hasText(question)) {
            return "您好！我是海星智联 AI 智能园秘。您可以问我关于入园作息、退费标准、接送安全、请假流程等问题哦！";
        }
        if (question.length() > 200) {
            return "【系统提示】您的问题过长，请精简后重试。";
        }
        if (question.matches(".*(忽略|系统提示|prompt|指令).*")) {
            return "【系统提示】包含非法指令，拒绝回答。";
        }

        // 检索本地 RAG 知识库
        LambdaQueryWrapper<KgAiKnowledgeBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgAiKnowledgeBase::getStatus, "启用");
        List<KgAiKnowledgeBase> kbList = knowledgeBaseMapper.selectList(wrapper);

        KgAiKnowledgeBase matchedKb = null;
        for (KgAiKnowledgeBase kb : kbList) {
            if (question.contains(kb.getTitle()) || 
               (kb.getTags() != null && Arrays.stream(kb.getTags().split(",")).anyMatch(question::contains))) {
                matchedKb = kb;
                break;
            }
        }

        String answer;
        if (matchedKb != null) {
            answer = String.format("🤖 【海星 AI 园秘权威解答 - %s】\n\n%s\n\n(来源：%s 规则文档)", 
                    matchedKb.getTitle(), matchedKb.getContent(), matchedKb.getCategory());
        } else {
            answer = String.format("🤖 您询问的问题：“%s”。根据园区规定，通常建议您提前在微信小程序【园务中心】发起申请或联系班级主班老师。如需了解详细作息与收费规则，也可随时向我咨询！", question);
        }

        saveLog("RAG_CHAT", question, answer, 120);
        return answer;
    }

    @Override
    public String polishText(String text) {
        log.info("AI 润色文本: {}", text);
        if (!StringUtils.hasText(text)) {
            return text;
        }
        if (text.length() > 500) {
            return "【系统提示】输入文本过长，请控制在500字以内。";
        }
        if (text.matches(".*(忽略|系统提示|prompt|指令).*")) {
            return "【系统提示】包含非法指令，拒绝润色。";
        }
        
        String systemPrompt = "请作为幼儿园教师，将用户的关键词润色为一段温馨、专业、口语化且适合发送给家长的通知或日常动态，要求语句通顺，情感真挚。不要包含多余的自我介绍。";
        try {
            String polished = aiGatewayService.generateTextAsync(systemPrompt, text).get(5, java.util.concurrent.TimeUnit.SECONDS);
            saveLog("TEXT_POLISH", text, polished, 100);
            return polished;
        } catch (Exception e) {
            log.error("调用大模型润色文本失败", e);
            return "【AI 润色失败】" + text;
        }
    }

    @Override
    public Page<KgAiKnowledgeBase> listKnowledge(String category, Integer pageNum, Integer pageSize) {
        Page<KgAiKnowledgeBase> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<KgAiKnowledgeBase> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(category)) {
            wrapper.eq(KgAiKnowledgeBase::getCategory, category);
        }
        wrapper.orderByDesc(KgAiKnowledgeBase::getCreateTime);
        return knowledgeBaseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean addKnowledge(KgAiKnowledgeBase kb) {
        if (kb.getCreateTime() == null) {
            kb.setCreateTime(LocalDateTime.now());
        }
        if (!StringUtils.hasText(kb.getStatus())) {
            kb.setStatus("启用");
        }
        return knowledgeBaseMapper.insert(kb) > 0;
    }

    @Override
    public boolean deleteKnowledge(Long id) {
        return knowledgeBaseMapper.deleteById(id) > 0;
    }

    private void saveLog(String sceneType, String prompt, String result, Integer tokens) {
        try {
            KgAiLog logEntity = new KgAiLog();
            logEntity.setSceneType(sceneType);
            logEntity.setPrompt(prompt);
            logEntity.setResult(result);
            logEntity.setTokensUsed(tokens);
            logEntity.setModelName("qwen-turbo");
            
            try {
                if (cn.dev33.satoken.stp.StpUtil.isLogin()) {
                    logEntity.setUserId(cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong());
                    logEntity.setUserType("USER");
                }
            } catch (Exception ignore) {
                // Not in request scope or not logged in
            }
            
            logEntity.setCreateTime(LocalDateTime.now());
            aiLogMapper.insert(logEntity);
        } catch (Exception e) {
            log.error("保存 AI 日志记录失败", e);
        }
    }
}
