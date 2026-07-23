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
        Map<String, Map<String, Object>> counts = this.baseMapper.countByStatus();
        
        long intent = getCount(counts, "意向");
        long enrolled = getCount(counts, "已报名");
        long interviewing = getCount(counts, "面试中");
        long admitted = getCount(counts, "已录取");
        long abandoned = getCount(counts, "已放弃");
        
        long total = intent + enrolled + interviewing + admitted + abandoned;
        
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

    private long getCount(Map<String, Map<String, Object>> counts, String status) {
        if (counts.containsKey(status) && counts.get(status).get("count") != null) {
            return ((Number) counts.get(status).get("count")).longValue();
        }
        return 0L;
    }
}
