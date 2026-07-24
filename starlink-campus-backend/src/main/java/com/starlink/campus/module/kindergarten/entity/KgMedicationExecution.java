package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("kg_medication_execution")
public class KgMedicationExecution {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long applicationId;
    private Long executorId;
    private BigDecimal studentTemperature;
    private String actualDosage;
    private LocalDateTime executionTime;
    private String evidenceImageUrl;
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public BigDecimal getStudentTemperature() {
        return studentTemperature;
    }

    public void setStudentTemperature(BigDecimal studentTemperature) {
        this.studentTemperature = studentTemperature;
    }

    public String getActualDosage() {
        return actualDosage;
    }

    public void setActualDosage(String actualDosage) {
        this.actualDosage = actualDosage;
    }

    public LocalDateTime getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(LocalDateTime executionTime) {
        this.executionTime = executionTime;
    }

    public String getEvidenceImageUrl() {
        return evidenceImageUrl;
    }

    public void setEvidenceImageUrl(String evidenceImageUrl) {
        this.evidenceImageUrl = evidenceImageUrl;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
