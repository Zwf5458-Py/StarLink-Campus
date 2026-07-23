package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgGrowthRecord;
import com.starlink.campus.module.kindergarten.mapper.KgGrowthRecordMapper;
import com.starlink.campus.module.kindergarten.service.GrowthRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GrowthRecordServiceImpl extends ServiceImpl<KgGrowthRecordMapper, KgGrowthRecord> implements GrowthRecordService {

    private static final Logger log = LoggerFactory.getLogger(GrowthRecordServiceImpl.class);

    @Override
    public Page<KgGrowthRecord> listByStudent(Long studentId, Integer pageNum, Integer pageSize) {
        Page<KgGrowthRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<KgGrowthRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgGrowthRecord::getStudentId, studentId)
               .orderByDesc(KgGrowthRecord::getRecordDate);
        return this.page(page, wrapper);
    }

    @Override
    public void addRecord(KgGrowthRecord record) {
        if (record.getCreateTime() == null) {
            record.setCreateTime(LocalDateTime.now());
        }
        this.save(record);
        log.info("新增成长记录: 学生={}, 类别={}, 值={}", record.getStudentId(), record.getCategory(), record.getValue());
    }

    @Override
    public void updateRecord(KgGrowthRecord record) {
        this.updateById(record);
    }

    @Override
    public void deleteRecord(Long id) {
        this.removeById(id);
    }

    @Override
    public List<Map<String, Object>> getGrowthTrend(Long studentId, String category) {
        return this.baseMapper.getGrowthTrend(studentId, category);
    }

    @Override
    public Map<String, Object> generateSemesterReport(Long studentId, String semester) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("studentId", studentId);
        report.put("semester", semester);

        LambdaQueryWrapper<KgGrowthRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KgGrowthRecord::getStudentId, studentId)
               .eq(KgGrowthRecord::getSemester, semester);
        List<KgGrowthRecord> records = this.list(wrapper);

        // 按类别分组统计
        Map<String, List<KgGrowthRecord>> byCategory = records.stream()
                .collect(Collectors.groupingBy(KgGrowthRecord::getCategory));

        List<Map<String, Object>> dimensions = new ArrayList<>();
        String[] categories = {"身高", "体重", "语言", "社交", "运动", "艺术"};
        for (String cat : categories) {
            Map<String, Object> dim = new HashMap<>();
            dim.put("category", cat);
            List<KgGrowthRecord> catRecords = byCategory.getOrDefault(cat, Collections.emptyList());
            dim.put("recordCount", catRecords.size());
            if (!catRecords.isEmpty()) {
                KgGrowthRecord latest = catRecords.stream()
                        .max(Comparator.comparing(KgGrowthRecord::getRecordDate))
                        .orElse(null);
                if (latest != null) {
                    dim.put("latestValue", latest.getValue());
                    dim.put("latestComment", latest.getTeacherComment());
                }
            }
            dimensions.add(dim);
        }
        report.put("dimensions", dimensions);
        report.put("totalRecords", records.size());
        report.put("generatedAt", LocalDateTime.now().toString());
        return report;
    }
}
