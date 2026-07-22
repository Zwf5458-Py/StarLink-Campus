package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgStudentAttendance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface StudentAttendanceService extends IService<KgStudentAttendance> {
    void checkIn(Long studentId, String type, BigDecimal temperature);
    void checkOut(Long studentId, String type);
    Map<String, Object> getClassSummary(Long classId, LocalDate date);
    BigDecimal calculateRefund(Long studentId, String month);
}
