package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgStaffAttendance;
import com.starlink.campus.module.kindergarten.mapper.KgStaffAttendanceMapper;
import com.starlink.campus.module.kindergarten.service.KgStaffAttendanceService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KgStaffAttendanceServiceImpl extends ServiceImpl<KgStaffAttendanceMapper, KgStaffAttendance> implements KgStaffAttendanceService {
}
