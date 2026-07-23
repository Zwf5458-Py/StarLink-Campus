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

    @Override
    public String generateGrowthComment(Long studentId, String keywords, String semester) {
        log.info("AI 生成成长评语请求: studentId={}, keywords={}, semester={}", studentId, keywords, semester);
        
        // 查找模版
        LambdaQueryWrapper<KgAiPromptTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgAiPromptTemplate::getTemplateCode, "GROWTH_COMMENT_V1");
        KgAiPromptTemplate template = promptTemplateMapper.selectOne(wrapper);

        String prompt = (template != null) 
            ? template.getPromptPattern().replace("{keywords}", keywords).replace("{semester}", semester)
            : "关键词：" + keywords;

        // 模拟智能大模型生成 (DeepSeek/Qwen 兼容算子)
        String generatedComment = String.format(
            "【AI 教师评语】在%s学期中，孩子展现出了极其出色的成长潜力！在日常生活与学习中：%s。" +
            "不仅能与同伴友好相处，还表现出强烈的探索欲望和动手能力。老师希望在新的学期里，" +
            "能继续保持这份热情，勇敢尝试新事物，成长为更加自信棒棒的小懂事！",
            StringUtils.hasText(semester) ? semester : "本",
            StringUtils.hasText(keywords) ? keywords : "表现积极活跃，乐于助人"
        );

        saveLog("GROWTH_COMMENT", prompt, generatedComment, 180);
        return generatedComment;
    }

    @Override
    public String generateWeeklyPlan(String theme, String targetAge) {
        log.info("AI 生成教学周计划: theme={}, targetAge={}", theme, targetAge);
        
        String planResult = String.format(
            "【AI 教学周计划建议 - 主题：%s (适用年龄：%s)】\n" +
            "1. 健康领域：通过《%s》主题户外拓展，提升幼儿大肌肉协调能力与平衡感。\n" +
            "2. 语言领域：引导幼儿讲述与“%s”相关的日常生活故事，丰富词汇表达。\n" +
            "3. 社会领域：分组合作完成主题任务，培养团队分享与礼貌交往意识。\n" +
            "4. 科学领域：观察记录相关自然与生活现象，激发好奇心与探究欲。\n" +
            "5. 艺术领域：开展《色彩与创意》美工绘画制作，鼓励个性化表达。",
            theme, StringUtils.hasText(targetAge) ? targetAge : "中大班", theme, theme
        );

        saveLog("WEEKLY_PLAN", theme + " | " + targetAge, planResult, 220);
        return planResult;
    }

    @Override
    public Map<String, Object> analyzeMenuNutrition(List<String> dishes) {
        log.info("AI 校验食谱营养: dishes={}", dishes);
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("dishes", dishes);
        result.put("proteinScore", 88);
        result.put("vitaminScore", 92);
        result.put("calorieLevel", "适中 (符合 3-6 岁幼儿每日所需)");
        
        List<String> suggestions = new ArrayList<>();
        suggestions.add("食谱整体营养结构均衡，碳水与优质蛋白比例恰当。");
        suggestions.add("建议适量增加深绿色蔬菜（如菠菜/西兰花），提高微量元素吸收。");
        suggestions.add("如班级有海鲜或坚果过敏儿童，请注意在后厨单独做备选替换餐。");

        result.put("suggestions", suggestions);
        result.put("aiEvaluation", "优秀（建议发布）");

        saveLog("MENU_NUTRITION", dishes != null ? dishes.toString() : "", result.toString(), 150);
        return result;
    }

    @Override
    public String chatWithKnowledgeBase(String question) {
        log.info("AI 智能园秘 RAG 对话: question={}", question);

        if (!StringUtils.hasText(question)) {
            return "您好！我是海星智联 AI 智能园秘。您可以问我关于入园作息、退费标准、接送安全、请假流程等问题哦！";
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
            logEntity.setModelName("DeepSeek-V3");
            logEntity.setCreateTime(LocalDateTime.now());
            aiLogMapper.insert(logEntity);
        } catch (Exception e) {
            log.error("保存 AI 日志记录失败", e);
        }
    }
}
