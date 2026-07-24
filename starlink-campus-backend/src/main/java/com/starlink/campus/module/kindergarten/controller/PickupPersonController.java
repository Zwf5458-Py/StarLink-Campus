package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgPickupPerson;
import com.starlink.campus.module.kindergarten.service.PickupPersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "接送人管理")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/pickup")
@CrossOrigin
public class PickupPersonController {

    @Autowired
    private PickupPersonService pickupPersonService;

    @Operation(summary = "获取学生接送人列表")
    @GetMapping("/list")
    public R<Page<KgPickupPerson>> list(@RequestParam(required = false) Long studentId,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<KgPickupPerson> page = pickupPersonService.listByStudent(studentId, pageNum, pageSize);
        return R.ok(page);
    }

    @Operation(summary = "新增接送人")
    @PostMapping("/add")
    public R<Void> add(@Valid @RequestBody KgPickupPerson person) {
        pickupPersonService.addPickupPerson(person);
        return R.ok(null);
    }

    @Operation(summary = "审批接送人")
    @PostMapping("/approve/{id}")
    public R<Void> approve(@PathVariable Long id) {
        pickupPersonService.approvePickupPerson(id);
        return R.ok(null);
    }

    @Operation(summary = "停用接送人")
    @PostMapping("/disable/{id}")
    public R<Void> disable(@PathVariable Long id) {
        pickupPersonService.disablePickupPerson(id);
        return R.ok(null);
    }

    @Operation(summary = "校验接送人身份")
    @PostMapping("/verify")
    public R<Boolean> verify(@Valid @RequestBody Map<String, Object> params) {
        Long studentId = Long.valueOf(params.get("studentId").toString());
        String identifier = params.get("identifier").toString();
        boolean result = pickupPersonService.verifyPickup(studentId, identifier);
        if (!result) {
            pickupPersonService.triggerUnauthorizedAlert(studentId, identifier);
        }
        return R.ok(result);
    }

    @Operation(summary = "获取有效接送人")
    @GetMapping("/active")
    public R<List<KgPickupPerson>> getActive(@RequestParam Long studentId) {
        List<KgPickupPerson> list = pickupPersonService.getActiveByStudent(studentId);
        return R.ok(list);
    }
}
