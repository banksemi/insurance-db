package com.sideproject.caregiver_management.caregiver.controller;

import com.sideproject.caregiver_management.auth.annotation.AuthorizeUser;
import com.sideproject.caregiver_management.caregiver.dto.*;
import com.sideproject.caregiver_management.caregiver.service.CaregiverService;
import com.sideproject.caregiver_management.common.dto.ListResponse;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.service.InsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{userId}/insurance/caregivers")
@RequiredArgsConstructor
@AuthorizeUser
public class CaregiverController {
    private final CaregiverService caregiverService;
    private final InsuranceService insuranceService;

    @PostMapping("")
    public CaregiverCreateResponse addCaregiver(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid CaregiverCreateRequest request) {
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);
        Long caregiverId = caregiverService.addCaregiver(insurance, request);
        return CaregiverCreateResponse.builder().id(caregiverId).build();
    }

    @GetMapping("")
    public ListResponse<CaregiverResponse> getCaregivers(@PathVariable("userId") Long userId, @Valid CaregiverSearchCondition searchCondition) {
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);

        return caregiverService.getCaregivers(insurance, searchCondition);
    }

    @GetMapping("/estimate")
    public CaregiverEstimateResponse getEstimation(@PathVariable("userId") Long userId, @Valid CaregiverEstimateRequest request) {
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);
        return caregiverService.getCaregiverEstimate(insurance, request);
    }

    @PatchMapping("/{caregiverId}/memo")
    public CaregiverResponse updateMemo(
            @PathVariable("userId") Long userId,
            @PathVariable("caregiverId") Long caregiverId,
            @RequestBody @Valid CaregiverUpdateMemoRequest request) {
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);
        caregiverService.updateMemo(insurance, caregiverId, request.getText());
        return caregiverService.getCaregiver(insurance, caregiverId);
    }


    @PatchMapping("/{caregiverId}/end-date")
    public CaregiverResponse updateEndDate(
            @PathVariable("userId") Long userId,
            @PathVariable("caregiverId") Long caregiverId,
            @RequestBody @Valid CaregiverUpdateEndDateRequest request) {
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);
        caregiverService.requestEndDate(insurance, caregiverId, request.getEndDate(), true);
        return caregiverService.getCaregiver(insurance, caregiverId);
    }
}
