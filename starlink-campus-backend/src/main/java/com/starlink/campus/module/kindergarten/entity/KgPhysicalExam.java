package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("kg_physical_exam")
public class KgPhysicalExam {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private String examType;
    private LocalDate examDate;
    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal eyesightLeft;
    private BigDecimal eyesightRight;
    private Integer dentalCaries;
    private BigDecimal hemoglobin;
    private String doctorConclusion;
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

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getEyesightLeft() {
        return eyesightLeft;
    }

    public void setEyesightLeft(BigDecimal eyesightLeft) {
        this.eyesightLeft = eyesightLeft;
    }

    public BigDecimal getEyesightRight() {
        return eyesightRight;
    }

    public void setEyesightRight(BigDecimal eyesightRight) {
        this.eyesightRight = eyesightRight;
    }

    public Integer getDentalCaries() {
        return dentalCaries;
    }

    public void setDentalCaries(Integer dentalCaries) {
        this.dentalCaries = dentalCaries;
    }

    public BigDecimal getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(BigDecimal hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public String getDoctorConclusion() {
        return doctorConclusion;
    }

    public void setDoctorConclusion(String doctorConclusion) {
        this.doctorConclusion = doctorConclusion;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
