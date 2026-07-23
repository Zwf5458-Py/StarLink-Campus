package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgVisitorRecord;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface VisitorService extends IService<KgVisitorRecord> {
    String createVisitorPass(KgVisitorRecord visitor);
    boolean verifyPass(String passCode);
    boolean approvePass(Long id);
    boolean rejectPass(Long id);
    List<KgVisitorRecord> checkOvertime();
    Page<KgVisitorRecord> getVisitorList(Integer pageNum, Integer pageSize);
    List<KgVisitorRecord> getVisitorList(); // 保留无参版本供导出等功能使用
}
