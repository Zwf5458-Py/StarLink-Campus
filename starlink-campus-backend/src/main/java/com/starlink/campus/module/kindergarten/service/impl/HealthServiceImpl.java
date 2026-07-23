package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgMorningCheck;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.mapper.KgMorningCheckMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStudentMapper;
import com.starlink.campus.module.kindergarten.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class HealthServiceImpl extends ServiceImpl<KgMorningCheckMapper, KgMorningCheck> implements HealthService {

    @Autowired
    private KgStudentMapper studentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMorningCheck(KgMorningCheck check) {
        if (check.getTemperature() != null && check.getTemperature() > 37.3) {
            check.setIsFever(1);
        } else {
            check.setIsFever(0);
        }
        check.setCheckDate(LocalDate.now());
        check.setCreateTime(LocalDateTime.now());
        this.save(check);
    }

    @Override
    public Map<String, Object> getMorningSummary(LocalDate date) {
        long total = this.count(new LambdaQueryWrapper<KgMorningCheck>().eq(KgMorningCheck::getCheckDate, date));
        long fever = this.count(new LambdaQueryWrapper<KgMorningCheck>()
                .eq(KgMorningCheck::getCheckDate, date)
                .eq(KgMorningCheck::getIsFever, 1));
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("date", date);
        summary.put("totalChecked", total);
        summary.put("feverCount", fever);
        return summary;
    }

    @Override
    public List<String> checkRecipeAllergies(List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<KgStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(KgStudent::getAllergies)
               .ne(KgStudent::getAllergies, "")
               .ne(KgStudent::getAllergies, "无");
        
        // 使用 like 条件，任何一个食材被包含即可
        wrapper.and(w -> {
            for (int i = 0; i < ingredients.size(); i++) {
                if (i == 0) {
                    w.like(KgStudent::getAllergies, ingredients.get(i));
                } else {
                    w.or().like(KgStudent::getAllergies, ingredients.get(i));
                }
            }
        });

        List<KgStudent> students = studentMapper.selectList(wrapper);
        List<String> warnings = new ArrayList<>();
        
        for (KgStudent student : students) {
            for (String ingredient : ingredients) {
                if (student.getAllergies().contains(ingredient)) {
                    warnings.add("学生 " + student.getName() + " 对 " + ingredient + " 过敏");
                }
            }
        }
        return warnings;
    }
}
