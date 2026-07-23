package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("kg_pickup_person")
public class KgPickupPerson implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private String name;
    private String relation;    // 父亲/母亲/祖父/祖母/外祖父/外祖母/保姆/其他
    private String phone;
    private String idCardNo;
    private String faceFeatureId;
    private String icCardNo;
    private Integer isPrimary;  // 1主要/0辅助
    private String photoUrl;
    private String status;      // 有效/待审核/停用
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 手写全部 getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRelation() { return relation; }
    public void setRelation(String relation) { this.relation = relation; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getIdCardNo() { return idCardNo; }
    public void setIdCardNo(String idCardNo) { this.idCardNo = idCardNo; }
    public String getFaceFeatureId() { return faceFeatureId; }
    public void setFaceFeatureId(String faceFeatureId) { this.faceFeatureId = faceFeatureId; }
    public String getIcCardNo() { return icCardNo; }
    public void setIcCardNo(String icCardNo) { this.icCardNo = icCardNo; }
    public Integer getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Integer isPrimary) { this.isPrimary = isPrimary; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
