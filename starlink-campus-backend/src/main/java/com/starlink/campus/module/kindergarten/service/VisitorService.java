package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgVisitorRecord;

import java.util.List;

public interface VisitorService extends IService<KgVisitorRecord> {
    String createVisitorPass(KgVisitorRecord visitor);
    boolean verifyPass(String passCode);
    boolean approvePass(Long id);
    boolean rejectPass(Long id);
    List<KgVisitorRecord> checkOvertime();
    List<KgVisitorRecord> getVisitorList();
}
