package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverEstimateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverEstimateResponse;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverSearchCondition;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.exception.NotFoundCaregiverException;
import com.sideproject.caregiver_management.insurance.entity.Insurance;

import java.time.LocalDate;
import java.util.List;

public interface CaregiverService {
    List<Caregiver> getCaregivers(Insurance insurance, CaregiverSearchCondition searchCondition);
    Caregiver getCaregiver(Long caregiverId) throws NotFoundCaregiverException;
    Long addCaregiver(Insurance insurance, CaregiverCreateRequest request);
    void requestEndDate(Long caregiverId, LocalDate endDate) throws NotFoundCaregiverException;
    void updateMemo(Long caregiverId, String memo);

    CaregiverEstimateResponse getCaregiverEstimate(Insurance insurance, CaregiverEstimateRequest request);
}
