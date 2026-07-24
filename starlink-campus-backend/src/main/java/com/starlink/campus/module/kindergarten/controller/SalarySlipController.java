package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgSalarySlip;
import com.starlink.campus.module.kindergarten.service.SalarySlipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/salary")
@CrossOrigin
@Tag(name = "教职工薪酬查询")
public class SalarySlipController {

    private static final Logger log = LoggerFactory.getLogger(SalarySlipController.class);

    @Autowired
    private SalarySlipService salarySlipService;

    @GetMapping("/my-list")
    @Operation(summary = "仅本人查看薪酬条列表")
    public R<Page<KgSalarySlip>> myList(@RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        long staffId = StpUtil.getLoginIdAsLong();
        return R.ok(salarySlipService.listByStaff(staffId, pageNum, pageSize));
    }

    @GetMapping("/admin/list")
    @Operation(summary = "管理端薪酬条列表")
    public R<Page<KgSalarySlip>> listAll(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok(salarySlipService.listAll(pageNum, pageSize));
    }

    @PostMapping("/add")
    @Operation(summary = "新增薪酬条")
    public R<Boolean> addSlip(@Valid @RequestBody KgSalarySlip slip) {
        return R.ok(salarySlipService.addSlip(slip));
    }

    @PutMapping("/publish/{id}")
    @Operation(summary = "发布薪酬条")
    public R<Boolean> publishSlip(@PathVariable Long id) {
        return R.ok(salarySlipService.publishSlip(id));
    }

    @PutMapping("/batch-publish")
    @Operation(summary = "批量发布薪酬条")
    public R<Boolean> batchPublish(@Valid @RequestBody List<Long> ids) {
        return R.ok(salarySlipService.batchPublish(ids));
    }
}
