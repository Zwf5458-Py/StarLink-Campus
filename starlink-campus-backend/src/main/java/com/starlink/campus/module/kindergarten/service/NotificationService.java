package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgNotification;

import java.util.Map;

public interface NotificationService extends IService<KgNotification> {
    void send(KgNotification notification);
    void markAsRead(Long id);
    Map<String, Object> getReadStats(Long notificationId);
    void sendWechatTemplateMessage(Long userId, String templateId, Map<String, String> data);
}
