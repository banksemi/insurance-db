package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverSearchCondition;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;

import java.time.LocalDate;
import java.util.List;

public interface CaregiverService {
    List<Caregiver> getCaregivers(Long insuranceId, CaregiverSearchCondition searchCondition);
    Caregiver getCaregiver(Long caregiverId);
    Long addCaregiver(Long insuranceId, CaregiverCreateRequest request);
    Long requestEndDate(Long caregiverId, LocalDate endDate);
    void updateMemo(Long caregiverId, String memo);
}
