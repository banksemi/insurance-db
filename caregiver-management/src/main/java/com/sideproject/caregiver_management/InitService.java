package com.sideproject.caregiver_management;

import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.service.InsuranceService;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.exception.DuplicateResourceException;
import com.sideproject.caregiver_management.user.service.TenantService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Profile({"dev", "prod"})
@Service
public class InitService {
    private final TenantService tenantService;
    private final InsuranceService insuranceService;
    @PostConstruct
    public void init(){
        System.out.println("InitService is called");
        try {
            Long tenantId = tenantService.createTenant("DEMO Tenant");
            Long userId = tenantService.createUser(tenantId, UserCreateRequest
                    .builder()
                    .loginId("admin")
                    .password("admin12")
                    .name("admin")
                    .build()
            );
            tenantService.findUserById(userId).setIsAdmin(true);
            Long userId2 = tenantService.createUser(tenantId, UserCreateRequest
                    .builder()
                    .loginId("test")
                    .password("test12")
                    .name("test")
                    .build()
            );
            insuranceService.createInsurance(userId2,
                    InsuranceUpdateRequest.builder()
                            .startDate(LocalDate.now())
                            .endDate(LocalDate.now())
                            .policyNumber("1234")
                            .personalInsuranceFee(10000L)
                            .sharedInsuranceFee(100000L)
                            .build()
            );
        } catch (DuplicateResourceException e) {

        }
    }
}
