package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgSchoolBus;
import com.starlink.campus.module.kindergarten.entity.KgBusRecord;
import com.starlink.campus.module.kindergarten.service.SchoolBusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/school-bus")
@Tag(name = "校车与上下车打卡管理")
@CrossOrigin
public class SchoolBusController {

    @Autowired
    private SchoolBusService busService;

    @Operation(summary = "新增/更新校车档案")
    @SaCheckRole("ADMIN")
    @PostMapping("/info")
    public R<KgSchoolBus> saveBus(@Valid @RequestBody KgSchoolBus bus) {
        return R.ok(busService.saveSchoolBus(bus));
    }

    @Operation(summary = "幼儿上下车刷卡记录")
    @SaCheckRole(value = {"DRIVER", "TEACHER", "ADMIN"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/record")
    public R<KgBusRecord> logRecord(@Valid @RequestBody KgBusRecord record) {
        return R.ok(busService.logBusRecord(record));
    }

    @Operation(summary = "查询幼儿历史乘车记录")
    @GetMapping("/record/list/{studentId}")
    public R<List<KgBusRecord>> listRecords(@PathVariable Long studentId) {
        return R.ok(busService.listStudentBusRecords(studentId));
    }
}
