package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@SaCheckLogin
@RestController
@RequestMapping("/api/kindergarten/system/config")
public class SystemConfigController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String CONFIG_KEY = "system:config:latest";
    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> configStore = new HashMap<>();
        configStore.put("themeMode", "light");
        configStore.put("aiPlatform", "qwen");
        configStore.put("customApiUrl", "");
        configStore.put("customApiKey", "");
        configStore.put("feverTemp", 37.3);
        configStore.put("overtimeMinutes", 15);
        configStore.put("refundRate", 20);
        configStore.put("wsEnable", true);
        return configStore;
    }

    @GetMapping
    public Map<String, Object> getConfig() {
        Map<String, Object> configStore = getDefaultConfig();
        try {
            String cached = stringRedisTemplate.opsForValue().get(CONFIG_KEY);
            if (cached != null) {
                Map<String, Object> saved = objectMapper.readValue(cached, new TypeReference<Map<String, Object>>(){});
                configStore.putAll(saved);
            }
        } catch (Exception e) {
            // ignore
        }

        // 脱敏 customApiKey，防止前端暴露
        Map<String, Object> safeConfig = new HashMap<>(configStore);
        String apiKey = (String) safeConfig.get("customApiKey");
        if (apiKey != null && !apiKey.isEmpty()) {
            safeConfig.put("customApiKey", "********");
        }

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "success");
        res.put("data", safeConfig);
        return res;
    }

    @PutMapping
    @SaCheckRole(value = {"system_admin", "kg_principal"}, mode = SaMode.OR)
    public Map<String, Object> updateConfig(@Valid @RequestBody Map<String, Object> config) {
        Map<String, Object> configStore = getDefaultConfig();
        try {
            String cached = stringRedisTemplate.opsForValue().get(CONFIG_KEY);
            if (cached != null) {
                Map<String, Object> saved = objectMapper.readValue(cached, new TypeReference<Map<String, Object>>(){});
                configStore.putAll(saved);
            }
        } catch (Exception e) {
            // ignore
        }

        if (config != null) {
            // 如果前端传回脱敏数据，则保留原数据不被覆盖
            if ("********".equals(config.get("customApiKey"))) {
                config.remove("customApiKey");
            }
            configStore.putAll(config);
            try {
                stringRedisTemplate.opsForValue().set(CONFIG_KEY, objectMapper.writeValueAsString(configStore));
            } catch (Exception e) {
                // ignore
            }
        }
        
        Map<String, Object> safeConfig = new HashMap<>(configStore);
        String apiKey = (String) safeConfig.get("customApiKey");
        if (apiKey != null && !apiKey.isEmpty()) {
            safeConfig.put("customApiKey", "********");
        }

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "success");
        res.put("data", safeConfig);
        return res;
    }
}
