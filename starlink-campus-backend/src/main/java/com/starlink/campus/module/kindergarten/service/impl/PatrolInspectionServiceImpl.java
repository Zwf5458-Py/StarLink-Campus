package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.entity.KgRepairOrder;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolRecordMapper;
import com.starlink.campus.module.kindergarten.mapper.KgRepairOrderMapper;
import com.starlink.campus.module.kindergarten.service.PatrolInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PatrolInspectionServiceImpl extends ServiceImpl<KgPatrolRecordMapper, KgPatrolRecord> implements PatrolInspectionService {

    @Autowired
    private KgRepairOrderMapper repairOrderMapper;

    @Autowired
    private KgPatrolRecordMapper patrolRecordMapper;

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
        String[] defaultPoints = {"大门岗亭", "教学楼A区", "食堂操作间", "操场游乐区", "消防通道"};
        for (String point : defaultPoints) {
            KgPatrolRecord record = new KgPatrolRecord();
            record.setPatrolPointName(point);
            record.setPatrolStaffId(staffId);
            record.setStatus("待巡检");
            record.setCreateTime(LocalDateTime.now());
            patrolRecordMapper.insert(record);
        }
        return true;
    }
}
