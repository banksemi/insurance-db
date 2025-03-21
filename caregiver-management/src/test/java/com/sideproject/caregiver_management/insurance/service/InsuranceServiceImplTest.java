package com.sideproject.caregiver_management.insurance.service;

import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.exception.DuplicateInsuranceException;
import com.sideproject.caregiver_management.insurance.exception.NotFoundInsuranceException;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.exception.NotFoundUserException;
import com.sideproject.caregiver_management.user.service.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InsuranceServiceImplTest {
    @Autowired
    private InsuranceServiceImpl insuranceService;

    @Autowired
    private TenantService tenantService;

    private Long defaultUserId;

    private InsuranceUpdateRequest defaultRequest = InsuranceUpdateRequest.builder()
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .policyNumber("123")
            .personalInsuranceFee(1000L)
            .sharedInsuranceFee(1000L)
            .build();

    @BeforeEach
    void setUp() {
        Long tenantId = tenantService.createTenant("Tenent");
        defaultUserId = tenantService.createUser(tenantId,
                UserCreateRequest.builder()
                        .loginId("loginId")
                        .password("password")
                        .name("name")
                        .build());
    }
    @Test
    void createInsurance() {
        insuranceService.createInsurance(defaultUserId, defaultRequest);
    }

    @Test
    void createInsurance_ThrowsNotFoundUserException() {
        assertThrows(NotFoundUserException.class, () -> {insuranceService.createInsurance(0L, defaultRequest);});
    }

    @Test
    void createInsurance_ThrowsDuplicateInsuranceException() {
        insuranceService.createInsurance(defaultUserId, defaultRequest);

        assertThrows(DuplicateInsuranceException.class, () -> {insuranceService.createInsurance(defaultUserId, defaultRequest);});
    }

    @Test
    void getInsurance() {
        insuranceService.createInsurance(defaultUserId, defaultRequest);

        Insurance insurance = insuranceService.getInsurance(defaultUserId);
        assertEquals(insurance.getUser().getId(), defaultUserId);
        assertEquals(insurance.getPolicyNumber(), defaultRequest.getPolicyNumber());
        assertEquals(insurance.getPersonalInsuranceFee(), defaultRequest.getPersonalInsuranceFee());
        assertEquals(insurance.getSharedInsuranceFee(), defaultRequest.getSharedInsuranceFee());
        assertEquals(insurance.getStartDate(), defaultRequest.getStartDate());
        assertEquals(insurance.getEndDate(), defaultRequest.getEndDate());
    }

    @Test
    void getInsurance_ThrowsNotFoundInsuranceException() {
        assertThrows(NotFoundInsuranceException.class, ()->insuranceService.getInsurance(-1L));
        assertThrows(NotFoundInsuranceException.class, ()->insuranceService.getInsurance(defaultUserId));
    }

    @Test
    void updateInsurance() {
        insuranceService.createInsurance(defaultUserId, defaultRequest);

        insuranceService.updateInsurance(defaultUserId, InsuranceUpdateRequest.builder()
                .startDate(LocalDate.now().minusDays(1))
                .build());

        insuranceService.updateInsurance(defaultUserId,
                InsuranceUpdateRequest.builder()
                        .endDate(LocalDate.now().plusDays(1))
                        .personalInsuranceFee(1L)
                        .sharedInsuranceFee(2L)
                        .policyNumber("333")
                .build());

        assertEquals(insuranceService.getInsurance(defaultUserId).getStartDate(), LocalDate.now().minusDays(1));
        assertEquals(insuranceService.getInsurance(defaultUserId).getEndDate(), LocalDate.now().plusDays(1));
        assertEquals(insuranceService.getInsurance(defaultUserId).getPolicyNumber(), "333");
        assertEquals(insuranceService.getInsurance(defaultUserId).getPersonalInsuranceFee(), 1L);
        assertEquals(insuranceService.getInsurance(defaultUserId).getSharedInsuranceFee(), 2L);
    }
}