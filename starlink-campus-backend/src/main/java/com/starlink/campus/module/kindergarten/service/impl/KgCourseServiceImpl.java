package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgCourse;
import com.starlink.campus.module.kindergarten.mapper.KgCourseMapper;
import com.starlink.campus.module.kindergarten.service.KgCourseService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KgCourseServiceImpl extends ServiceImpl<KgCourseMapper, KgCourse> implements KgCourseService {
}
