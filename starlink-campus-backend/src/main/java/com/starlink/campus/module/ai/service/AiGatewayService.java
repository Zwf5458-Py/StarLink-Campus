package com.starlink.campus.module.ai.service;

import java.util.concurrent.CompletableFuture;

/**
 * 统一 AI 网关服务接口
 * 负责与外部大模型（通义千问、百度AI等）通信
 */
public interface AiGatewayService {
    
    /**
     * 异步文本生成 (例如：评语生成、周计划生成)
     * @param prompt 组装好的 prompt 字符串
     * @return 异步返回生成的文本
     */
    CompletableFuture<String> generateTextAsync(String prompt);

    /**
     * 异步语义级别文本安全审核
     * @param text 待审核的文本
     * @return 异步返回布尔值 (true: 安全, false: 违规)
     */
    CompletableFuture<Boolean> checkTextSecurityAsync(String text);

    /**
     * 异步多模态图像/视频安全审核
     * @param mediaUrl 媒体资源URL
     * @return 异步返回布尔值 (true: 安全, false: 违规)
     */
    CompletableFuture<Boolean> checkMediaSecurityAsync(String mediaUrl);
}
