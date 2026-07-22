package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgRole;
import java.util.List;

public interface KgRoleService extends IService<KgRole> {
    List<String> getRoleKeysByStaffId(Long staffId);
}
