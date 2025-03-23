package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverRequest;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.insurance.entity.Insurance;

import java.time.LocalDate;
import java.util.List;

public interface CaregiverService {
    List<Caregiver> getCaregivers(Long insuranceId);
    Caregiver getCaregiver(Long id);
    Long createCaregiver(Long insuranceId, CaregiverRequest request);
    Long updateCaregiver(Long caregiverId, CaregiverRequest request);
    void updateMemo(Long caregiverId, String memo);
    void approveCaregiver(Long caregiverId);

    Long createCaregiverAsApproved(Long insuranceId, CaregiverRequest request);
    Long updateCaregiverAsApproved(Long caregiverId, CaregiverRequest request);
}
