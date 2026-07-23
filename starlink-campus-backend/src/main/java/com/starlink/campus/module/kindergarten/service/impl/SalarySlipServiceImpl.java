package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgSalarySlip;
import com.starlink.campus.module.kindergarten.mapper.KgSalarySlipMapper;
import com.starlink.campus.module.kindergarten.service.SalarySlipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalarySlipServiceImpl extends ServiceImpl<KgSalarySlipMapper, KgSalarySlip> implements SalarySlipService {

    private static final Logger log = LoggerFactory.getLogger(SalarySlipServiceImpl.class);

    @Override
    public Page<KgSalarySlip> listByStaff(Long staffId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KgSalarySlip> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KgSalarySlip::getStaffId, staffId)
                .eq(KgSalarySlip::getPublishStatus, "已发布")
                .orderByDesc(KgSalarySlip::getMonth);
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSlip(KgSalarySlip slip) {
        slip.setCreateTime(LocalDateTime.now());
        if (StringUtils.isBlank(slip.getPublishStatus())) {
            slip.setPublishStatus("待发布");
        }
        return save(slip);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishSlip(Long id) {
        KgSalarySlip slip = new KgSalarySlip();
        slip.setId(id);
        slip.setPublishStatus("已发布");
        return updateById(slip);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchPublish(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        List<KgSalarySlip> slips = ids.stream().map(id -> {
            KgSalarySlip slip = new KgSalarySlip();
            slip.setId(id);
            slip.setPublishStatus("已发布");
            return slip;
        }).toList();
        return updateBatchById(slips);
    }

    @Override
    public Page<KgSalarySlip> listAll(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KgSalarySlip> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(KgSalarySlip::getCreateTime);
        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }
}
