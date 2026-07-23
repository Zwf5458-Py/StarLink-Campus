package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgWeeklyMenu;

import java.time.LocalDate;

public interface WeeklyMenuService extends IService<KgWeeklyMenu> {
    Page<KgWeeklyMenu> listByWeek(LocalDate weekStart, Integer pageNum, Integer pageSize);
    boolean addMenu(KgWeeklyMenu menu);
    boolean updateMenu(KgWeeklyMenu menu);
    boolean deleteMenu(Long id);
    boolean publishMenu(Long id);
    String checkAllergen(Long menuId, Long classId);
}
