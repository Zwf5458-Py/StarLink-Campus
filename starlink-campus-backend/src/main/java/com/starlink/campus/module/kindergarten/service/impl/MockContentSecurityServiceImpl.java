package com.starlink.campus.module.kindergarten.service.impl;

import com.starlink.campus.module.kindergarten.service.ContentSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 内容安全审核 Mock 实现
 * 本地开发/测试环境使用，生产环境应替换为真实服务实现
 *
 * 替换步骤：
 * 1. 创建 WechatContentSecurityServiceImpl 实现 ContentSecurityService 接口
 * 2. 使用 @Profile("prod") 标注生产实现
 * 3. 本 Mock 类添加 @Profile("!prod") 条件注解
 */
@Service
public class MockContentSecurityServiceImpl implements ContentSecurityService {

    private static final Logger log = LoggerFactory.getLogger(MockContentSecurityServiceImpl.class);
    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
        "暴力", "色情", "恐怖", "涉政", "反动", "赌博", "毒品"
    );

    @Override
    public boolean checkTextSecurity(String content) {
        if (content == null || content.trim().isEmpty()) {
            return true;
        }
        for (String word : SENSITIVE_WORDS) {
            if (content.contains(word)) {
                log.warn("[Mock 内容安全] 文本包含敏感词: {}", word);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkMediaSecurity(String mediaUrl) {
        if (mediaUrl == null || mediaUrl.trim().isEmpty()) {
            return true;
        }
        if (mediaUrl.contains("illegal") || mediaUrl.contains("porn") || mediaUrl.contains("violence")) {
            log.warn("[Mock 内容安全] 媒体文件涉嫌违规: {}", mediaUrl);
            return false;
        }
        return true;
    }
}
