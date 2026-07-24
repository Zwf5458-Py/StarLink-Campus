package com.starlink.campus.module.ai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.ai.entity.KgAiKnowledgeBase;

import java.util.List;
import java.util.Map;

public interface AiAssistantService {
    // 现有功能
    String generateGrowthComment(Long studentId, String keywords, String semester);
    String generateWeeklyPlan(String theme, String targetAge);
    Map<String, Object> analyzeMenuNutrition(List<String> dishes);
    String chatWithKnowledgeBase(String question);
    String polishText(String text);
    Page<KgAiKnowledgeBase> listKnowledge(String category, Integer pageNum, Integer pageSize);
    boolean addKnowledge(KgAiKnowledgeBase kb);
    boolean deleteKnowledge(Long id);

    // 第一批增强新增功能
    List<Map<String, Object>> generateBatchDailyComments(List<Map<String, Object>> studentDataList);
    String generateParentMessageReplyDraft(String parentQuestion, String category);
    Map<String, String> generateNoticeDraft(String keyPoints, String targetAudience);
}
