package com.starlink.campus.common.utils;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class MockWechatSecurityUtil {
    
    // 模拟微信敏感词库
    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
        "暴力", "色情", "恐怖", "涉政", "反动", "赌博", "毒品"
    );

    /**
     * 模拟微信 msgSecCheck 文本安全校验
     * @param content 需要校验的文本内容
     * @return 如果安全返回 true，包含敏感词返回 false
     */
    public boolean checkTextSecurity(String content) {
        if (content == null || content.trim().isEmpty()) {
            return true;
        }
        for (String word : SENSITIVE_WORDS) {
            if (content.contains(word)) {
                System.out.println("[Mock Wechat Security] 文本内容包含敏感词: " + word);
                return false;
            }
        }
        return true;
    }

    /**
     * 模拟微信 mediaCheckAsync 图片/音频安全校验
     * 本地实现：仅检查 URL 中是否包含特定黑名单关键字
     * @param mediaUrl 需要校验的媒体URL
     * @return 如果安全返回 true，包含违规信息返回 false
     */
    public boolean checkMediaSecurity(String mediaUrl) {
        if (mediaUrl == null || mediaUrl.trim().isEmpty()) {
            return true;
        }
        if (mediaUrl.contains("illegal") || mediaUrl.contains("porn") || mediaUrl.contains("violence")) {
            System.out.println("[Mock Wechat Security] 媒体文件涉嫌违规: " + mediaUrl);
            return false;
        }
        return true;
    }
}
