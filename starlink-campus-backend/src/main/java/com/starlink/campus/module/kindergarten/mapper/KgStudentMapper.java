package com.starlink.campus.module.kindergarten.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface KgStudentMapper extends BaseMapper<KgStudent> {

    @MapKey("class_id")
    @Select("SELECT class_id, COUNT(*) as count FROM kg_student WHERE status = 1 GROUP BY class_id")
    Map<Long, Map<String, Object>> countStudentsByClass();
}
