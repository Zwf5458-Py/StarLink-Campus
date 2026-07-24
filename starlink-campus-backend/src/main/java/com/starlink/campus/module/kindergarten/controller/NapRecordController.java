package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgNapRecord;
import com.starlink.campus.module.kindergarten.service.NapRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/nap")
@Tag(name = "午睡管理")
@CrossOrigin
public class NapRecordController {

    @Autowired
    private NapRecordService napService;

    @Operation(summary = "录入午睡记录")
    @SaCheckRole(value = {"TEACHER", "ADMIN"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/save")
    public R<Boolean> saveRecord(@Valid @RequestBody KgNapRecord record) {
        return R.ok(napService.saveNapRecord(record));
    }

    @Operation(summary = "查询某日班级午睡情况")
    @GetMapping("/list/{date}")
    public R<List<KgNapRecord>> listByDate(@PathVariable LocalDate date, @RequestParam(required = false) Long classId) {
        return R.ok(napService.listNapRecordsByDate(date, classId));
    }
}
