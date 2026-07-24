package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgMedicationApplication;
import com.starlink.campus.module.kindergarten.entity.KgMedicationExecution;
import com.starlink.campus.module.kindergarten.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/medication")
@Tag(name = "喂药管理", description = "家长申请与保健医执行留痕")
@CrossOrigin
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    @Operation(summary = "家长提交喂药申请")
    @PostMapping("/apply")
    public R<KgMedicationApplication> submitApplication(@RequestBody KgMedicationApplication application) {
        return R.ok(medicationService.submitApplication(application));
    }

    @Operation(summary = "教师确认接收药品")
    @SaCheckRole(value = {"TEACHER", "ADMIN", "DOCTOR"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PutMapping("/accept/{id}")
    public R<Boolean> acceptApplication(@PathVariable Long id) {
        return R.ok(medicationService.acceptApplication(id));
    }

    @Operation(summary = "保健医执行喂药留痕")
    @SaCheckRole(value = {"DOCTOR", "ADMIN"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/execute")
    public R<KgMedicationExecution> executeMedication(@RequestBody KgMedicationExecution execution) {
        return R.ok(medicationService.executeMedication(execution));
    }

    @Operation(summary = "查询幼儿待处理申请")
    @GetMapping("/pending/{studentId}")
    public R<List<KgMedicationApplication>> getPending(@PathVariable Long studentId) {
        return R.ok(medicationService.listPendingApplications(studentId));
    }
}
