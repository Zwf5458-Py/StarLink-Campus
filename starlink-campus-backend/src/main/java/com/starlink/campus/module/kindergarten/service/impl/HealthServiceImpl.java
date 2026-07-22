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
        LambdaQueryWrapper<KgMorningCheck> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgMorningCheck::getCheckDate, date);
        List<KgMorningCheck> checks = this.list(wrapper);
        
        long total = checks.size();
        long fever = checks.stream().filter(c -> c.getIsFever() != null && c.getIsFever() == 1).count();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("date", date);
        summary.put("totalChecked", total);
        summary.put("feverCount", fever);
        return summary;
    }

    @Override
    public List<String> checkRecipeAllergies(List<String> ingredients) {
        List<KgStudent> students = studentMapper.selectList(null);
        List<String> warnings = new ArrayList<>();
        
        for (KgStudent student : students) {
            if (student.getAllergies() != null && !student.getAllergies().isEmpty() && !"无".equals(student.getAllergies())) {
                for (String ingredient : ingredients) {
                    if (student.getAllergies().contains(ingredient)) {
                        warnings.add("学生 " + student.getName() + " 对 " + ingredient + " 过敏");
                    }
                }
            }
        }
        return warnings;
    }
}
