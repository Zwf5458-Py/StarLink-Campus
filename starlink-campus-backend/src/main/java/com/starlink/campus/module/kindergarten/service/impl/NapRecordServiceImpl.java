package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgNapRecord;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.mapper.KgNapRecordMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStudentMapper;
import com.starlink.campus.module.kindergarten.service.NapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class NapRecordServiceImpl implements NapRecordService {

    @Autowired
    private KgNapRecordMapper napMapper;

    @Autowired
    private KgStudentMapper studentMapper;

    @Override
    public boolean saveNapRecord(KgNapRecord record) {
        if (record.getId() != null) {
            return napMapper.updateById(record) > 0;
        }
        return napMapper.insert(record) > 0;
    }

    @Override
    public List<KgNapRecord> listNapRecordsByDate(LocalDate date, Long classId) {
        List<KgStudent> students = studentMapper.selectList(new QueryWrapper<KgStudent>().eq("class_id", classId));
        if (students.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        List<Long> studentIds = students.stream().map(KgStudent::getId).collect(java.util.stream.Collectors.toList());
        return napMapper.selectList(new QueryWrapper<KgNapRecord>()
                .eq("record_date", date)
                .in("student_id", studentIds));
    }
}
