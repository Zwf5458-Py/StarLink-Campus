package com.starlink.campus.module.kindergarten.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgGrowthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface KgGrowthRecordMapper extends BaseMapper<KgGrowthRecord> {

    @Select("SELECT record_date, value FROM kg_growth_record WHERE student_id = #{studentId} AND category = #{category} ORDER BY record_date ASC")
    List<Map<String, Object>> getGrowthTrend(@Param("studentId") Long studentId, @Param("category") String category);
}
