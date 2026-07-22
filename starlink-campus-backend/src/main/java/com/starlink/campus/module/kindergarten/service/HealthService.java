package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgMorningCheck;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HealthService extends IService<KgMorningCheck> {
    void addMorningCheck(KgMorningCheck check);
    Map<String, Object> getMorningSummary(LocalDate date);
    List<String> checkRecipeAllergies(List<String> ingredients);
}
