package com.starlink.campus.module.kindergarten.dto;

import java.io.Serializable;
import java.util.List;

public class RolePermissionSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private List<Long> checkedMenuIds;
    private List<String> actionPerms;

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }

    public List<Long> getCheckedMenuIds() { return checkedMenuIds; }
    public void setCheckedMenuIds(List<Long> checkedMenuIds) { this.checkedMenuIds = checkedMenuIds; }

    public List<String> getActionPerms() { return actionPerms; }
    public void setActionPerms(List<String> actionPerms) { this.actionPerms = actionPerms; }
}
