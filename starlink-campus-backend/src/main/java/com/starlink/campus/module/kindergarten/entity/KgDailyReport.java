package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("kg_daily_report")
public class KgDailyReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private LocalDate reportDate;
    private String attendanceStatus;
    private BigDecimal morningTemp;
    private String mealSummary;
    private String napSummary;
    private String performanceHighlights;
    private String teacherComments;
    private Boolean isReadByParent;
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public BigDecimal getMorningTemp() {
        return morningTemp;
    }

    public void setMorningTemp(BigDecimal morningTemp) {
        this.morningTemp = morningTemp;
    }

    public String getMealSummary() {
        return mealSummary;
    }

    public void setMealSummary(String mealSummary) {
        this.mealSummary = mealSummary;
    }

    public String getNapSummary() {
        return napSummary;
    }

    public void setNapSummary(String napSummary) {
        this.napSummary = napSummary;
    }

    public String getPerformanceHighlights() {
        return performanceHighlights;
    }

    public void setPerformanceHighlights(String performanceHighlights) {
        this.performanceHighlights = performanceHighlights;
    }

    public String getTeacherComments() {
        return teacherComments;
    }

    public void setTeacherComments(String teacherComments) {
        this.teacherComments = teacherComments;
    }

    public Boolean getIsReadByParent() {
        return isReadByParent;
    }

    public void setIsReadByParent(Boolean isReadByParent) {
        this.isReadByParent = isReadByParent;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
