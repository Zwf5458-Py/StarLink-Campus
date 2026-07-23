package com.starlink.campus.module.kindergarten.dto;

import java.io.Serializable;
import java.util.List;

public class RoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String roleName;
    private String roleCode;
    private String description;
    private Integer userCount;
    private List<Long> checkedMenuIds;
    private List<String> actionPerms;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getUserCount() { return userCount; }
    public void setUserCount(Integer userCount) { this.userCount = userCount; }

    public List<Long> getCheckedMenuIds() { return checkedMenuIds; }
    public void setCheckedMenuIds(List<Long> checkedMenuIds) { this.checkedMenuIds = checkedMenuIds; }

    public List<String> getActionPerms() { return actionPerms; }
    public void setActionPerms(List<String> actionPerms) { this.actionPerms = actionPerms; }
}
