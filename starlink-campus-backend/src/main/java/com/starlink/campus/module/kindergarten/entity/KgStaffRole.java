package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("kg_staff_role")
public class KgStaffRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long staffId;
    private Long roleId;

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
}
