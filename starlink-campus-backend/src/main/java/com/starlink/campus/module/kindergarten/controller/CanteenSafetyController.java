package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgFoodSample;
import com.starlink.campus.module.kindergarten.entity.KgFoodSupplier;
import com.starlink.campus.module.kindergarten.service.CanteenSafetyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/canteen/safety")
@Tag(name = "食堂食品安全与留样溯源")
@CrossOrigin
public class CanteenSafetyController {

    @Autowired
    private CanteenSafetyService canteenService;

    @Operation(summary = "新增/更新供应商档案")
    @SaCheckRole(value = {"ADMIN", "LOGISTICS"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/supplier")
    public R<KgFoodSupplier> saveSupplier(@Valid @RequestBody KgFoodSupplier supplier) {
        return R.ok(canteenService.saveSupplier(supplier));
    }

    @Operation(summary = "录入食品48小时留样")
    @SaCheckRole(value = {"ADMIN", "LOGISTICS"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/sample")
    public R<KgFoodSample> recordSample(@Valid @RequestBody KgFoodSample sample) {
        return R.ok(canteenService.recordFoodSample(sample));
    }

    @Operation(summary = "销毁留样")
    @SaCheckRole(value = {"ADMIN", "LOGISTICS"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PutMapping("/sample/destroy/{sampleId}")
    public R<Boolean> destroySample(@PathVariable Long sampleId, @RequestParam Long destroyerId) {
        return R.ok(canteenService.destroyFoodSample(sampleId, destroyerId));
    }

    @Operation(summary = "查询48小时内即将到期的留样预警")
    @SaCheckRole(value = {"ADMIN", "LOGISTICS"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @GetMapping("/sample/warning")
    public R<List<KgFoodSample>> listWarningSamples() {
        return R.ok(canteenService.listWarningSamples());
    }
}
