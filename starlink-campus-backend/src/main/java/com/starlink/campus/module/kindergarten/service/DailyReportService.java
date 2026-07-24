package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgDailyReport;
import java.time.LocalDate;
import java.util.List;

public interface DailyReportService {
    void generateDailyReports(LocalDate date);
    List<KgDailyReport> listParentReports(Long studentId);
    boolean markAsRead(Long reportId);
}
