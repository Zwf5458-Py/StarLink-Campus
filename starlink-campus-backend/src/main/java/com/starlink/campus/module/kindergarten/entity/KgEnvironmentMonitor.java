package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("kg_environment_monitor")
public class KgEnvironmentMonitor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long classId;
    private java.math.BigDecimal temperature;
    private java.math.BigDecimal humidity;
    private Integer co2Level;
    private Integer pm25Level;
    private Integer warningTriggered;
    private java.time.LocalDateTime recordTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getClassId() {
        return classId;
    }
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    public java.math.BigDecimal getTemperature() {
        return temperature;
    }
    public void setTemperature(java.math.BigDecimal temperature) {
        this.temperature = temperature;
    }
    public java.math.BigDecimal getHumidity() {
        return humidity;
    }
    public void setHumidity(java.math.BigDecimal humidity) {
        this.humidity = humidity;
    }
    public Integer getCo2Level() {
        return co2Level;
    }
    public void setCo2Level(Integer co2Level) {
        this.co2Level = co2Level;
    }
    public Integer getPm25Level() {
        return pm25Level;
    }
    public void setPm25Level(Integer pm25Level) {
        this.pm25Level = pm25Level;
    }
    public Integer getWarningTriggered() {
        return warningTriggered;
    }
    public void setWarningTriggered(Integer warningTriggered) {
        this.warningTriggered = warningTriggered;
    }
    public java.time.LocalDateTime getRecordTime() {
        return recordTime;
    }
    public void setRecordTime(java.time.LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }
}
