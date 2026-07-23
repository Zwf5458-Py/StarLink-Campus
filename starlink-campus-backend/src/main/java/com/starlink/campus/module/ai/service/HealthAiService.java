package com.starlink.campus.module.ai.service;

import java.util.Map;

/**
 * 晨检智能视觉辅助服务
 */
public interface HealthAiService {
    
    /**
     * 分析晨检医疗图像 (如手掌/口腔)
     * @param imageUrl 图像的 URL
     * @return 返回分析结果 (包含置信度，疑似症状，警报等)
     */
    Map<String, Object> analyzeMedicalImage(String imageUrl);
}
