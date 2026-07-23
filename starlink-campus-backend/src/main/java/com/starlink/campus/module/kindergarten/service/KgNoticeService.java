package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgNotice;
import java.util.List;

public interface KgNoticeService extends IService<KgNotice> {
    List<KgNotice> getUnreadNoticeList();
    boolean markAsRead(Long id);
}
