package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgOaApproval;

public interface KgOaApprovalService extends IService<KgOaApproval> {
    boolean submit(KgOaApproval approval);
    boolean approve(Long id);
    boolean reject(Long id);
}
