package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgNapRecord;
import java.time.LocalDate;
import java.util.List;

public interface NapRecordService {
    boolean saveNapRecord(KgNapRecord record);
    List<KgNapRecord> listNapRecordsByDate(LocalDate date, Long classId);
}
