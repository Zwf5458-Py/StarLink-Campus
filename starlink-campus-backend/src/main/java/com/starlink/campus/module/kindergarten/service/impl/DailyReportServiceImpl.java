package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgDailyReport;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.mapper.KgDailyReportMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStudentMapper;
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

    @Autowired
    private KgDailyReportMapper reportMapper;

    @Autowired
    private KgStudentMapper studentMapper;

    @Override
    public void generateDailyReports(LocalDate date) {
        log.info("Generating daily reports for date: {}", date);
        List<KgStudent> students = studentMapper.selectList(new QueryWrapper<>());
        for (KgStudent student : students) {
            KgDailyReport report = new KgDailyReport();
            report.setStudentId(student.getId());
            report.setReportDate(date);
            report.setAttendanceStatus("PRESENT");
            report.setMorningTemp(new java.math.BigDecimal("36.5"));
            report.setMealSummary("营养早餐全吃完，午餐表现很棒！");
            report.setNapSummary("午休安稳，准时起床。");
            report.setPerformanceHighlights("积极参与科学探索，乐于分享。");
            report.setTeacherComments("宝贝今天在幼儿园度过了愉快的一天，继续保持喔！");
            report.setIsReadByParent(false);
            report.setCreateTime(java.time.LocalDateTime.now());
            reportMapper.insert(report);
        }
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
