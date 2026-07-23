package com.starlink.campus.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxMpConfiguration {

    @Bean
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        // 预留真实配置占位，目前可以由系统设置读取或写死 mock
        config.setAppId("wx_dummy_appid_for_demo");
        config.setSecret("dummy_secret");
        config.setToken("dummy_token");
        config.setAesKey("dummy_aes_key");

        WxMpService service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(config);
        return service;
    }
}
