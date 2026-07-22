package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("kg_patrol_record")
public class KgPatrolRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String patrolPointName;
    private Long patrolStaffId;
    private LocalDateTime patrolTime;
    private String photoUrl;
    private String watermarkInfo;
    private Integer isNormal;
    private String abnormalDesc;
    private LocalDateTime createTime;
    private String status;

    public KgPatrolRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatrolPointName() { return patrolPointName; }
    public void setPatrolPointName(String patrolPointName) { this.patrolPointName = patrolPointName; }

    public Long getPatrolStaffId() { return patrolStaffId; }
    public void setPatrolStaffId(Long patrolStaffId) { this.patrolStaffId = patrolStaffId; }

    public LocalDateTime getPatrolTime() { return patrolTime; }
    public void setPatrolTime(LocalDateTime patrolTime) { this.patrolTime = patrolTime; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getWatermarkInfo() { return watermarkInfo; }
    public void setWatermarkInfo(String watermarkInfo) { this.watermarkInfo = watermarkInfo; }

    public Integer getIsNormal() { return isNormal; }
    public void setIsNormal(Integer isNormal) { this.isNormal = isNormal; }

    public String getAbnormalDesc() { return abnormalDesc; }
    public void setAbnormalDesc(String abnormalDesc) { this.abnormalDesc = abnormalDesc; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
