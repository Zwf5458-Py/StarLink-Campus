package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgPhysicalExam;
import java.util.List;

public interface HealthArchiveService {
    boolean savePhysicalExam(KgPhysicalExam exam);
    List<KgPhysicalExam> listStudentExams(Long studentId);
}
