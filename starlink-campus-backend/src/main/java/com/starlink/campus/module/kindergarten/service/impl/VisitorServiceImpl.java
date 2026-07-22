package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgVisitorRecord;
import com.starlink.campus.module.kindergarten.mapper.KgVisitorRecordMapper;
import com.starlink.campus.module.kindergarten.service.VisitorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VisitorServiceImpl extends ServiceImpl<KgVisitorRecordMapper, KgVisitorRecord> implements VisitorService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createVisitorPass(KgVisitorRecord visitor) {
        String passCode = "PASS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        visitor.setPassCode(passCode);
        visitor.setQrcodeUrl("/api/static/qrcode/" + passCode + ".png");
        visitor.setStatus("待审核");
        visitor.setOvertimeAlerted(0);
        visitor.setCreateTime(LocalDateTime.now());
        this.save(visitor);
        return passCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verifyPass(String passCode) {
        LambdaQueryWrapper<KgVisitorRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgVisitorRecord::getPassCode, passCode);
        KgVisitorRecord record = this.getOne(wrapper);
        if (record != null && "已通过".equals(record.getStatus())) {
            record.setCheckInTime(LocalDateTime.now());
            record.setStatus("在园中");
            this.updateById(record);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approvePass(Long id) {
        KgVisitorRecord record = this.getById(id);
        if (record != null && "待审核".equals(record.getStatus())) {
            record.setStatus("已通过");
            return this.updateById(record);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectPass(Long id) {
        KgVisitorRecord record = this.getById(id);
        if (record != null && "待审核".equals(record.getStatus())) {
            record.setStatus("已拒绝");
            return this.updateById(record);
        }
        return false;
    }

    @Override
    public List<KgVisitorRecord> checkOvertime() {
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(15);
        LambdaQueryWrapper<KgVisitorRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgVisitorRecord::getStatus, "在园中")
               .le(KgVisitorRecord::getCheckInTime, deadline);
        
        List<KgVisitorRecord> overtimeList = this.list(wrapper);
        for (KgVisitorRecord record : overtimeList) {
            record.setOvertimeAlerted(1);
            this.updateById(record);
        }
        return overtimeList;
    }

    @Override
    public List<KgVisitorRecord> getVisitorList() {
        return this.list();
    }
}
