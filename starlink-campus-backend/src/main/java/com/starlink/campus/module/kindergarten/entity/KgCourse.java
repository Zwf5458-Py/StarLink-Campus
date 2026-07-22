package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("kg_course")
public class KgCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String courseName;
    private String campusName;
    private String targetGrades;
    private String teacherName;
    private LocalDateTime teacherSubmitTime;
    private LocalDateTime studentSelectTime;
    private String status;
    private LocalDateTime createTime;

    public KgCourse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCampusName() { return campusName; }
    public void setCampusName(String campusName) { this.campusName = campusName; }

    public String getTargetGrades() { return targetGrades; }
    public void setTargetGrades(String targetGrades) { this.targetGrades = targetGrades; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public LocalDateTime getTeacherSubmitTime() { return teacherSubmitTime; }
    public void setTeacherSubmitTime(LocalDateTime teacherSubmitTime) { this.teacherSubmitTime = teacherSubmitTime; }

    public LocalDateTime getStudentSelectTime() { return studentSelectTime; }
    public void setStudentSelectTime(LocalDateTime studentSelectTime) { this.studentSelectTime = studentSelectTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
