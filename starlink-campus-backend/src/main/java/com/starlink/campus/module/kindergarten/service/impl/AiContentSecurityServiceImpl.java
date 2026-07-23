package com.starlink.campus.module.kindergarten.service.impl;

import com.starlink.campus.module.ai.service.AiGatewayService;
import com.starlink.campus.module.kindergarten.service.ContentSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 真实内容安全审核服务实现 (接入 AI 网关)
 * 替代原有的 MockContentSecurityServiceImpl
 */
@Service
public class AiContentSecurityServiceImpl implements ContentSecurityService {

    private static final Logger log = LoggerFactory.getLogger(AiContentSecurityServiceImpl.class);

    @Autowired
    private AiGatewayService aiGatewayService;

    @Override
    public boolean checkTextSecurity(String content) {
        if (content == null || content.trim().isEmpty()) {
            return true;
        }
        try {
            // 调用统一 AI 网关进行真正的语义级别大模型审核
            // 设置超时时间，保证不阻塞主线程过久
            return aiGatewayService.checkTextSecurityAsync(content)
                    .get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[内容安全] 文本审核网关调用超时或异常，降级通过", e);
            // 降级策略: 遇到大模型不可用时，暂时放行，后续可转入人工后台离线复审队列
            return true;
        }
    }

    @Override
    public boolean checkMediaSecurity(String mediaUrl) {
        if (mediaUrl == null || mediaUrl.trim().isEmpty()) {
            return true;
        }
        try {
            // 调用统一 AI 网关进行多模态视觉审核
            return aiGatewayService.checkMediaSecurityAsync(mediaUrl)
                    .get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[内容安全] 媒体审核网关调用超时或异常，降级通过: {}", mediaUrl, e);
            return true;
        }
    }
}
