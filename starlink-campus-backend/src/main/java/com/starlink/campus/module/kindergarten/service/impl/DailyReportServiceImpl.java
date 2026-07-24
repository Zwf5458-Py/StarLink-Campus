package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgDailyReport;
import com.starlink.campus.module.kindergarten.mapper.KgDailyReportMapper;
import com.starlink.campus.module.kindergarten.service.DailyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DailyReportServiceImpl implements DailyReportService {
    
    private static final Logger log = LoggerFactory.getLogger(DailyReportServiceImpl.class);

    @Autowired(required = false)
    private KgDailyReportMapper reportMapper;

    @Override
    public void generateDailyReports(LocalDate date) {
        log.info("Generating daily reports for date: {}", date);
        // This method would be triggered by a @Scheduled task to aggregate 
        // attendance, morning check, nap, and meals into kg_daily_report.
    }

    @Override
    public List<KgDailyReport> listParentReports(Long studentId) {
        return reportMapper.selectList(new QueryWrapper<KgDailyReport>()
                .eq("student_id", studentId)
                .orderByDesc("report_date"));
    }

    @Override
    public boolean markAsRead(Long reportId) {
        KgDailyReport report = reportMapper.selectById(reportId);
        if (report != null && !Boolean.TRUE.equals(report.getIsReadByParent())) {
            report.setIsReadByParent(true);
            return reportMapper.updateById(report) > 0;
        }
        return false;
    }
}
