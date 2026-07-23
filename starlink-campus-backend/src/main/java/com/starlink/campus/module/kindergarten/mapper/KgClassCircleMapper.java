package com.starlink.campus.module.kindergarten.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgClassCircle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface KgClassCircleMapper extends BaseMapper<KgClassCircle> {

    /**
     * 原子点赞，避免读-改-写竞态
     */
    @Update("UPDATE kg_class_circle SET likes = likes + 1 WHERE id = #{id}")
    int incrementLikes(@Param("id") Long id);
}
