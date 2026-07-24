package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgMedicationApplication;
import com.starlink.campus.module.kindergarten.entity.KgMedicationExecution;
import com.starlink.campus.module.kindergarten.mapper.KgMedicationApplicationMapper;
import com.starlink.campus.module.kindergarten.mapper.KgMedicationExecutionMapper;
import com.starlink.campus.module.kindergarten.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private KgMedicationApplicationMapper appMapper;
    
    @Autowired
    private KgMedicationExecutionMapper execMapper;

    @Override
    public KgMedicationApplication submitApplication(KgMedicationApplication application) {
        application.setStatus("PENDING");
        appMapper.insert(application);
        return application;
    }

    @Override
    public boolean acceptApplication(Long applicationId) {
        KgMedicationApplication app = appMapper.selectById(applicationId);
        if (app != null && "PENDING".equals(app.getStatus())) {
            app.setStatus("ACCEPTED");
            return appMapper.updateById(app) > 0;
        }
        return false;
    }

    @Override
    public boolean rejectApplication(Long applicationId) {
        KgMedicationApplication app = appMapper.selectById(applicationId);
        if (app != null && "PENDING".equals(app.getStatus())) {
            app.setStatus("REJECTED");
            return appMapper.updateById(app) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public KgMedicationExecution executeMedication(KgMedicationExecution execution) {
        execMapper.insert(execution);
        KgMedicationApplication app = appMapper.selectById(execution.getApplicationId());
        if (app != null) {
            app.setStatus("COMPLETED");
            appMapper.updateById(app);
        }
        return execution;
    }

    @Override
    public List<KgMedicationApplication> listPendingApplications(Long studentId) {
        QueryWrapper<KgMedicationApplication> qw = new QueryWrapper<>();
        qw.eq("status", "PENDING");
        if (studentId != null) {
            qw.eq("student_id", studentId);
        }
        return appMapper.selectList(qw);
    }
}
