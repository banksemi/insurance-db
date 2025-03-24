package com.sideproject.caregiver_management.caregiver.controller;

import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.auth.service.AuthorizationService;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverDateUpdate;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverEstimateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverEstimateResponse;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.service.CaregiverService;
import com.sideproject.caregiver_management.caregiver.service.amount.CaregiverInsuranceAmountCalculator;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.service.InsuranceService;
import com.sideproject.caregiver_management.user.dto.TenantPublicInformation;
import com.sideproject.caregiver_management.user.entity.Tenant;
import com.sideproject.caregiver_management.user.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{userId}/insurance/caregivers")
@RequiredArgsConstructor
public class CaregiverEstimateController {
    private final CaregiverService caregiverService;
    private final InsuranceService insuranceService;
    private final AuthorizationService authorizationService;

    @GetMapping("/estimate")
    @Auth(Auth.Role.ROLE_USER)
    public CaregiverEstimateResponse getEstimation(@PathVariable("userId") Long userId, @Valid CaregiverEstimateRequest request) {
        authorizationService.validateAccessToUser(userId);
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);
        return caregiverService.getCaregiverEstimate(insurance, request);
    }
}
