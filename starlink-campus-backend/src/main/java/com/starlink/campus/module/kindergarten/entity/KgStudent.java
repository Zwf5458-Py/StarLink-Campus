package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

@TableName("kg_student")
public class KgStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long classId;

    private String name;

    private Integer gender; // 1男, 2女

    private Date birthday;

    private String avatarUrl;

    private String icCardNo;

    private String faceFeatureId;

    private String allergies; // 过敏源

    private String guardianName;

    private String guardianPhone;

    private Integer status; // 1在读, 2毕业, 3退园

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    public KgStudent() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getIcCardNo() { return icCardNo; }
    public void setIcCardNo(String icCardNo) { this.icCardNo = icCardNo; }

    public String getFaceFeatureId() { return faceFeatureId; }
    public void setFaceFeatureId(String faceFeatureId) { this.faceFeatureId = faceFeatureId; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }

    public String getGuardianPhone() { return guardianPhone; }
    public void setGuardianPhone(String guardianPhone) { this.guardianPhone = guardianPhone; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
