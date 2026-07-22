#!/bin/bash
BASE_DIR="/Users/oraclez/Desktop/zwf/StarLink Campus/starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten"
ENTITY_DIR="$BASE_DIR/entity"
MAPPER_DIR="$BASE_DIR/mapper"
SERVICE_DIR="$BASE_DIR/service"
IMPL_DIR="$SERVICE_DIR/impl"
CONTROLLER_DIR="$BASE_DIR/controller"

mkdir -p "$ENTITY_DIR" "$MAPPER_DIR" "$IMPL_DIR" "$CONTROLLER_DIR"

# KgClassCircle Entity
cat << 'JAVA' > "$ENTITY_DIR/KgClassCircle.java"
package com.starlink.campus.module.kindergarten.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("kg_class_circle")
public class KgClassCircle {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String author;
    private String className;
    private String content;
    private Integer likes;
    private Integer comments;
    private LocalDateTime publishTime;
}
JAVA

# KgClassCircle Mapper
cat << 'JAVA' > "$MAPPER_DIR/KgClassCircleMapper.java"
package com.starlink.campus.module.kindergarten.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgClassCircle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KgClassCircleMapper extends BaseMapper<KgClassCircle> {}
JAVA

# KgClassCircle Service
cat << 'JAVA' > "$SERVICE_DIR/KgClassCircleService.java"
package com.starlink.campus.module.kindergarten.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgClassCircle;

public interface KgClassCircleService extends IService<KgClassCircle> {}
JAVA

# KgClassCircle ServiceImpl
cat << 'JAVA' > "$IMPL_DIR/KgClassCircleServiceImpl.java"
package com.starlink.campus.module.kindergarten.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgClassCircle;
import com.starlink.campus.module.kindergarten.mapper.KgClassCircleMapper;
import com.starlink.campus.module.kindergarten.service.KgClassCircleService;
import org.springframework.stereotype.Service;

@Service
public class KgClassCircleServiceImpl extends ServiceImpl<KgClassCircleMapper, KgClassCircle> implements KgClassCircleService {}
JAVA

# KgArticle Entity
cat << 'JAVA' > "$ENTITY_DIR/KgArticle.java"
package com.starlink.campus.module.kindergarten.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("kg_article")
public class KgArticle {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String author;
    private String content;
    private String category;
    private Integer views;
    private LocalDateTime createTime;
}
JAVA

# KgArticle Mapper
cat << 'JAVA' > "$MAPPER_DIR/KgArticleMapper.java"
package com.starlink.campus.module.kindergarten.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgArticle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KgArticleMapper extends BaseMapper<KgArticle> {}
JAVA

# KgArticle Service
cat << 'JAVA' > "$SERVICE_DIR/KgArticleService.java"
package com.starlink.campus.module.kindergarten.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgArticle;

public interface KgArticleService extends IService<KgArticle> {}
JAVA

# KgArticle ServiceImpl
cat << 'JAVA' > "$IMPL_DIR/KgArticleServiceImpl.java"
package com.starlink.campus.module.kindergarten.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgArticle;
import com.starlink.campus.module.kindergarten.mapper.KgArticleMapper;
import com.starlink.campus.module.kindergarten.service.KgArticleService;
import org.springframework.stereotype.Service;

@Service
public class KgArticleServiceImpl extends ServiceImpl<KgArticleMapper, KgArticle> implements KgArticleService {}
JAVA

echo "Generated Entities, Mappers, Services."
