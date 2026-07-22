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
}
