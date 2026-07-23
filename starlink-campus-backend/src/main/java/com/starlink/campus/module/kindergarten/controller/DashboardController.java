package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 数智监管 8 大动态看板 Real-Data API
 */
@Tag(name = "数智监管看板")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/dashboard")
@CrossOrigin
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(summary = "获取8大看板实时统计数据")
    @GetMapping("/stats")
    public R<Map<String, Object>> getDashboardStats() {
        return R.ok(dashboardService.getDashboardStats());
    }
}
