package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.service.StudentAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import com.starlink.campus.common.utils.ExcelExportUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "幼儿考勤管理")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/attendance")
@CrossOrigin
public class KgAttendanceController {

    @Autowired
    private StudentAttendanceService attendanceService;

    @PostMapping("/check-in")
    public R<Boolean> checkIn(@RequestParam Long studentId,
                              @RequestParam(defaultValue = "FACE") String type,
                              @RequestParam(required = false) BigDecimal temperature) {
        attendanceService.checkIn(studentId, type, temperature);
        return R.ok(true);
    }

    @PostMapping("/check-out")
    public R<Boolean> checkOut(@RequestParam Long studentId,
                               @RequestParam(defaultValue = "FACE") String type) {
        attendanceService.checkOut(studentId, type);
        return R.ok(true);
    }

    @GetMapping("/class-summary")
    public R<Map<String, Object>> getClassSummary(@RequestParam Long classId,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(attendanceService.getClassSummary(classId, date));
    }

    @GetMapping("/calculate-refund/{studentId}")
    public R<BigDecimal> calculateRefund(@PathVariable Long studentId,
                                         @RequestParam String month) {
        return R.ok(attendanceService.calculateRefund(studentId, month));
    }

    @GetMapping("/export")
    @Operation(summary = "导出考勤报表 Excel")
    public void exportAttendance(@RequestParam Long classId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 HttpServletResponse response) throws IOException {
        Map<String, Object> summary = attendanceService.getClassSummary(classId, date);
        // 将 summary 转为可导出的数据格式
        List<Map<String, Object>> data = new ArrayList<>();
        // 如果 summary 中包含学生列表则直接使用，否则构造基础数据行
        Map<String, Object> row = new HashMap<>();
        row.put("date", date.toString());
        row.put("classId", classId);
        row.put("totalCount", summary.getOrDefault("totalCount", 0));
        row.put("presentCount", summary.getOrDefault("presentCount", 0));
        row.put("absentCount", summary.getOrDefault("absentCount", 0));
        data.add(row);
        
        ExcelExportUtil.export(data,
            List.of("date", "classId", "totalCount", "presentCount", "absentCount"),
            List.of("日期", "班级ID", "总人数", "出勤人数", "缺勤人数"),
            "考勤报表", "考勤报表_" + date, response);
    }
}
