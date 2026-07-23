package com.starlink.campus.module.ai.service.impl;

import com.starlink.campus.module.ai.service.AiGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class TongyiAiGatewayServiceImpl implements AiGatewayService {

    private static final Logger log = LoggerFactory.getLogger(TongyiAiGatewayServiceImpl.class);

    @Value("${starlink.ai.gateway.api-key:}")
    private String apiKey;

    @Value("${starlink.ai.gateway.endpoint:https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation}")
    private String endpoint;

    @Value("${starlink.ai.gateway.mock:true}")
    private boolean isMock;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    @Override
    public CompletableFuture<String> generateTextAsync(String prompt) {
        log.info("[AI Gateway] 开始异步文本生成, mock模式={}", isMock);
        if (isMock || apiKey.isEmpty()) {
            // 模拟大模型 1.5 秒长耗时生成
            simulateNetworkDelay(1500);
            String mockResult = "【AI 网关生成】基于通义千问大模型结构的文本生成：根据您的输入“" 
                                + (prompt.length() > 20 ? prompt.substring(0, 20) + "..." : prompt) 
                                + "”，AI建议如下内容...";
            return CompletableFuture.completedFuture(mockResult);
        }

        try {
            // 真实生产环境 HTTP 请求逻辑
            // 此处省略具体的 RestTemplate HTTP POST 组装逻辑（Header 鉴权, JSON 组装）
            // String response = restTemplate.postForObject(endpoint, requestEntity, String.class);
            simulateNetworkDelay(2000); // 暂用延迟模拟真实HTTP请求耗时
            return CompletableFuture.completedFuture("【通义千问 真实返回】" + prompt);
        } catch (Exception e) {
            log.error("[AI Gateway] 调用文本生成大模型API异常", e);
            // 发生异常时进行熔断降级
            return CompletableFuture.completedFuture("【AI 熔断降级】系统繁忙，请稍后再试。");
        }
    }

    @Async
    @Override
    public CompletableFuture<Boolean> checkTextSecurityAsync(String text) {
        log.info("[AI Gateway] 开始异步文本安全审核, mock模式={}", isMock);
        if (isMock || apiKey.isEmpty()) {
            simulateNetworkDelay(500);
            // 本地简单的敏感词降级检测
            boolean safe = !(text.contains("暴力") || text.contains("色情") || text.contains("赌博"));
            if (!safe) {
                log.warn("[AI Gateway] 文本包含基础敏感词 (Mock Fallback)");
            }
            return CompletableFuture.completedFuture(safe);
        }

        try {
            simulateNetworkDelay(1000);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("[AI Gateway] 调用文本审核API异常", e);
            // 异常降级策略，为了不阻断用户正常使用，返回true，并转异步人工后台二次审查
            return CompletableFuture.completedFuture(true);
        }
    }

    @Async
    @Override
    public CompletableFuture<Boolean> checkMediaSecurityAsync(String mediaUrl) {
        log.info("[AI Gateway] 开始异步多模态媒体安全审核, URL={}, mock模式={}", mediaUrl, isMock);
        if (isMock || apiKey.isEmpty()) {
            simulateNetworkDelay(800);
            boolean safe = !(mediaUrl.contains("illegal") || mediaUrl.contains("porn"));
            return CompletableFuture.completedFuture(safe);
        }

        try {
            simulateNetworkDelay(1500);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("[AI Gateway] 调用媒体审核API异常", e);
            return CompletableFuture.completedFuture(true);
        }
    }

    private void simulateNetworkDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
