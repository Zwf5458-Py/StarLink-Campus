package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("kg_graduate_record")
public class KgGraduateRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private Integer graduationYear;
    private String primarySchoolName;
    private String futureDirection;
    private String contactInfo;
    private java.time.LocalDateTime createTime;

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
    public Integer getGraduationYear() {
        return graduationYear;
    }
    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }
    public String getPrimarySchoolName() {
        return primarySchoolName;
    }
    public void setPrimarySchoolName(String primarySchoolName) {
        this.primarySchoolName = primarySchoolName;
    }
    public String getFutureDirection() {
        return futureDirection;
    }
    public void setFutureDirection(String futureDirection) {
        this.futureDirection = futureDirection;
    }
    public String getContactInfo() {
        return contactInfo;
    }
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
