package com.starlink.campus.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 权限拦截配置类
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解鉴权功能
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 这里可以配置全局路由拦截，但目前我们主要依靠 @SaCheckLogin 注解
            // SaRouter.match("/**").notMatch("/login", "/swagger-ui/**", "/v3/api-docs/**").check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
}
