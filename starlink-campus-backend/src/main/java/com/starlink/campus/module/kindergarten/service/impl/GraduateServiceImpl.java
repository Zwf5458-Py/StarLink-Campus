package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgGraduateRecord;
import com.starlink.campus.module.kindergarten.mapper.KgGraduateRecordMapper;
import com.starlink.campus.module.kindergarten.service.GraduateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GraduateServiceImpl implements GraduateService {

    @Autowired
    private KgGraduateRecordMapper graduateMapper;

    @Override
    public KgGraduateRecord saveGraduateRecord(KgGraduateRecord record) {
        if (record.getId() != null) {
            graduateMapper.updateById(record);
        } else {
            record.setCreateTime(LocalDateTime.now());
            graduateMapper.insert(record);
        }
        return record;
    }

    @Override
    public List<KgGraduateRecord> listRecordsByYear(Integer year) {
        QueryWrapper<KgGraduateRecord> qw = new QueryWrapper<>();
        if (year != null) {
            qw.eq("graduation_year", year);
        }
        qw.orderByDesc("create_time");
        return graduateMapper.selectList(qw);
    }
}
