package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.mapper.KgStudentMapper;
import com.starlink.campus.module.kindergarten.service.KgStudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

@Slf4j
@Service
public class KgStudentServiceImpl extends ServiceImpl<KgStudentMapper, KgStudent> implements KgStudentService {

    @Override
    public Page<KgStudent> list(Integer pageNum, Integer pageSize, String name, Long classId) {
        Page<KgStudent> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<KgStudent> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(KgStudent::getName, name);
        }
        if (classId != null) {
            wrapper.eq(KgStudent::getClassId, classId);
        }
        return this.page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(KgStudent student) {
        student.setCreateTime(new Date());
        if (student.getStatus() == null) {
            student.setStatus(1);
        }
        this.save(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KgStudent student) {
        this.updateById(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    public List<KgStudent> listByClassId(Long classId) {
        LambdaQueryWrapper<KgStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgStudent::getClassId, classId);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchPromote(List<Long> studentIds, Long targetClassId) {
        if (studentIds == null || studentIds.isEmpty()) return 0;
        LambdaUpdateWrapper<KgStudent> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(KgStudent::getId, studentIds).set(KgStudent::getClassId, targetClassId);
        return this.baseMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchGraduate(List<Long> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) return 0;
        LambdaUpdateWrapper<KgStudent> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(KgStudent::getId, studentIds).set(KgStudent::getStatus, 2);
        return this.baseMapper.update(null, wrapper);
    }
}
