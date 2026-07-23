package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("kg_patrol_route")
public class KgPatrolRoute implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String routeName;      // 路线名称
    private String pointIds;       // 点位ID列表,逗号分隔(如 "1,3,5,7")
    private String period;         // 周期: DAILY/WEEKLY
    private Integer status;        // 1启用 0停用
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    public String getPointIds() { return pointIds; }
    public void setPointIds(String pointIds) { this.pointIds = pointIds; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
