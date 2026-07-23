package com.starlink.campus.module.kindergarten.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

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

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "success");
        res.put("data", configStore);
        return res;
    }

    @PutMapping
    public Map<String, Object> updateConfig(@RequestBody Map<String, Object> config) {
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
            configStore.putAll(config);
            try {
                stringRedisTemplate.opsForValue().set(CONFIG_KEY, objectMapper.writeValueAsString(configStore));
            } catch (Exception e) {
                // ignore
            }
        }
        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "success");
        res.put("data", configStore);
        return res;
    }
}
