package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.entity.KgRepairOrder;
import com.starlink.campus.module.kindergarten.entity.KgPatrolPoint;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRoute;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolRecordMapper;
import com.starlink.campus.module.kindergarten.mapper.KgRepairOrderMapper;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolPointMapper;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolRouteMapper;
import com.starlink.campus.module.kindergarten.service.PatrolInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class PatrolInspectionServiceImpl extends ServiceImpl<KgPatrolRecordMapper, KgPatrolRecord> implements PatrolInspectionService {

    @Autowired
    private KgRepairOrderMapper repairOrderMapper;

    @Autowired
    private KgPatrolRecordMapper patrolRecordMapper;

    @Autowired
    private KgPatrolPointMapper patrolPointMapper;

    @Autowired
    private KgPatrolRouteMapper patrolRouteMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitPatrol(KgPatrolRecord record) {
        record.setPatrolTime(LocalDateTime.now());
        if (record.getCreateTime() == null) {
            record.setCreateTime(LocalDateTime.now());
        }
        
        if (record.getId() == null) {
            patrolRecordMapper.insert(record);
        } else {
            patrolRecordMapper.updateById(record);
        }

        if (record.getIsNormal() != null && record.getIsNormal() == 0) {
            KgRepairOrder order = new KgRepairOrder();
            order.setPatrolRecordId(record.getId());
            order.setOrderNo("REP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            order.setDescription("巡检异常自动报修: " + record.getAbnormalDesc());
            order.setStatus("待处理");
            order.setCreateTime(LocalDateTime.now());
            repairOrderMapper.insert(order);
        }
    }

    @Override
    public List<KgPatrolRecord> getPatrolList() {
        return this.list();
    }

    @Override
    public List<KgRepairOrder> getRepairOrders() {
        return repairOrderMapper.selectList(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateTasks(Long staffId) {
        QueryWrapper<KgPatrolPoint> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<KgPatrolPoint> points = patrolPointMapper.selectList(queryWrapper);
        
        for (KgPatrolPoint point : points) {
            KgPatrolRecord record = new KgPatrolRecord();
            record.setPatrolPointName(point.getPointName());
            record.setPatrolStaffId(staffId);
            record.setStatus("待巡检");
            record.setCreateTime(LocalDateTime.now());
            patrolRecordMapper.insert(record);
        }
        return true;
    }

    @Override
    public List<KgPatrolPoint> listPoints() {
        return patrolPointMapper.selectList(new QueryWrapper<KgPatrolPoint>().orderByAsc("sort_order"));
    }

    @Override
    public boolean addPoint(KgPatrolPoint point) {
        if (point.getCreateTime() == null) {
            point.setCreateTime(LocalDateTime.now());
        }
        return patrolPointMapper.insert(point) > 0;
    }

    @Override
    public boolean updatePoint(KgPatrolPoint point) {
        return patrolPointMapper.updateById(point) > 0;
    }

    @Override
    public boolean deletePoint(Long id) {
        return patrolPointMapper.deleteById(id) > 0;
    }

    @Override
    public List<KgPatrolRoute> listRoutes() {
        return patrolRouteMapper.selectList(new QueryWrapper<KgPatrolRoute>().orderByDesc("create_time"));
    }

    @Override
    public boolean addRoute(KgPatrolRoute route) {
        if (route.getCreateTime() == null) {
            route.setCreateTime(LocalDateTime.now());
        }
        return patrolRouteMapper.insert(route) > 0;
    }

    @Override
    public boolean updateRoute(KgPatrolRoute route) {
        return patrolRouteMapper.updateById(route) > 0;
    }

    @Override
    public boolean deleteRoute(Long id) {
        return patrolRouteMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateTasksByRoute(Long routeId, Long staffId) {
        KgPatrolRoute route = patrolRouteMapper.selectById(routeId);
        if (route == null || !StringUtils.hasText(route.getPointIds())) {
            return false;
        }
        
        String[] ids = route.getPointIds().split(",");
        for (String idStr : ids) {
            Long pointId = Long.parseLong(idStr.trim());
            KgPatrolPoint point = patrolPointMapper.selectById(pointId);
            if (point != null && Integer.valueOf(1).equals(point.getStatus())) {
                KgPatrolRecord record = new KgPatrolRecord();
                record.setPatrolPointName(point.getPointName());
                record.setPatrolStaffId(staffId);
                record.setStatus("待巡检");
                record.setCreateTime(LocalDateTime.now());
                patrolRecordMapper.insert(record);
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> getPatrolStats() {
        Map<String, Object> stats = new HashMap<>();
        List<KgPatrolRecord> allRecords = patrolRecordMapper.selectList(null);
        long totalTasks = allRecords.size();
        long completedTasks = allRecords.stream().filter(r -> "已完成".equals(r.getStatus())).count();
        long abnormalCount = allRecords.stream().filter(r -> r.getIsNormal() != null && r.getIsNormal() == 0).count();
        
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        long todayTasks = allRecords.stream().filter(r -> r.getCreateTime() != null && r.getCreateTime().isAfter(startOfDay)).count();
        
        double completionRate = totalTasks == 0 ? 0.0 : (double) completedTasks / totalTasks * 100;
        
        stats.put("totalTasks", totalTasks);
        stats.put("completedTasks", completedTasks);
        stats.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        stats.put("todayTasks", todayTasks);
        stats.put("abnormalCount", abnormalCount);
        
        return stats;
    }
}
