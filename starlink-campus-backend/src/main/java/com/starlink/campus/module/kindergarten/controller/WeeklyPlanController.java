package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgWeeklyPlan;
import com.starlink.campus.module.kindergarten.service.WeeklyPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/plan")
@Tag(name = "教学周计划")
@CrossOrigin
public class WeeklyPlanController {

    private static final Logger log = LoggerFactory.getLogger(WeeklyPlanController.class);

    @Autowired
    private WeeklyPlanService weeklyPlanService;

    @GetMapping("/list")
    @Operation(summary = "获取周计划列表")
    public R<Page<KgWeeklyPlan>> list(@RequestParam(required = false) Long classId,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        if (classId != null) {
            return R.ok(weeklyPlanService.listByClass(classId, pageNum, pageSize));
        }
        return R.ok(weeklyPlanService.listAll(pageNum, pageSize));
    }

    @PostMapping("/add")
    @Operation(summary = "新增周计划")
    public R<Boolean> add(@Valid @RequestBody KgWeeklyPlan plan) {
        return R.ok(weeklyPlanService.addPlan(plan));
    }

    @PutMapping("/update")
    @Operation(summary = "更新周计划")
    public R<Boolean> update(@Valid @RequestBody KgWeeklyPlan plan) {
        return R.ok(weeklyPlanService.updatePlan(plan));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除周计划")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.ok(weeklyPlanService.deletePlan(id));
    }

    @PutMapping("/publish/{id}")
    @Operation(summary = "发布周计划")
    public R<Boolean> publish(@PathVariable Long id) {
        return R.ok(weeklyPlanService.publish(id));
    }
}
