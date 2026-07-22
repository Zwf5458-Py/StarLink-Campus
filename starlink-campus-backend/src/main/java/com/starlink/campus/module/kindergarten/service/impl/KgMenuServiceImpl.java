package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgMenu;
import com.starlink.campus.module.kindergarten.entity.KgRoleMenu;
import com.starlink.campus.module.kindergarten.entity.KgStaffRole;
import com.starlink.campus.module.kindergarten.mapper.KgMenuMapper;
import com.starlink.campus.module.kindergarten.mapper.KgRoleMenuMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStaffRoleMapper;
import com.starlink.campus.module.kindergarten.service.KgMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class KgMenuServiceImpl extends ServiceImpl<KgMenuMapper, KgMenu> implements KgMenuService {

    @Autowired
    private KgStaffRoleMapper staffRoleMapper;

    @Autowired
    private KgRoleMenuMapper roleMenuMapper;

    @Override
    public List<String> getPermsByStaffId(Long staffId) {
        if (staffId == null) {
            return Collections.emptyList();
        }

        List<KgStaffRole> staffRoles = staffRoleMapper.selectList(
                new QueryWrapper<KgStaffRole>().eq("staff_id", staffId)
        );

        if (staffRoles.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> roleIds = staffRoles.stream().map(KgStaffRole::getRoleId).collect(Collectors.toList());
        
        List<KgRoleMenu> roleMenus = roleMenuMapper.selectList(
                new QueryWrapper<KgRoleMenu>().in("role_id", roleIds)
        );
        
        if (roleMenus.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Long> menuIds = roleMenus.stream().map(KgRoleMenu::getMenuId).distinct().collect(Collectors.toList());
        List<KgMenu> menus = this.baseMapper.selectBatchIds(menuIds);
        
        return menus.stream()
                .map(KgMenu::getPerms)
                .filter(Objects::nonNull)
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.toList());
    }
}
