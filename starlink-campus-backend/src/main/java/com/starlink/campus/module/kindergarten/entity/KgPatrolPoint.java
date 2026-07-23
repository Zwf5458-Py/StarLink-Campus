package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("kg_patrol_point")
public class KgPatrolPoint implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String pointName;      // 点位名称
    private String location;       // 位置描述
    private String deviceType;     // 设备类型(消防栓/摄像头/门禁等)
    private Integer sortOrder;     // 排序
    private Integer status;        // 1启用 0停用
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPointName() { return pointName; }
    public void setPointName(String pointName) { this.pointName = pointName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
