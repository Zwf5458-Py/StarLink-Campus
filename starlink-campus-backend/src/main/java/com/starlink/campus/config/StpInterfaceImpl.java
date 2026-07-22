package com.starlink.campus.config;

import cn.dev33.satoken.stp.StpInterface;
import com.starlink.campus.module.kindergarten.service.KgMenuService;
import com.starlink.campus.module.kindergarten.service.KgRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private KgRoleService roleService;

    @Autowired
    private KgMenuService menuService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (loginId == null) {
            return Collections.emptyList();
        }
        Long staffId = Long.valueOf(loginId.toString());
        return menuService.getPermsByStaffId(staffId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if (loginId == null) {
            return Collections.emptyList();
        }
        Long staffId = Long.valueOf(loginId.toString());
        return roleService.getRoleKeysByStaffId(staffId);
    }
}
