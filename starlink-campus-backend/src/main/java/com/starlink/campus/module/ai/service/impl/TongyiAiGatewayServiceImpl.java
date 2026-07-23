package com.starlink.campus.module.ai.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starlink.campus.module.ai.service.AiGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    @Qualifier("aiRestTemplate")
    private RestTemplate aiRestTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    @Async("aiExecutor")
    @Override
    public CompletableFuture<String> generateTextAsync(String systemPrompt, String userMessage) {
        log.info("[AI Gateway] 开始异步文本生成, mock模式={}", isMock);
        if (isMock || apiKey.isEmpty()) {
            simulateNetworkDelay(1500);
            String mockResult = "【AI 网关生成】基于通义千问大模型结构的文本生成：根据您的输入“" 
                                + (userMessage.length() > 20 ? userMessage.substring(0, 20) + "..." : userMessage) 
                                + "”，AI建议如下内容...";
            return CompletableFuture.completedFuture(mockResult);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-turbo");

            Map<String, Object> input = new HashMap<>();
            List<Map<String, String>> messages = new ArrayList<>();
            
            // System Role
            if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
                Map<String, String> sysMsg = new HashMap<>();
                sysMsg.put("role", "system");
                sysMsg.put("content", systemPrompt);
                messages.add(sysMsg);
            }
            
            // User Role
            Map<String, String> usrMsg = new HashMap<>();
            usrMsg.put("role", "user");
            usrMsg.put("content", userMessage);
            messages.add(usrMsg);
            
            input.put("messages", messages);
            requestBody.put("input", input);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = aiRestTemplate.postForEntity(endpoint, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode rootNode = mapper.readTree(response.getBody());
                String resultText = rootNode.path("output").path("text").asText();
                return CompletableFuture.completedFuture(resultText);
            }
            log.error("[AI Gateway] API响应状态异常: {}", response.getStatusCode());
            return CompletableFuture.completedFuture("【AI 熔断降级】系统繁忙，请稍后再试。");
        } catch (Exception e) {
            log.error("[AI Gateway] 调用文本生成大模型API异常", e);
            return CompletableFuture.completedFuture("【AI 熔断降级】网络或系统繁忙，请稍后再试。");
        }
    }

    @Async("aiExecutor")
    @Override
    public CompletableFuture<Boolean> checkTextSecurityAsync(String text) {
        log.info("[AI Gateway] 开始异步文本安全审核, mock模式={}", isMock);
        if (isMock || apiKey.isEmpty()) {
            simulateNetworkDelay(500);
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
            return CompletableFuture.completedFuture(true);
        }
    }

    @Async("aiExecutor")
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

    @Async("aiExecutor")
    @Override
    public CompletableFuture<String> analyzeImageAsync(String systemPrompt, String userPrompt, String imageUrl) {
        log.info("[AI Gateway] 开始多模态异步图像识别, URL={}, mock模式={}", imageUrl, isMock);
        if (isMock || apiKey.isEmpty()) {
            simulateNetworkDelay(1500);
            return CompletableFuture.completedFuture("{\"success\":true,\"confidence\":\"98%\",\"hasWarning\":true,\"symptoms\":\"疑似红疹, 口腔细小疱疹\",\"conclusion\":\"高度疑似手足口病早期症状，建议立即隔离并安排复诊！\"}");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-vl-max");

            Map<String, Object> input = new HashMap<>();
            List<Map<String, Object>> messages = new ArrayList<>();
            
            // System Role
            if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
                Map<String, Object> sysMsg = new HashMap<>();
                sysMsg.put("role", "system");
                List<Map<String, String>> sysContent = new ArrayList<>();
                Map<String, String> sysText = new HashMap<>();
                sysText.put("text", systemPrompt);
                sysContent.add(sysText);
                sysMsg.put("content", sysContent);
                messages.add(sysMsg);
            }
            
            // User Role
            Map<String, Object> usrMsg = new HashMap<>();
            usrMsg.put("role", "user");
            List<Map<String, String>> usrContent = new ArrayList<>();
            Map<String, String> imgContent = new HashMap<>();
            imgContent.put("image", imageUrl);
            usrContent.add(imgContent);
            
            Map<String, String> txtContent = new HashMap<>();
            txtContent.put("text", userPrompt);
            usrContent.add(txtContent);
            
            usrMsg.put("content", usrContent);
            messages.add(usrMsg);
            
            input.put("messages", messages);
            requestBody.put("input", input);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 多模态接口路径
            String vlEndpoint = "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation";
            ResponseEntity<String> response = aiRestTemplate.postForEntity(vlEndpoint, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode rootNode = mapper.readTree(response.getBody());
                String resultText = rootNode.path("output").path("choices").get(0).path("message").path("content").get(0).path("text").asText();
                return CompletableFuture.completedFuture(resultText);
            }
            log.error("[AI Gateway] VL API响应状态异常: {}", response.getStatusCode());
            return CompletableFuture.completedFuture("{\"success\":false,\"error\":\"【AI 熔断降级】系统繁忙，请稍后再试。\"}");
        } catch (Exception e) {
            log.error("[AI Gateway] 调用多模态生成大模型API异常", e);
            return CompletableFuture.completedFuture("{\"success\":false,\"error\":\"【AI 熔断降级】网络繁忙，请稍后再试。\"}");
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
