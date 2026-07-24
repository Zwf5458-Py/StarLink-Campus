package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgPhysicalExam;
import com.starlink.campus.module.kindergarten.mapper.KgPhysicalExamMapper;
import com.starlink.campus.module.kindergarten.service.HealthArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HealthArchiveServiceImpl implements HealthArchiveService {

    @Autowired(required = false)
    private KgPhysicalExamMapper examMapper;

    @Override
    public boolean savePhysicalExam(KgPhysicalExam exam) {
        if (exam.getId() != null) {
            return examMapper.updateById(exam) > 0;
        }
        return examMapper.insert(exam) > 0;
    }

    @Override
    public List<KgPhysicalExam> listStudentExams(Long studentId) {
        return examMapper.selectList(new QueryWrapper<KgPhysicalExam>()
                .eq("student_id", studentId)
                .orderByDesc("exam_date"));
    }
}
