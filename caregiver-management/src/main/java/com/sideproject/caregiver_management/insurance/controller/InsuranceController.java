package com.sideproject.caregiver_management.insurance.controller;

import com.sideproject.caregiver_management.auth.annotation.Auth;
import com.sideproject.caregiver_management.auth.service.AuthorizationService;
import com.sideproject.caregiver_management.insurance.dto.InsuranceResponse;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}")
@RequiredArgsConstructor
public class InsuranceController {
    private final InsuranceService insuranceService;
    private final AuthorizationService authorizationService;

    @GetMapping("/insurance")
    @Auth(Auth.Role.ROLE_USER)
    public InsuranceResponse getInsurance(@PathVariable("userId") Long userId) {
        authorizationService.validateAccessToUser(userId);
        Insurance insurance = insuranceService.getInsuranceByUserId(userId);
        return InsuranceResponse.builder()
                .startDate(insurance.getStartDate())
                .endDate(insurance.getEndDate())
                .personalInsuranceFee(insurance.getPersonalInsuranceFee())
                .sharedInsuranceFee(insurance.getSharedInsuranceFee())
                .policyNumber(insurance.getPolicyNumber())
                .build();
    }

}
