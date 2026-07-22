package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.entity.KgRepairOrder;

import java.util.List;

public interface PatrolInspectionService extends IService<KgPatrolRecord> {
    void submitPatrol(KgPatrolRecord record);
    List<KgPatrolRecord> getPatrolList();
    List<KgRepairOrder> getRepairOrders();
    boolean generateTasks(Long staffId);
}
