package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgEnvironmentMonitor;
import com.starlink.campus.module.kindergarten.entity.KgSmartGateRecord;
import com.starlink.campus.module.kindergarten.mapper.KgEnvironmentMonitorMapper;
import com.starlink.campus.module.kindergarten.mapper.KgSmartGateRecordMapper;
import com.starlink.campus.module.kindergarten.service.IotDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Service
public class IotDeviceServiceImpl implements IotDeviceService {

    @Autowired(required = false)
    private KgEnvironmentMonitorMapper envMapper;

    @Autowired(required = false)
    private KgSmartGateRecordMapper gateMapper;

    @Override
    public KgEnvironmentMonitor recordEnvironmentData(KgEnvironmentMonitor data) {
        data.setRecordTime(LocalDateTime.now());
        // Simple threshold check for PM2.5 or CO2
        if (data.getPm25Level() != null && data.getPm25Level() > 100) {
            data.setWarningTriggered(1);
        } else if (data.getCo2Level() != null && data.getCo2Level() > 1000) {
            data.setWarningTriggered(1);
        } else {
            data.setWarningTriggered(0);
        }
        envMapper.insert(data);
        return data;
    }

    @Override
    public List<KgEnvironmentMonitor> listClassEnvironment(Long classId) {
        return envMapper.selectList(new QueryWrapper<KgEnvironmentMonitor>()
                .eq("class_id", classId)
                .orderByDesc("record_time")
                .last("LIMIT 24"));
    }

    @Override
    public KgSmartGateRecord logGatePass(KgSmartGateRecord record) {
        record.setPassTime(LocalDateTime.now());
        gateMapper.insert(record);
        return record;
    }

    @Override
    public List<KgSmartGateRecord> listPersonPassRecords(Long personId, String personType) {
        return gateMapper.selectList(new QueryWrapper<KgSmartGateRecord>()
                .eq("person_id", personId)
                .eq("person_type", personType)
                .orderByDesc("pass_time"));
    }
}
