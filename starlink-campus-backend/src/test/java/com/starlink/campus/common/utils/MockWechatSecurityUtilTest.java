package com.starlink.campus.common.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MockWechatSecurityUtilTest {

    private MockWechatSecurityUtil securityUtil;

    @BeforeEach
    public void setUp() {
        securityUtil = new MockWechatSecurityUtil();
    }

    @Test
    public void testCheckTextSecurity_ValidContent() {
        String validText = "小朋友们今天表现非常棒！";
        assertTrue(securityUtil.checkTextSecurity(validText), "Valid text should pass security check.");
    }

    @Test
    public void testCheckTextSecurity_InvalidContent() {
        String invalidText = "这里包含暴力和色情内容";
        assertFalse(securityUtil.checkTextSecurity(invalidText), "Text containing sensitive words should be rejected.");
    }

    @Test
    public void testCheckTextSecurity_NullOrEmpty() {
        assertTrue(securityUtil.checkTextSecurity(null), "Null text should pass.");
        assertTrue(securityUtil.checkTextSecurity("   "), "Empty text should pass.");
    }

    @Test
    public void testCheckMediaSecurity_ValidUrl() {
        String validUrl = "https://example.com/images/kids-playing.png";
        assertTrue(securityUtil.checkMediaSecurity(validUrl), "Valid media URL should pass.");
    }

    @Test
    public void testCheckMediaSecurity_InvalidUrl() {
        String invalidUrl = "https://example.com/images/porn_content.jpg";
        assertFalse(securityUtil.checkMediaSecurity(invalidUrl), "Media URL containing illegal words should be rejected.");
    }

    @Test
    public void testCheckMediaSecurity_NullOrEmpty() {
        assertTrue(securityUtil.checkMediaSecurity(null), "Null URL should pass.");
        assertTrue(securityUtil.checkMediaSecurity("   "), "Empty URL should pass.");
    }
}
