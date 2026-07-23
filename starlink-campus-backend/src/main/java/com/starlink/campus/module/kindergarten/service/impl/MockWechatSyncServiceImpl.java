package com.starlink.campus.module.kindergarten.service.impl;

import com.starlink.campus.module.kindergarten.entity.KgArticle;
import com.starlink.campus.module.kindergarten.service.WechatSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 微信同步 Mock 实现
 * 使用 @Async 模拟异步消息队列处理
 *
 * 生产环境替换步骤：
 * 1. 创建 WechatApiSyncServiceImpl 实现 WechatSyncService
 * 2. 配置微信 AppID/AppSecret 并调用 Wechat API
 * 3. 或通过 RabbitMQ/Kafka 发送同步消息
 */
@Service
public class MockWechatSyncServiceImpl implements WechatSyncService {

    private static final Logger log = LoggerFactory.getLogger(MockWechatSyncServiceImpl.class);
    @Async
    @Override
    public boolean syncArticle(KgArticle article) {
        log.info("[Mock 微信同步] 异步同步文章到微信公众号: {} (ID: {})", article.getTitle(), article.getId());
        // 模拟网络延迟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[Mock 微信同步] 文章同步完成: {}", article.getTitle());
        return true;
    }
}
