package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgEnrollment;

import java.util.Map;

public interface EnrollmentService extends IService<KgEnrollment> {
    Page<KgEnrollment> list(Integer pageNum, Integer pageSize, String status);
    boolean add(KgEnrollment enrollment);
    boolean update(KgEnrollment enrollment);
    boolean delete(Long id);
    boolean updateStatus(Long id, String newStatus);
    Map<String, Object> getFunnelStats();
}
