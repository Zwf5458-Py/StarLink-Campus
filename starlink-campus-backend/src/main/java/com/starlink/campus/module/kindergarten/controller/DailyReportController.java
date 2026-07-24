package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgDailyReport;
import com.starlink.campus.module.kindergarten.service.DailyReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/daily-report")
@Tag(name = "每日报告", description = "家园共育聚合报告")
@CrossOrigin
public class DailyReportController {

    @Autowired
    private DailyReportService dailyReportService;

    @Operation(summary = "查询家长名下的幼儿每日报告")
    @GetMapping("/list/{studentId}")
    public R<List<KgDailyReport>> listReports(@PathVariable Long studentId) {
        return R.ok(dailyReportService.listParentReports(studentId));
    }

    @Operation(summary = "家长标记报告已读")
    @PutMapping("/read/{reportId}")
    public R<Boolean> markAsRead(@PathVariable Long reportId) {
        return R.ok(dailyReportService.markAsRead(reportId));
    }
}
