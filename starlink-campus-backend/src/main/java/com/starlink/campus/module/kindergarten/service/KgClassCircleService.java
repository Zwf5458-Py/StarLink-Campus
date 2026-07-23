package com.starlink.campus.module.kindergarten.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgClassCircle;

public interface KgClassCircleService extends IService<KgClassCircle> {
    /** 原子点赞，使用 SQL 级别自增避免竞态 */
    boolean atomicLike(Long id);
}
