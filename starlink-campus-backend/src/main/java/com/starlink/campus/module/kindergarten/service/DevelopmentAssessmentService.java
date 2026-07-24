package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgDevelopmentAssessment;
import java.util.List;

public interface DevelopmentAssessmentService {
    KgDevelopmentAssessment saveAssessment(KgDevelopmentAssessment assessment);
    List<KgDevelopmentAssessment> listStudentAssessments(Long studentId);
}
