package com.sideproject.caregiver_management.caregiver.controller;

import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.auth.service.AuthorizationService;
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
public class CaregiverController {
    private final CaregiverService caregiverService;
    private final InsuranceService insuranceService;
    private final AuthorizationService authorizationService;

    @PostMapping("")
    @Auth(Auth.Role.ROLE_USER)
    public CaregiverCreateResponse addCaregiver(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid CaregiverCreateRequest request) {
        authorizationService.validateAccessToUser(userId);
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);
        Long caregiverId = caregiverService.addCaregiver(insurance, request);
        return CaregiverCreateResponse.builder().caregiverId(caregiverId).build();
    }

    @GetMapping("")
    @Auth(Auth.Role.ROLE_USER)
    public ListResponse<CaregiverResponse> getCaregivers(@PathVariable("userId") Long userId, @Valid CaregiverSearchCondition searchCondition) {
        authorizationService.validateAccessToUser(userId);
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);

        return caregiverService.getCaregivers(insurance, searchCondition);
    }
}
