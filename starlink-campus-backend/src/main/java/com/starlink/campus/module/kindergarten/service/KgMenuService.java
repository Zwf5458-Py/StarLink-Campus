package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgMenu;
import java.util.List;

public interface KgMenuService extends IService<KgMenu> {
    List<String> getPermsByStaffId(Long staffId);
}
