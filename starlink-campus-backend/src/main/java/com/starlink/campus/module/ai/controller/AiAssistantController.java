package com.starlink.campus.module.ai.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.ai.entity.KgAiKnowledgeBase;
import com.starlink.campus.module.ai.service.AiAssistantService;
import com.starlink.campus.module.ai.service.HealthAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "AI 自动化助手")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/ai")
@CrossOrigin
public class AiAssistantController {

    @Autowired
    private AiAssistantService aiAssistantService;
    
    @Autowired
    private HealthAiService healthAiService;

    // =====================================
    // 原有 AI 接口
    // =====================================

    @Operation(summary = "AI 生成成长评语")
    @PostMapping("/growth-comment")
    public R<String> generateGrowthComment(
            @RequestParam(required = false) Long studentId,
            @RequestParam String keywords,
            @RequestParam(required = false) String semester) {
        return R.ok(aiAssistantService.generateGrowthComment(studentId, keywords, semester));
    }

    @Operation(summary = "AI 生成教学周计划建议")
    @PostMapping("/weekly-plan")
    public R<String> generateWeeklyPlan(
            @RequestParam String theme,
            @RequestParam(required = false) String targetAge) {
        return R.ok(aiAssistantService.generateWeeklyPlan(theme, targetAge));
    }

    @Operation(summary = "AI 评估每周食谱营养 (深度解析)")
    @PostMapping("/menu-nutrition")
    public R<Map<String, Object>> analyzeMenuNutrition(@Valid @RequestBody List<String> dishes) {
        return R.ok(aiAssistantService.analyzeMenuNutrition(dishes));
    }

    @Operation(summary = "AI 智能润色文案")
    @PostMapping("/polish-text")
    public R<String> polishText(@RequestParam String text) {
        return R.ok(aiAssistantService.polishText(text));
    }

    @Operation(summary = "AI 园秘 RAG 智能对话")
    @PostMapping("/chat")
    public R<String> chatWithKnowledgeBase(@Valid @RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        return R.ok(aiAssistantService.chatWithKnowledgeBase(question));
    }

    @Operation(summary = "晨检 AI 视觉医疗辅助分析")
    @PostMapping("/health-analyze")
    public R<Map<String, Object>> analyzeHealthImage(@RequestParam String imageUrl) {
        return R.ok(healthAiService.analyzeMedicalImage(imageUrl));
    }

    // =====================================
    // 知识库管理
    // =====================================

    @Operation(summary = "获取 AI 知识库列表")
    @GetMapping("/knowledge/list")
    public R<Page<KgAiKnowledgeBase>> listKnowledge(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return R.ok(aiAssistantService.listKnowledge(category, pageNum, pageSize));
    }

    @Operation(summary = "新增 AI 知识库条目")
    @PostMapping("/knowledge/add")
    public R<Boolean> addKnowledge(@Valid @RequestBody KgAiKnowledgeBase kb) {
        return R.ok(aiAssistantService.addKnowledge(kb));
    }

    @Operation(summary = "删除 AI 知识库条目")
    @DeleteMapping("/knowledge/delete/{id}")
    public R<Boolean> deleteKnowledge(@PathVariable Long id) {
        return R.ok(aiAssistantService.deleteKnowledge(id));
    }

    // =====================================
    // 第一批增强新增接口
    // =====================================

    @Operation(summary = "每日评语批量生成")
    @SaCheckRole(value = {"TEACHER", "ADMIN"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/batch-daily-comments")
    public R<List<Map<String, Object>>> generateBatchDailyComments(@Valid @RequestBody List<Map<String, Object>> studentDataList) {
        return R.ok(aiAssistantService.generateBatchDailyComments(studentDataList));
    }

    @Operation(summary = "家长消息智能回复草稿")
    @SaCheckRole(value = {"TEACHER", "ADMIN"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/parent-reply-draft")
    public R<String> generateParentMessageReplyDraft(
            @RequestParam String parentQuestion,
            @RequestParam String category) {
        return R.ok(aiAssistantService.generateParentMessageReplyDraft(parentQuestion, category));
    }

    @Operation(summary = "通知智能撰写")
    @SaCheckRole("ADMIN")
    @PostMapping("/notice-draft")
    public R<Map<String, String>> generateNoticeDraft(
            @RequestParam String keyPoints,
            @RequestParam String targetAudience) {
        return R.ok(aiAssistantService.generateNoticeDraft(keyPoints, targetAudience));
    }
}
