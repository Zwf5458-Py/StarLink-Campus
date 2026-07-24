package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgSchoolBus;
import com.starlink.campus.module.kindergarten.entity.KgBusRecord;
import java.util.List;

public interface SchoolBusService {
    KgSchoolBus saveSchoolBus(KgSchoolBus bus);
    KgBusRecord logBusRecord(KgBusRecord record);
    List<KgBusRecord> listStudentBusRecords(Long studentId);
}
