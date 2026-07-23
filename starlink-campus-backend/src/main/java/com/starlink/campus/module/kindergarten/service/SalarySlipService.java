package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgSalarySlip;

import java.util.List;

public interface SalarySlipService extends IService<KgSalarySlip> {
    Page<KgSalarySlip> listByStaff(Long staffId, Integer pageNum, Integer pageSize);
    boolean addSlip(KgSalarySlip slip);
    boolean publishSlip(Long id);
    boolean batchPublish(List<Long> ids);
    Page<KgSalarySlip> listAll(Integer pageNum, Integer pageSize);
}
