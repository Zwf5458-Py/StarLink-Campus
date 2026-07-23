package com.starlink.campus.module.kindergarten.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgGrowthRecord;
import com.starlink.campus.module.kindergarten.service.GrowthRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@Tag(name = "学生成长档案")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/growth")
@CrossOrigin
public class GrowthRecordController {

    @Autowired
    private GrowthRecordService growthRecordService;

    @Operation(summary = "获取学生成长记录列表")
    @GetMapping("/list")
    public R<Page<KgGrowthRecord>> list(
            @RequestParam Long studentId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return R.ok(growthRecordService.listByStudent(studentId, pageNum, pageSize));
    }

    @Operation(summary = "新增成长记录")
    @PostMapping("/add")
    public R<Boolean> add(@Valid @RequestBody KgGrowthRecord record) {
        growthRecordService.addRecord(record);
        return R.ok(true);
    }

    @Operation(summary = "更新成长记录")
    @PostMapping("/update")
    public R<Boolean> update(@Valid @RequestBody KgGrowthRecord record) {
        growthRecordService.updateRecord(record);
        return R.ok(true);
    }

    @Operation(summary = "删除成长记录")
    @DeleteMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        growthRecordService.deleteRecord(id);
        return R.ok(true);
    }

    @Operation(summary = "获取成长趋势曲线")
    @GetMapping("/trend")
    public R<List<Map<String, Object>>> trend(
            @RequestParam Long studentId,
            @RequestParam String category) {
        return R.ok(growthRecordService.getGrowthTrend(studentId, category));
    }

    @Operation(summary = "生成学期发展报告")
    @GetMapping("/semester-report")
    public R<Map<String, Object>> semesterReport(
            @RequestParam Long studentId,
            @RequestParam String semester) {
        return R.ok(growthRecordService.generateSemesterReport(studentId, semester));
    }
}
