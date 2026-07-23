package com.starlink.campus.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 轻量级接口限流拦截器，基于 Redis 滑动窗口计数器
 * 对高频接口 (check-in, like) 限制请求频率
 */
@Component
public class SaTokenRateLimitConfig implements HandlerInterceptor {

    /** 窗口大小：60秒 */
    private static final long WINDOW_MS = 60L;
    /** 每个IP+接口在窗口内最大请求数 */
    private static final int MAX_REQUESTS = 30;

    /** 限流路径列表 */
    private static final String[] RATE_LIMITED_PATHS = {
        "/kindergarten/attendance/check-in",
        "/kindergarten/attendance/check-out",
        "/kindergarten/circle/like",
        "/kindergarten/ai"
    };

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String uri = request.getRequestURI().replace("/api", "");
        boolean needLimit = false;
        for (String path : RATE_LIMITED_PATHS) {
            if (uri.startsWith(path)) {
                needLimit = true;
                break;
            }
        }
        if (!needLimit) return true;

        String clientIp = getClientIp(request);
        String key = "rate_limit:" + clientIp + ":" + uri;

        Long count = stringRedisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            stringRedisTemplate.expire(key, WINDOW_MS, TimeUnit.SECONDS);
        }

        if (count != null && count > MAX_REQUESTS) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":429,\"msg\":\"请求过于频繁，请稍后重试\",\"data\":null}");
            return false;
        }
        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 取第一个IP（如果经过代理）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
