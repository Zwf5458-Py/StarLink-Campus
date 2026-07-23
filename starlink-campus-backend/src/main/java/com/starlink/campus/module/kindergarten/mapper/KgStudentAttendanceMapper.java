package com.starlink.campus.module.kindergarten.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgStudentAttendance;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.Date;
import java.util.Map;

@Mapper
public interface KgStudentAttendanceMapper extends BaseMapper<KgStudentAttendance> {

    @MapKey("class_id")
    @Select("SELECT class_id, COUNT(*) as count FROM kg_student_attendance WHERE attendance_date = #{date} GROUP BY class_id")
    Map<Long, Map<String, Object>> countAttendanceByClass(@Param("date") Date date);
}
