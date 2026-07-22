package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("kg_class")
public class KgClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long campusId;
    private String gradeLevel;
    private String className;
    private Long headTeacherId;
    private Integer studentCount;
    private String roomNumber;
    private LocalDateTime createTime;

    public KgClass() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCampusId() { return campusId; }
    public void setCampusId(Long campusId) { this.campusId = campusId; }

    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Long getHeadTeacherId() { return headTeacherId; }
    public void setHeadTeacherId(Long headTeacherId) { this.headTeacherId = headTeacherId; }

    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
