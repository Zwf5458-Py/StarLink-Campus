package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("kg_bus_record")
public class KgBusRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long busId;
    private Long studentId;
    private String direction;
    private String actionType;
    private java.time.LocalDateTime actionTime;
    private String stationName;
    private java.time.LocalDateTime createTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getBusId() {
        return busId;
    }
    public void setBusId(Long busId) {
        this.busId = busId;
    }
    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getActionType() {
        return actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    public java.time.LocalDateTime getActionTime() {
        return actionTime;
    }
    public void setActionTime(java.time.LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }
    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
