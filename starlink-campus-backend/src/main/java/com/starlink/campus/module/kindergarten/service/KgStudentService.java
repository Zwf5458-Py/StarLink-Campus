package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgStudent;

import java.util.List;

public interface KgStudentService extends IService<KgStudent> {
    Page<KgStudent> list(Integer pageNum, Integer pageSize, String name, Long classId);
    void add(KgStudent student);
    void update(KgStudent student);
    void delete(Long id);
    List<KgStudent> listByClassId(Long classId);
}
