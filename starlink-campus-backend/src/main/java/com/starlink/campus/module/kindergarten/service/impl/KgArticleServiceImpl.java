package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgArticle;
import com.starlink.campus.module.kindergarten.mapper.KgArticleMapper;
import com.starlink.campus.module.kindergarten.service.KgArticleService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KgArticleServiceImpl extends ServiceImpl<KgArticleMapper, KgArticle> implements KgArticleService {
}
