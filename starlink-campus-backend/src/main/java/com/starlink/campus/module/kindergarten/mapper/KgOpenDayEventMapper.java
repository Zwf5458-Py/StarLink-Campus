package com.starlink.campus.module.kindergarten.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgOpenDayEvent;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Update;

@Mapper
public interface KgOpenDayEventMapper extends BaseMapper<KgOpenDayEvent> {

    @Update("UPDATE kg_open_day_event SET enrolled_count = enrolled_count + 1 WHERE id = #{id} AND enrolled_count < capacity")
    int updateEnrolledCount(Long id);
}
