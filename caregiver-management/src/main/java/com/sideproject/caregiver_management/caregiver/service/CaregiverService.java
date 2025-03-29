package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.*;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.exception.NotFoundCaregiverException;
import com.sideproject.caregiver_management.common.dto.ListResponse;
import com.sideproject.caregiver_management.insurance.entity.Insurance;

import java.time.LocalDate;
import java.util.List;

public interface CaregiverService {
    ListResponse<CaregiverResponse> getCaregivers(Insurance insurance, CaregiverSearchCondition searchCondition);
    Caregiver getCaregiver(Long caregiverId) throws NotFoundCaregiverException;
    Long addCaregiver(Insurance insurance, CaregiverCreateRequest request);
    void requestEndDate(Long caregiverId, LocalDate endDate) throws NotFoundCaregiverException;
    void updateMemo(Long caregiverId, String memo);

    CaregiverEstimateResponse getCaregiverEstimate(Insurance insurance, CaregiverEstimateRequest request);
}
