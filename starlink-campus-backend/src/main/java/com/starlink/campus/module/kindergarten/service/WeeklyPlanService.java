package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgWeeklyPlan;

public interface WeeklyPlanService extends IService<KgWeeklyPlan> {
    Page<KgWeeklyPlan> listByClass(Long classId, Integer pageNum, Integer pageSize);
    Page<KgWeeklyPlan> listAll(Integer pageNum, Integer pageSize);
    boolean addPlan(KgWeeklyPlan plan);
    boolean updatePlan(KgWeeklyPlan plan);
    boolean deletePlan(Long id);
    boolean publish(Long id);
}
