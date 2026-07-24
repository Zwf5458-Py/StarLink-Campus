package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgOpenDayEvent;
import com.starlink.campus.module.kindergarten.entity.KgReferralRecord;
import java.util.List;

public interface EnrollmentGrowthService {
    KgOpenDayEvent createEvent(KgOpenDayEvent event);
    boolean enrollInEvent(Long eventId);
    List<KgOpenDayEvent> listEvents();

    KgReferralRecord addReferral(KgReferralRecord record);
    boolean updateReferralStatus(Long recordId, String status);
    List<KgReferralRecord> listReferrals();
}
