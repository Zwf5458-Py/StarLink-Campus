package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgMorningCheck;
import com.starlink.campus.module.kindergarten.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/health")
@CrossOrigin
public class KgHealthCheckController {

    @Autowired
    private HealthService healthService;

    @PostMapping("/morning-check")
    public R<Boolean> addMorningCheck(@Valid @RequestBody KgMorningCheck check) {
        healthService.addMorningCheck(check);
        return R.ok(true);
    }

    @GetMapping("/morning-summary")
    public R<Map<String, Object>> getMorningSummary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(healthService.getMorningSummary(date));
    }

    @PostMapping("/check-recipe")
    public R<List<String>> checkRecipeAllergies(@Valid @RequestBody Map<String, List<String>> body) {
        List<String> ingredients = body.get("ingredients");
        return R.ok(healthService.checkRecipeAllergies(ingredients));
    }
}
