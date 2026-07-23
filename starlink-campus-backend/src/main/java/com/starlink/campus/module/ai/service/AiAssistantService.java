package com.starlink.campus.module.ai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.ai.entity.KgAiKnowledgeBase;

import java.util.List;
import java.util.Map;

public interface AiAssistantService {
    String generateGrowthComment(Long studentId, String keywords, String semester);
    String generateWeeklyPlan(String theme, String targetAge);
    Map<String, Object> analyzeMenuNutrition(List<String> dishes);
    String chatWithKnowledgeBase(String question);
    String polishText(String text);
    Page<KgAiKnowledgeBase> listKnowledge(String category, Integer pageNum, Integer pageSize);
    boolean addKnowledge(KgAiKnowledgeBase kb);
    boolean deleteKnowledge(Long id);
}
