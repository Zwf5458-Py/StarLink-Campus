package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgEnrollment;
import com.starlink.campus.module.kindergarten.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/enrollment")
@CrossOrigin
@Tag(name = "招生管理")
public class EnrollmentController {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/list")
    @Operation(summary = "招生列表")
    public R<Page<KgEnrollment>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(required = false) String status) {
        return R.ok(enrollmentService.list(pageNum, pageSize, status));
    }

    @PostMapping("/add")
    @Operation(summary = "新增招生记录")
    public R<Boolean> add(@Valid @RequestBody KgEnrollment enrollment) {
        return R.ok(enrollmentService.add(enrollment));
    }

    @PutMapping("/update")
    @Operation(summary = "更新招生记录")
    public R<Boolean> update(@Valid @RequestBody KgEnrollment enrollment) {
        return R.ok(enrollmentService.update(enrollment));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除招生记录")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.ok(enrollmentService.delete(id));
    }

    @PutMapping("/status/{id}")
    @Operation(summary = "更新招生状态")
    public R<Boolean> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return R.ok(enrollmentService.updateStatus(id, status));
    }

    @GetMapping("/funnel-stats")
    @Operation(summary = "招生漏斗统计")
    public R<Map<String, Object>> getFunnelStats() {
        return R.ok(enrollmentService.getFunnelStats());
    }
}
