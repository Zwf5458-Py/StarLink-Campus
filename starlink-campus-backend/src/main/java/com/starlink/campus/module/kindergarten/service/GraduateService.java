package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgGraduateRecord;
import java.util.List;

public interface GraduateService {
    KgGraduateRecord saveGraduateRecord(KgGraduateRecord record);
    List<KgGraduateRecord> listRecordsByYear(Integer year);
}
