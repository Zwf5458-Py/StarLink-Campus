package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgPickupPerson;
import com.starlink.campus.module.kindergarten.mapper.KgPickupPersonMapper;
import com.starlink.campus.module.kindergarten.service.PickupPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PickupPersonServiceImpl extends ServiceImpl<KgPickupPersonMapper, KgPickupPerson> implements PickupPersonService {

    private static final Logger log = LoggerFactory.getLogger(PickupPersonServiceImpl.class);

    @Override
    public Page<KgPickupPerson> listByStudent(Long studentId, Integer pageNum, Integer pageSize) {
        Page<KgPickupPerson> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        LambdaQueryWrapper<KgPickupPerson> queryWrapper = new LambdaQueryWrapper<>();
        if (studentId != null) {
            queryWrapper.eq(KgPickupPerson::getStudentId, studentId);
        }
        queryWrapper.orderByDesc(KgPickupPerson::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPickupPerson(KgPickupPerson person) {
        person.setStatus("待审核");
        person.setCreateTime(LocalDateTime.now());
        this.save(person);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approvePickupPerson(Long id) {
        KgPickupPerson person = this.getById(id);
        if (person != null && "待审核".equals(person.getStatus())) {
            person.setStatus("有效");
            this.updateById(person);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disablePickupPerson(Long id) {
        KgPickupPerson person = this.getById(id);
        if (person != null) {
            person.setStatus("停用");
            this.updateById(person);
        }
    }

    @Override
    public boolean verifyPickup(Long studentId, String identifier) {
        LambdaQueryWrapper<KgPickupPerson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KgPickupPerson::getStudentId, studentId)
                .eq(KgPickupPerson::getStatus, "有效")
                .and(qw -> qw.eq(KgPickupPerson::getIcCardNo, identifier).or().eq(KgPickupPerson::getFaceFeatureId, identifier));
        
        return this.count(queryWrapper) > 0;
    }

    @Override
    public void triggerUnauthorizedAlert(Long studentId, String unknownIdentifier) {
        log.warn("Unauthorized pickup attempt detected! Student ID: {}, Identifier: {}", studentId, unknownIdentifier);
    }

    @Override
    public List<KgPickupPerson> getActiveByStudent(Long studentId) {
        LambdaQueryWrapper<KgPickupPerson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KgPickupPerson::getStudentId, studentId)
                .eq(KgPickupPerson::getStatus, "有效");
        return this.list(queryWrapper);
    }
}
