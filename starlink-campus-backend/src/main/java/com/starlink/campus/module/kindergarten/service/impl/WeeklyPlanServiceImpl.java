package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgWeeklyPlan;
import com.starlink.campus.module.kindergarten.mapper.KgWeeklyPlanMapper;
import com.starlink.campus.module.kindergarten.service.WeeklyPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WeeklyPlanServiceImpl extends ServiceImpl<KgWeeklyPlanMapper, KgWeeklyPlan> implements WeeklyPlanService {

    private static final Logger log = LoggerFactory.getLogger(WeeklyPlanServiceImpl.class);

    @Override
    public Page<KgWeeklyPlan> listByClass(Long classId, Integer pageNum, Integer pageSize) {
        Page<KgWeeklyPlan> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<KgWeeklyPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KgWeeklyPlan::getClassId, classId)
                .orderByDesc(KgWeeklyPlan::getWeekStartDate);
        return this.page(page, queryWrapper);
    }

    @Override
    public Page<KgWeeklyPlan> listAll(Integer pageNum, Integer pageSize) {
        Page<KgWeeklyPlan> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<KgWeeklyPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(KgWeeklyPlan::getWeekStartDate);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPlan(KgWeeklyPlan plan) {
        plan.setCreateTime(LocalDateTime.now());
        plan.setPublishStatus("草稿");
        return this.save(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePlan(KgWeeklyPlan plan) {
        return this.updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePlan(Long id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(Long id) {
        KgWeeklyPlan plan = new KgWeeklyPlan();
        plan.setId(id);
        plan.setPublishStatus("已发布");
        return this.updateById(plan);
    }
}
