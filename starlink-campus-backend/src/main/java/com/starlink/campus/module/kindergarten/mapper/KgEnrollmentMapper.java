package com.starlink.campus.module.kindergarten.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgEnrollment;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;
import java.util.Map;

@Mapper
public interface KgEnrollmentMapper extends BaseMapper<KgEnrollment> {

    @MapKey("status")
    @Select("SELECT status, COUNT(*) as count FROM kg_enrollment GROUP BY status")
    Map<String, Map<String, Object>> countByStatus();
}
