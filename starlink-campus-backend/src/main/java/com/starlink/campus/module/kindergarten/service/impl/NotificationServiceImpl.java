package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgNotification;
import com.starlink.campus.module.kindergarten.mapper.KgNotificationMapper;
import com.starlink.campus.module.kindergarten.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl extends ServiceImpl<KgNotificationMapper, KgNotification> implements NotificationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(KgNotification notification) {
        notification.setCreateTime(LocalDateTime.now());
        notification.setIsRead(0);
        this.save(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id) {
        KgNotification notification = this.getById(id);
        if (notification != null && notification.getIsRead() == 0) {
            notification.setIsRead(1);
            notification.setReadTime(LocalDateTime.now());
            this.updateById(notification);
        }
    }

    @Override
    public Map<String, Object> getReadStats(Long notificationId) {
        KgNotification notification = this.getById(notificationId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("notificationId", notificationId);
        // 由于目前 KgNotification 是一对一记录，按单条统计
        stats.put("total", 1);
        stats.put("read", notification != null && notification.getIsRead() != null && notification.getIsRead() == 1 ? 1 : 0);
        stats.put("unread", notification != null && notification.getIsRead() != null && notification.getIsRead() == 1 ? 0 : 1);
        return stats;
    }

    @Override
    public void sendWechatTemplateMessage(Long userId, String templateId, Map<String, String> data) {
        // [Mock] 记录微信模板消息发送，因为没有实际的 appId 和 secret 配置
        // 实际开发中会调用类似 WxMaService.getMsgService().sendTemplateMsg()
        System.out.println("====== [微信模板消息推送 (Mock)] ======");
        System.out.println("用户ID: " + userId);
        System.out.println("模板ID: " + templateId);
        System.out.println("数据: " + data);
        System.out.println("======================================");
    }
}
