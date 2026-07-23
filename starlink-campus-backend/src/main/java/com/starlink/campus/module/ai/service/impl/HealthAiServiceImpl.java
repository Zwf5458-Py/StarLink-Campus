package com.starlink.campus.module.ai.service.impl;

import com.starlink.campus.module.ai.service.AiGatewayService;
import com.starlink.campus.module.ai.service.HealthAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class HealthAiServiceImpl implements HealthAiService {

    private static final Logger log = LoggerFactory.getLogger(HealthAiServiceImpl.class);

    @Autowired
    private AiGatewayService aiGatewayService;

    @Override
    public Map<String, Object> analyzeMedicalImage(String imageUrl) {
        log.info("开始 AI 医疗影像分析 (如晨检手足口病辅助): url={}", imageUrl);
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("imageUrl", imageUrl);
        
        String systemPrompt = "你是一名拥有10年经验的儿科医生。请分析用户提供的图片，重点检查幼儿手掌、口腔、足底是否存在红色斑点或水疱（手足口病早期特征）。" +
                "你必须且只能输出严格合法的 JSON，绝对不能包含 markdown 标记或额外文字。JSON 包含字段：\n" +
                "1. success (布尔型，固定返回 true)\n" +
                "2. confidence (字符串，置信度，如'95%')\n" +
                "3. hasWarning (布尔型，是否发现疑似红疹/水疱)\n" +
                "4. symptoms (字符串，发现的具体症状，无则写'未发现明显异常')\n" +
                "5. conclusion (字符串，最终诊断建议)";
        String userPrompt = "请分析这张幼儿晨检照片是否存在手足口病或其他常见皮肤病症状。";

        try {
            String jsonResult = aiGatewayService.analyzeImageAsync(systemPrompt, userPrompt, imageUrl).get(15, TimeUnit.SECONDS);
            jsonResult = jsonResult.replace("```json", "").replace("```", "").trim();
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> aiMap = mapper.readValue(jsonResult, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>(){});
            result.putAll(aiMap);
            
            if (Boolean.TRUE.equals(result.get("hasWarning"))) {
                log.warn("AI 医疗影像识别出异常: {}", result);
            }
            return result;
        } catch (Exception e) {
            log.error("AI 医疗影像分析异常或返回格式不正确", e);
            result.put("success", false);
            result.put("error", "AI 分析引擎暂时不可用，建议转人工判断");
            return result;
        }
    }
}
