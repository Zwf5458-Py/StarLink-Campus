package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgDevelopmentAssessment;
import com.starlink.campus.module.kindergarten.service.DevelopmentAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/assessment")
@Tag(name = "3-6岁儿童发展评估")
@CrossOrigin
public class AssessmentController {

    @Autowired
    private DevelopmentAssessmentService assessmentService;

    @Operation(summary = "提交幼儿学期评估")
    @SaCheckRole(value = {"TEACHER", "ADMIN"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/save")
    public R<KgDevelopmentAssessment> saveAssessment(@Valid @RequestBody KgDevelopmentAssessment assessment) {
        return R.ok(assessmentService.saveAssessment(assessment));
    }

    @Operation(summary = "查询幼儿历史评估列表")
    @GetMapping("/list/{studentId}")
    public R<List<KgDevelopmentAssessment>> listAssessments(@PathVariable Long studentId) {
        return R.ok(assessmentService.listStudentAssessments(studentId));
    }
}
