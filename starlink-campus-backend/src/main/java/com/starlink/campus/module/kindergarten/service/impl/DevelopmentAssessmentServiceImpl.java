package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgDevelopmentAssessment;
import com.starlink.campus.module.kindergarten.mapper.KgDevelopmentAssessmentMapper;
import com.starlink.campus.module.kindergarten.service.DevelopmentAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DevelopmentAssessmentServiceImpl implements DevelopmentAssessmentService {

    @Autowired
    private KgDevelopmentAssessmentMapper mapper;

    @Override
    public KgDevelopmentAssessment saveAssessment(KgDevelopmentAssessment assessment) {
        if (assessment.getId() != null) {
            mapper.updateById(assessment);
        } else {
            mapper.insert(assessment);
        }
        return assessment;
    }

    @Override
    public List<KgDevelopmentAssessment> listStudentAssessments(Long studentId) {
        return mapper.selectList(new QueryWrapper<KgDevelopmentAssessment>()
                .eq("student_id", studentId)
                .orderByDesc("term"));
    }
}
