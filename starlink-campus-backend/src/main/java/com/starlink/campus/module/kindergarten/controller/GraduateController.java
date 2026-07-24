package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgGraduateRecord;
import com.starlink.campus.module.kindergarten.service.GraduateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/graduate")
@Tag(name = "毕业生去向档案")
@CrossOrigin
public class GraduateController {

    @Autowired
    private GraduateService graduateService;

    @Operation(summary = "录入毕业生去向追踪档案")
    @SaCheckRole(value = {"TEACHER", "ADMIN"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/record")
    public R<KgGraduateRecord> saveRecord(@RequestBody KgGraduateRecord record) {
        return R.ok(graduateService.saveGraduateRecord(record));
    }

    @Operation(summary = "按年份查询毕业生去向统计")
    @GetMapping("/list")
    public R<List<KgGraduateRecord>> listRecords(@RequestParam(required = false) Integer year) {
        return R.ok(graduateService.listRecordsByYear(year));
    }
}
