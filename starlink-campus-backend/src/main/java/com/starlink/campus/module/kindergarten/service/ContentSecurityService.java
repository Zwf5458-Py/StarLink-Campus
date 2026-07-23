package com.starlink.campus.module.kindergarten.service;

/**
 * 内容安全审核服务接口
 * 生产环境应替换为微信 msg_sec_check / media_check_async
 * 或阿里云/网易易盾等第三方内容审核服务
 */
public interface ContentSecurityService {

    /**
     * 文本内容安全检查
     * @param content 待检文本
     * @return true=安全，false=包含违规内容
     */
    boolean checkTextSecurity(String content);

    /**
     * 媒体文件安全检查
     * @param mediaUrl 媒体文件URL
     * @return true=安全，false=包含违规内容
     */
    boolean checkMediaSecurity(String mediaUrl);
}
