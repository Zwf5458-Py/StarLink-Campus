package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.entity.KgRepairOrder;
import com.starlink.campus.module.kindergarten.entity.KgPatrolPoint;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRoute;
import com.starlink.campus.module.kindergarten.service.PatrolInspectionService;
import com.starlink.campus.common.utils.ExcelExportUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Tag(name = "安防巡检管理")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/patrol")
@CrossOrigin
public class KgPatrolController {

    @Autowired
    private PatrolInspectionService patrolInspectionService;

    @Operation(summary = "获取巡检记录列表")
    @GetMapping("/list")
    public R<Page<KgPatrolRecord>> getPatrolList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return R.ok(patrolInspectionService.getPatrolList(pageNum, pageSize));
    }

    @Operation(summary = "提交巡检记录")
    @PostMapping("/submit")
    public R<Boolean> submitPatrol(@Valid @RequestBody KgPatrolRecord record) {
        if (record.getId() != null) {
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

    @Operation(summary = "生成巡检任务")
    @PostMapping("/tasks/generate")
    public R<Boolean> generateTasks(@RequestParam Long staffId) {
        return R.ok(patrolInspectionService.generateTasks(staffId));
    }

    @Operation(summary = "获取报修订单列表")
    @GetMapping("/repair-orders")
    public R<List<KgRepairOrder>> getRepairOrders() {
        return R.ok(patrolInspectionService.getRepairOrders());
    }

    @Operation(summary = "获取点位列表")
    @GetMapping("/points")
    public R<List<KgPatrolPoint>> listPoints() {
        return R.ok(patrolInspectionService.listPoints());
    }

    @Operation(summary = "新增点位")
    @PostMapping("/points/add")
    public R<Boolean> addPoint(@Valid @RequestBody KgPatrolPoint point) {
        return R.ok(patrolInspectionService.addPoint(point));
    }

    @Operation(summary = "更新点位")
    @PutMapping("/points/update")
    public R<Boolean> updatePoint(@Valid @RequestBody KgPatrolPoint point) {
        return R.ok(patrolInspectionService.updatePoint(point));
    }

    @Operation(summary = "删除点位")
    @DeleteMapping("/points/delete/{id}")
    public R<Boolean> deletePoint(@PathVariable Long id) {
        return R.ok(patrolInspectionService.deletePoint(id));
    }

    @Operation(summary = "获取路线列表")
    @GetMapping("/routes")
    public R<List<KgPatrolRoute>> listRoutes() {
        return R.ok(patrolInspectionService.listRoutes());
    }

    @Operation(summary = "新增路线")
    @PostMapping("/routes/add")
    public R<Boolean> addRoute(@Valid @RequestBody KgPatrolRoute route) {
        return R.ok(patrolInspectionService.addRoute(route));
    }

    @Operation(summary = "基于路线生成任务")
    @PostMapping("/tasks/generate-by-route")
    public R<Boolean> generateTasksByRoute(@RequestParam Long routeId, @RequestParam Long staffId) {
        return R.ok(patrolInspectionService.generateTasksByRoute(routeId, staffId));
    }

    @Operation(summary = "获取巡检统计报表")
    @GetMapping("/stats")
    public R<Map<String, Object>> getPatrolStats() {
        return R.ok(patrolInspectionService.getPatrolStats());
    }

    @Operation(summary = "导出巡检统计报表 Excel")
    @GetMapping("/export")
    public void exportPatrol(HttpServletResponse response) throws IOException {
        List<KgPatrolRecord> records = patrolInspectionService.getPatrolList();
        List<Map<String, Object>> data = new ArrayList<>();
        for (KgPatrolRecord r : records) {
            Map<String, Object> row = new HashMap<>();
            row.put("pointName", r.getPatrolPointName());
            row.put("status", r.getStatus());
            row.put("isNormal", r.getIsNormal() != null && r.getIsNormal() == 1 ? "正常" : "异常");
            row.put("abnormalDesc", r.getAbnormalDesc() != null ? r.getAbnormalDesc() : "");
            row.put("patrolTime", r.getPatrolTime() != null ? r.getPatrolTime().toString() : "");
            data.add(row);
        }
        ExcelExportUtil.export(data,
            List.of("pointName", "status", "isNormal", "abnormalDesc", "patrolTime"),
            List.of("巡更点位", "状态", "是否正常", "异常描述", "巡检时间"),
            "巡检报表", "巡检统计报表", response);
    }
}
