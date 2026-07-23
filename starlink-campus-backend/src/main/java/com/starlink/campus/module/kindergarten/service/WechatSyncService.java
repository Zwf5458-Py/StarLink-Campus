package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgArticle;

/**
 * 微信公众号同步服务接口
 * 生产环境应替换为真实微信公众号 API 调用
 * 或接入消息队列 (RabbitMQ/Kafka) 异步处理
 */
public interface WechatSyncService {

    /**
     * 异步同步文章到微信公众号
     * @param article 待同步文章
     * @return true=同步任务已提交
     */
    boolean syncArticle(KgArticle article);
}
