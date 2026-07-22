package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("kg_role_menu")
public class KgRoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long menuId;

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }

    public Long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }
}
