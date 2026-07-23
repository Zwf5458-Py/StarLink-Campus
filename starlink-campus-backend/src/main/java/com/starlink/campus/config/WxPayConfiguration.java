package com.starlink.campus.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxPayConfiguration {

    @Bean
    public WxPayService wxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId("wx_dummy_appid_for_demo");
        payConfig.setMchId("dummy_mch_id");
        payConfig.setMchKey("dummy_mch_key");
        payConfig.setKeyPath("classpath:apiclient_cert.p12");
        // 可以设置为真实沙箱环境或根据配置文件读取

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
