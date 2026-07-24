package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgFoodSample;
import com.starlink.campus.module.kindergarten.entity.KgFoodSupplier;
import java.util.List;

public interface CanteenSafetyService {
    KgFoodSupplier saveSupplier(KgFoodSupplier supplier);
    KgFoodSample recordFoodSample(KgFoodSample sample);
    boolean destroyFoodSample(Long sampleId, Long destroyerId);
    List<KgFoodSample> listWarningSamples(); // those approaching 48h limit
}
