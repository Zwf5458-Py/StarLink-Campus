package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgOpenDayEvent;
import com.starlink.campus.module.kindergarten.entity.KgReferralRecord;
import com.starlink.campus.module.kindergarten.mapper.KgOpenDayEventMapper;
import com.starlink.campus.module.kindergarten.mapper.KgReferralRecordMapper;
import com.starlink.campus.module.kindergarten.service.EnrollmentGrowthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentGrowthServiceImpl implements EnrollmentGrowthService {

    @Autowired(required = false)
    private KgOpenDayEventMapper eventMapper;

    @Autowired(required = false)
    private KgReferralRecordMapper referralMapper;

    @Override
    public KgOpenDayEvent createEvent(KgOpenDayEvent event) {
        event.setCreateTime(LocalDateTime.now());
        if (event.getStatus() == null) event.setStatus("PUBLISHED");
        eventMapper.insert(event);
        return event;
    }

    @Override
    public boolean enrollInEvent(Long eventId) {
        KgOpenDayEvent event = eventMapper.selectById(eventId);
        if (event != null && !"FULL".equals(event.getStatus()) && !"COMPLETED".equals(event.getStatus())) {
            int current = event.getEnrolledCount() == null ? 0 : event.getEnrolledCount();
            if (current < event.getCapacity()) {
                event.setEnrolledCount(current + 1);
                if (current + 1 == event.getCapacity()) {
                    event.setStatus("FULL");
                }
                return eventMapper.updateById(event) > 0;
            }
        }
        return false;
    }

    @Override
    public List<KgOpenDayEvent> listEvents() {
        return eventMapper.selectList(new QueryWrapper<KgOpenDayEvent>().orderByDesc("event_date"));
    }

    @Override
    public KgReferralRecord addReferral(KgReferralRecord record) {
        record.setCreateTime(LocalDateTime.now());
        if (record.getReferralStatus() == null) record.setReferralStatus("LEAD");
        if (record.getRewardStatus() == null) record.setRewardStatus("UNPAID");
        referralMapper.insert(record);
        return record;
    }

    @Override
    public boolean updateReferralStatus(Long recordId, String status) {
        KgReferralRecord record = referralMapper.selectById(recordId);
        if (record != null) {
            record.setReferralStatus(status);
            return referralMapper.updateById(record) > 0;
        }
        return false;
    }

    @Override
    public List<KgReferralRecord> listReferrals() {
        return referralMapper.selectList(new QueryWrapper<KgReferralRecord>().orderByDesc("create_time"));
    }
}
