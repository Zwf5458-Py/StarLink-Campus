package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgFoodSample;
import com.starlink.campus.module.kindergarten.entity.KgFoodSupplier;
import com.starlink.campus.module.kindergarten.mapper.KgFoodSampleMapper;
import com.starlink.campus.module.kindergarten.mapper.KgFoodSupplierMapper;
import com.starlink.campus.module.kindergarten.service.CanteenSafetyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CanteenSafetyServiceImpl implements CanteenSafetyService {

    @Autowired
    private KgFoodSupplierMapper supplierMapper;

    @Autowired
    private KgFoodSampleMapper sampleMapper;

    @Override
    public KgFoodSupplier saveSupplier(KgFoodSupplier supplier) {
        if (supplier.getId() != null) {
            supplierMapper.updateById(supplier);
        } else {
            supplierMapper.insert(supplier);
        }
        return supplier;
    }

    @Override
    public KgFoodSample recordFoodSample(KgFoodSample sample) {
        sample.setSampleTime(LocalDateTime.now());
        sampleMapper.insert(sample);
        return sample;
    }

    @Override
    public boolean destroyFoodSample(Long sampleId, Long destroyerId) {
        KgFoodSample sample = sampleMapper.selectById(sampleId);
        if (sample != null && sample.getDestroyTime() == null) {
            sample.setDestroyTime(LocalDateTime.now());
            sample.setDestroyerId(destroyerId);
            return sampleMapper.updateById(sample) > 0;
        }
        return false;
    }

    @Override
    public List<KgFoodSample> listWarningSamples() {
        // Find samples older than 46 hours that haven't been destroyed yet
        LocalDateTime threshold = LocalDateTime.now().minusHours(46);
        return sampleMapper.selectList(new QueryWrapper<KgFoodSample>()
                .isNull("destroy_time")
                .le("sample_time", threshold));
    }
}
