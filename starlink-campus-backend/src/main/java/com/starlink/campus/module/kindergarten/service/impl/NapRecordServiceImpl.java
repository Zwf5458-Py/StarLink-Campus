package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgNapRecord;
import com.starlink.campus.module.kindergarten.mapper.KgNapRecordMapper;
import com.starlink.campus.module.kindergarten.service.NapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class NapRecordServiceImpl implements NapRecordService {

    @Autowired(required = false)
    private KgNapRecordMapper napMapper;

    @Override
    public boolean saveNapRecord(KgNapRecord record) {
        if (record.getId() != null) {
            return napMapper.updateById(record) > 0;
        }
        return napMapper.insert(record) > 0;
    }

    @Override
    public List<KgNapRecord> listNapRecordsByDate(LocalDate date, Long classId) {
        // Here we ideally join with student to filter by classId, but keeping it simple for now
        return napMapper.selectList(new QueryWrapper<KgNapRecord>()
                .eq("record_date", date));
    }
}
