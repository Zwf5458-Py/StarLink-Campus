package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgGrowthRecord;

import java.util.List;
import java.util.Map;

public interface GrowthRecordService extends IService<KgGrowthRecord> {
    Page<KgGrowthRecord> listByStudent(Long studentId, Integer pageNum, Integer pageSize);
    void addRecord(KgGrowthRecord record);
    void updateRecord(KgGrowthRecord record);
    void deleteRecord(Long id);
    List<Map<String, Object>> getGrowthTrend(Long studentId, String category);
    Map<String, Object> generateSemesterReport(Long studentId, String semester);
}
