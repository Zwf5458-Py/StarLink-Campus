package com.starlink.campus.module.kindergarten.service;

public interface WxMessageService {
    boolean sendHealthAlertNotice(String openId, String studentName, String temp, String timeStr);
    boolean sendOaApprovalNotice(String openId, String applicantName, String leaveType, String status);
}
