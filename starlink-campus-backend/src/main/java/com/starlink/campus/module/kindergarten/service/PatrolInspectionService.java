package com.starlink.campus.module.kindergarten.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.entity.KgRepairOrder;
import com.starlink.campus.module.kindergarten.entity.KgPatrolPoint;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRoute;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface PatrolInspectionService extends IService<KgPatrolRecord> {
    void submitPatrol(KgPatrolRecord record);
    Page<KgPatrolRecord> getPatrolList(Integer pageNum, Integer pageSize);
    List<KgPatrolRecord> getPatrolList();
    List<KgRepairOrder> getRepairOrders();
    boolean generateTasks(Long staffId);

    // 点位 CRUD
    List<KgPatrolPoint> listPoints();
    boolean addPoint(KgPatrolPoint point);
    boolean updatePoint(KgPatrolPoint point);
    boolean deletePoint(Long id);

    // 路线管理
    List<KgPatrolRoute> listRoutes();
    boolean addRoute(KgPatrolRoute route);
    boolean updateRoute(KgPatrolRoute route);
    boolean deleteRoute(Long id);

    // 基于路线生成巡检任务
    boolean generateTasksByRoute(Long routeId, Long staffId);

    // 统计报表
    Map<String, Object> getPatrolStats();
}
