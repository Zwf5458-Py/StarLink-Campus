package com.starlink.campus.module.kindergarten.service.impl;

import com.starlink.campus.module.kindergarten.service.WxMessageService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WxMessageServiceImpl implements WxMessageService {

    private static final Logger log = LoggerFactory.getLogger(WxMessageServiceImpl.class);

    @Autowired
    private WxMpService wxMpService;

    @Value("${wx.mp.template-id.health-alert:HEALTH_ALERT_TEMP_ID}")
    private String healthAlertTempId;

    @Value("${wx.mp.template-id.oa-approval:OA_APPROVAL_TEMP_ID}")
    private String oaApprovalTempId;

    @Override
    public boolean sendHealthAlertNotice(String openId, String studentName, String temp, String timeStr) {
        log.info("[微信生态] 尝试向openid={} 发送晨检异常模板消息, 学生={}, 体温={}", openId, studentName, temp);
        if (wxMpService == null) {
            log.warn("[微信生态] WxMpService 暂未配置，无法发送模板消息。");
            return false;
        }

        try {
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)
                    .templateId(healthAlertTempId)
                    .build();

            templateMessage.addData(new WxMpTemplateData("first", "尊敬的家长，检测到您家宝贝晨检体温异常！", "#FF0000"));
            templateMessage.addData(new WxMpTemplateData("keyword1", studentName, "#173177"));
            templateMessage.addData(new WxMpTemplateData("keyword2", temp + "℃", "#FF0000"));
            templateMessage.addData(new WxMpTemplateData("keyword3", timeStr, "#173177"));
            templateMessage.addData(new WxMpTemplateData("remark", "请配合园区保健医进行二次复测与留观处置。", "#666666"));

            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            return true;
        } catch (Exception e) {
            log.error("[微信生态] 发送晨检模板消息失败", e);
            return false;
        }
    }

    @Override
    public boolean sendOaApprovalNotice(String openId, String applicantName, String leaveType, String status) {
        log.info("[微信生态] 发送 OA 审批通知, 申请人={}, 状态={}", applicantName, status);
        if (wxMpService == null) {
            log.warn("[微信生态] WxMpService 暂未配置，无法发送模板消息。");
            return false;
        }

        try {
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)
                    .templateId(oaApprovalTempId)
                    .build();

            templateMessage.addData(new WxMpTemplateData("first", "您好，您提交的园务 OA 申请有了最新进展。"));
            templateMessage.addData(new WxMpTemplateData("keyword1", leaveType));
            templateMessage.addData(new WxMpTemplateData("keyword2", applicantName));
            templateMessage.addData(new WxMpTemplateData("keyword3", status));

            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            return true;
        } catch (Exception e) {
            log.error("[微信生态] 发送 OA 审批模板消息失败", e);
            return false;
        }
    }
}
