package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgSchoolBus;
import com.starlink.campus.module.kindergarten.entity.KgBusRecord;
import com.starlink.campus.module.kindergarten.mapper.KgSchoolBusMapper;
import com.starlink.campus.module.kindergarten.mapper.KgBusRecordMapper;
import com.starlink.campus.module.kindergarten.service.SchoolBusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchoolBusServiceImpl implements SchoolBusService {

    @Autowired(required = false)
    private KgSchoolBusMapper busMapper;

    @Autowired(required = false)
    private KgBusRecordMapper recordMapper;

    @Override
    public KgSchoolBus saveSchoolBus(KgSchoolBus bus) {
        if (bus.getId() != null) {
            busMapper.updateById(bus);
        } else {
            busMapper.insert(bus);
        }
        return bus;
    }

    @Override
    public KgBusRecord logBusRecord(KgBusRecord record) {
        record.setActionTime(LocalDateTime.now());
        recordMapper.insert(record);
        return record;
    }

    @Override
    public List<KgBusRecord> listStudentBusRecords(Long studentId) {
        return recordMapper.selectList(new QueryWrapper<KgBusRecord>()
                .eq("student_id", studentId)
                .orderByDesc("action_time"));
    }
}
