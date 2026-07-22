package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.entity.KgRepairOrder;
import com.starlink.campus.module.kindergarten.service.PatrolInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/patrol")
@CrossOrigin
public class KgPatrolController {

    @Autowired
    private PatrolInspectionService patrolInspectionService;

    @GetMapping("/list")
    public R<List<KgPatrolRecord>> getPatrolList() {
        return R.ok(patrolInspectionService.getPatrolList());
    }

    @PostMapping("/submit")
    public R<Boolean> submitPatrol(@Valid @RequestBody KgPatrolRecord record) {
        if (record.getId() != null) {
            // Update existing task
            KgPatrolRecord existing = patrolInspectionService.getById(record.getId());
            if (existing != null) {
                existing.setStatus("已完成");
                existing.setIsNormal(record.getIsNormal());
                existing.setAbnormalDesc(record.getAbnormalDesc());
                existing.setPhotoUrl(record.getPhotoUrl());
                patrolInspectionService.submitPatrol(existing);
            }
        } else {
            record.setStatus("已完成");
            patrolInspectionService.submitPatrol(record);
        }
        return R.ok(true);
    }

    @PostMapping("/tasks/generate")
    public R<Boolean> generateTasks(@RequestParam Long staffId) {
        return R.ok(patrolInspectionService.generateTasks(staffId));
    }

    @GetMapping("/repair-orders")
    public R<List<KgRepairOrder>> getRepairOrders() {
        return R.ok(patrolInspectionService.getRepairOrders());
    }
}
