package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgRole;
import com.starlink.campus.module.kindergarten.entity.KgStaffRole;
import com.starlink.campus.module.kindergarten.mapper.KgRoleMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStaffRoleMapper;
import com.starlink.campus.module.kindergarten.service.KgRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KgRoleServiceImpl extends ServiceImpl<KgRoleMapper, KgRole> implements KgRoleService {

    @Autowired
    private KgStaffRoleMapper staffRoleMapper;

    @Override
    public List<String> getRoleKeysByStaffId(Long staffId) {
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
        List<KgRole> roles = this.baseMapper.selectBatchIds(roleIds);
        
        return roles.stream().map(KgRole::getRoleKey).collect(Collectors.toList());
    }
}
