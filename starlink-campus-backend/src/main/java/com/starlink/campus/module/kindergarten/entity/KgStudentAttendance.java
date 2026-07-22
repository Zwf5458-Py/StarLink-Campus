package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

@TableName("kg_student_attendance")
public class KgStudentAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long studentId;

    private Long classId;

    private Date attendanceDate;

    private Date checkInTime;

    private String checkInType; // FACE, CARD, MANUAL

    private Double checkInTemperature;

    private Date checkOutTime;

    private String checkOutType;

    private String status; // NORMAL, LATE, ABSENT, LEAVE

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    public KgStudentAttendance() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public Date getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(Date attendanceDate) { this.attendanceDate = attendanceDate; }

    public Date getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Date checkInTime) { this.checkInTime = checkInTime; }

    public String getCheckInType() { return checkInType; }
    public void setCheckInType(String checkInType) { this.checkInType = checkInType; }

    public Double getCheckInTemperature() { return checkInTemperature; }
    public void setCheckInTemperature(Double checkInTemperature) { this.checkInTemperature = checkInTemperature; }

    public Date getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Date checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getCheckOutType() { return checkOutType; }
    public void setCheckOutType(String checkOutType) { this.checkOutType = checkOutType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
