package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.service.StudentAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

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
}
