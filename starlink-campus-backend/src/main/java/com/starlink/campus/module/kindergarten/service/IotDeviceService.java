package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgEnvironmentMonitor;
import com.starlink.campus.module.kindergarten.entity.KgSmartGateRecord;
import java.util.List;

public interface IotDeviceService {
    KgEnvironmentMonitor recordEnvironmentData(KgEnvironmentMonitor data);
    List<KgEnvironmentMonitor> listClassEnvironment(Long classId);

    KgSmartGateRecord logGatePass(KgSmartGateRecord record);
    List<KgSmartGateRecord> listPersonPassRecords(Long personId, String personType);
}
