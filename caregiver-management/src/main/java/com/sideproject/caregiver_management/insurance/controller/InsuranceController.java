package com.sideproject.caregiver_management.insurance.controller;

import com.sideproject.caregiver_management.auth.annotation.AuthorizeUser;
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
@AuthorizeUser
public class InsuranceController {
    private final InsuranceService insuranceService;

    @GetMapping("/insurance")
    public InsuranceResponse getInsurance(@PathVariable("userId") Long userId) {
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
