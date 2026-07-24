package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgOaApproval;
import com.starlink.campus.module.kindergarten.service.KgOaApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/oa")
@CrossOrigin
@Tag(name = "OA 审批模块", description = "教职工请假、采购等审批流程管理")
public class KgOaController {

    @Autowired
    private KgOaApprovalService oaService;

    @GetMapping("/list")
    @Operation(summary = "获取审批列表", description = "查询当前用户相关的 OA 审批单记录")
    public R<List<KgOaApproval>> list() {
        return R.ok(oaService.list());
    }

    @PostMapping("/submit")
    public R<Boolean> submit(@Valid @RequestBody KgOaApproval approval) {
        return R.ok(oaService.submit(approval));
    }

    @PostMapping("/approve/{id}")
    public R<Boolean> approve(@PathVariable Long id) {
        return R.ok(oaService.approve(id));
    }

    @PostMapping("/reject/{id}")
    public R<Boolean> reject(@PathVariable Long id) {
        return R.ok(oaService.reject(id));
    }
}
