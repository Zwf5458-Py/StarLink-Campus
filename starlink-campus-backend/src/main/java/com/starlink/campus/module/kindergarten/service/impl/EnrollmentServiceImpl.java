package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgEnrollment;
import com.starlink.campus.module.kindergarten.mapper.KgEnrollmentMapper;
import com.starlink.campus.module.kindergarten.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<KgEnrollmentMapper, KgEnrollment> implements EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    @Override
    public Page<KgEnrollment> list(Integer pageNum, Integer pageSize, String status) {
        LambdaQueryWrapper<KgEnrollment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(status), KgEnrollment::getStatus, status)
                .orderByDesc(KgEnrollment::getCreateTime);
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(KgEnrollment enrollment) {
        enrollment.setCreateTime(LocalDateTime.now());
        if (StringUtils.isBlank(enrollment.getStatus())) {
            enrollment.setStatus("意向");
        }
        return save(enrollment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KgEnrollment enrollment) {
        return updateById(enrollment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, String newStatus) {
        KgEnrollment enrollment = new KgEnrollment();
        enrollment.setId(id);
        enrollment.setStatus(newStatus);
        return updateById(enrollment);
    }

    @Override
    public Map<String, Object> getFunnelStats() {
        List<KgEnrollment> all = list();
        long total = all.size();
        
        Map<String, Long> statusCount = all.stream()
                .collect(Collectors.groupingBy(e -> e.getStatus() == null ? "未知" : e.getStatus(), Collectors.counting()));
        
        long intent = statusCount.getOrDefault("意向", 0L);
        long enrolled = statusCount.getOrDefault("已报名", 0L);
        long interviewing = statusCount.getOrDefault("面试中", 0L);
        long admitted = statusCount.getOrDefault("已录取", 0L);
        long abandoned = statusCount.getOrDefault("已放弃", 0L);
        
        double conversionRate = total == 0 ? 0.0 : ((double) admitted / total) * 100;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("意向", intent);
        stats.put("已报名", enrolled);
        stats.put("面试中", interviewing);
        stats.put("已录取", admitted);
        stats.put("已放弃", abandoned);
        stats.put("conversionRate", conversionRate);
        
        return stats;
    }
}
