package com.starlink.campus.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("海星智联智慧校园 API 文档")
                        .version("1.0.0")
                        .description("包含了智慧班牌、移动端和管理后台的所有 RESTful 接口规范。")
                        .contact(new Contact().name("StarLink Team").email("support@starlink.com")));
    }
}
