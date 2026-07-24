package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgMedicationApplication;
import com.starlink.campus.module.kindergarten.entity.KgMedicationExecution;
import java.util.List;

public interface MedicationService {
    KgMedicationApplication submitApplication(KgMedicationApplication application);
    boolean acceptApplication(Long applicationId);
    boolean rejectApplication(Long applicationId);
    KgMedicationExecution executeMedication(KgMedicationExecution execution);
    List<KgMedicationApplication> listPendingApplications(Long studentId);
}
