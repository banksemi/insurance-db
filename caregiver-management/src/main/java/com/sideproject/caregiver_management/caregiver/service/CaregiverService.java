package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.*;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverForbiddenException;
import com.sideproject.caregiver_management.caregiver.exception.NotFoundCaregiverException;
import com.sideproject.caregiver_management.common.dto.ListResponse;
import com.sideproject.caregiver_management.insurance.entity.Insurance;

import java.time.LocalDate;
import java.util.List;

public interface CaregiverService {
    ListResponse<CaregiverResponse> getCaregivers(Insurance insurance, CaregiverSearchCondition searchCondition);
    CaregiverResponse getCaregiver(Insurance insurance, Long caregiverId) throws NotFoundCaregiverException, CaregiverForbiddenException;
    Long addCaregiver(Insurance insurance, CaregiverCreateRequest request);
    void requestEndDate(Insurance insurance, Long caregiverId, LocalDate endDate, Boolean checkApproved) throws NotFoundCaregiverException, CaregiverForbiddenException;
    void updateMemo(Insurance insurance, Long caregiverId, String memo) throws NotFoundCaregiverException, CaregiverForbiddenException;

    CaregiverEstimateResponse getCaregiverEstimate(Insurance insurance, CaregiverEstimateRequest request);
}
