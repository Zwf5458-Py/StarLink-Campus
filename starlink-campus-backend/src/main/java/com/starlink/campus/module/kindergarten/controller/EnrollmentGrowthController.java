package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgOpenDayEvent;
import com.starlink.campus.module.kindergarten.entity.KgReferralRecord;
import com.starlink.campus.module.kindergarten.service.EnrollmentGrowthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/enrollment-growth")
@Tag(name = "招生裂变与活动管理")
@CrossOrigin
public class EnrollmentGrowthController {

    @Autowired
    private EnrollmentGrowthService growthService;

    @Operation(summary = "发布开放日活动")
    @SaCheckRole("ADMIN")
    @PostMapping("/event")
    public R<KgOpenDayEvent> createEvent(@RequestBody KgOpenDayEvent event) {
        return R.ok(growthService.createEvent(event));
    }

    @Operation(summary = "开放日报名占位")
    @PostMapping("/event/enroll/{eventId}")
    public R<Boolean> enrollInEvent(@PathVariable Long eventId) {
        return R.ok(growthService.enrollInEvent(eventId));
    }

    @Operation(summary = "查询开放日活动大厅")
    @GetMapping("/event/list")
    public R<List<KgOpenDayEvent>> listEvents() {
        return R.ok(growthService.listEvents());
    }

    @Operation(summary = "家长提交老带新推荐信息")
    @PostMapping("/referral")
    public R<KgReferralRecord> submitReferral(@RequestBody KgReferralRecord record) {
        return R.ok(growthService.addReferral(record));
    }

    @Operation(summary = "管理端更新推荐人转化进度")
    @SaCheckRole("ADMIN")
    @PutMapping("/referral/{recordId}/status")
    public R<Boolean> updateReferralStatus(@PathVariable Long recordId, @RequestParam String status) {
        return R.ok(growthService.updateReferralStatus(recordId, status));
    }

    @Operation(summary = "查询转介绍排行榜与记录")
    @GetMapping("/referral/list")
    public R<List<KgReferralRecord>> listReferrals() {
        return R.ok(growthService.listReferrals());
    }
}
