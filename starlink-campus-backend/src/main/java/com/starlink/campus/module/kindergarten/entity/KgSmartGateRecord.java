package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("kg_smart_gate_record")
public class KgSmartGateRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String personType;
    private Long personId;
    private String gateNo;
    private String passDirection;
    private String passMethod;
    private String captureImage;
    private java.time.LocalDateTime passTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPersonType() {
        return personType;
    }
    public void setPersonType(String personType) {
        this.personType = personType;
    }
    public Long getPersonId() {
        return personId;
    }
    public void setPersonId(Long personId) {
        this.personId = personId;
    }
    public String getGateNo() {
        return gateNo;
    }
    public void setGateNo(String gateNo) {
        this.gateNo = gateNo;
    }
    public String getPassDirection() {
        return passDirection;
    }
    public void setPassDirection(String passDirection) {
        this.passDirection = passDirection;
    }
    public String getPassMethod() {
        return passMethod;
    }
    public void setPassMethod(String passMethod) {
        this.passMethod = passMethod;
    }
    public String getCaptureImage() {
        return captureImage;
    }
    public void setCaptureImage(String captureImage) {
        this.captureImage = captureImage;
    }
    public java.time.LocalDateTime getPassTime() {
        return passTime;
    }
    public void setPassTime(java.time.LocalDateTime passTime) {
        this.passTime = passTime;
    }
}
