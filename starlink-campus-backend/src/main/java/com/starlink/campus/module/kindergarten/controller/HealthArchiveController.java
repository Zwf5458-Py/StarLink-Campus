package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgPhysicalExam;
import com.starlink.campus.module.kindergarten.service.HealthArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/health/archive")
@Tag(name = "幼儿体检档案")
@CrossOrigin
public class HealthArchiveController {

    @Autowired
    private HealthArchiveService archiveService;

    @Operation(summary = "录入体检记录")
    @SaCheckRole(value = {"DOCTOR", "ADMIN"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/save")
    public R<Boolean> saveExam(@RequestBody KgPhysicalExam exam) {
        return R.ok(archiveService.savePhysicalExam(exam));
    }

    @Operation(summary = "查询幼儿体检记录列表")
    @GetMapping("/list/{studentId}")
    public R<List<KgPhysicalExam>> listExams(@PathVariable Long studentId) {
        return R.ok(archiveService.listStudentExams(studentId));
    }
}
