package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverResponse;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverDuplicateException;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverStartDateBeforeNowException;
import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.service.InsuranceService;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.service.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CaregiverServiceCreateTest {
    @MockitoBean
    private Clock clock;

    @Autowired
    private CaregiverService caregiverService;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private TenantService tenantService;

    private Insurance insurance;

    @BeforeEach
    void setUp() {
        LocalDate fakeToday = LocalDate.of(2024, 9, 1);
        Clock fixedClock = Clock.fixed(
                fakeToday.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long tenantId = tenantService.createTenant("Tenant");
        Long userId = tenantService.createUser(tenantId,
                UserCreateRequest.builder()
                        .loginId("loginId")
                        .password("password")
                        .name("name")
                        .build());
        Long insuranceId = insuranceService.createInsurance(userId, InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2024, 6, 1))
                .endDate(LocalDate.of(2025, 6, 1))
                .policyNumber("1")
                .personalInsuranceFee(1000L)
                .sharedInsuranceFee(10000L)
                .build());
        insurance = insuranceService.getInsuranceById(insuranceId);
    }

    private CaregiverCreateRequest createRequest(LocalDate startDate) {
        return CaregiverCreateRequest.builder()
                .startDate(startDate)
                .name("이름")
                .birthday(LocalDate.of(2000, 1, 1))
                .genderCode(1)
                .isShared(false)
                .build();
    }

    @Test
    @DisplayName("간병인 생성 테스트 (Default)")
    void createCaregiver_success() {
        Long caregiverId = caregiverService.addCaregiver(
                insurance, createRequest(LocalDate.of(2024, 9, 1)));
        assertNotNull(caregiverId);
    }

    @Test
    @DisplayName("동일한 간병인 정보를 중복으로 등록할 수 없음")
    void createCaregiver_duplicateCaregiver_throwsException() {
        caregiverService.addCaregiver(insurance, createRequest(LocalDate.of(2024, 9, 1)));
        assertThrows(CaregiverDuplicateException.class, ()->caregiverService.addCaregiver(
                insurance,
                createRequest(LocalDate.of(2024, 9, 1))
        ));
    }

    @Test
    @DisplayName("간병인 보험이 예약된 상태에서는 동일한 간병인 정보를 등록할 수 없음")
    void createCaregiver_duplicateEvenIfOnlyReserved_throwsException() {
        CaregiverCreateRequest request = CaregiverCreateRequest.builder()
                .startDate(LocalDate.of(2024, 9, 2))
                .name("이름")
                .birthday(LocalDate.of(2000, 1, 1))
                .genderCode(1)
                .isShared(false)
                .build();
        caregiverService.addCaregiver(insurance, createRequest(LocalDate.of(2024, 9, 2)));

        request.setStartDate(LocalDate.of(2024, 9, 1));
        assertThrows(CaregiverDuplicateException.class, ()->caregiverService.addCaregiver(insurance, request));

        request.setStartDate(LocalDate.of(2024, 10, 1));
        assertThrows(CaregiverDuplicateException.class, ()->caregiverService.addCaregiver(insurance, request));
    }

    @Test
    @DisplayName("간병인의 보험 해지일 이후로는 등록 가능")
    void createCaregiver_afterEndDate_success() {
        CaregiverCreateRequest request = CaregiverCreateRequest.builder()
                .startDate(LocalDate.of(2024, 9, 10))
                .name("이름")
                .birthday(LocalDate.of(2000, 1, 1))
                .genderCode(1)
                .isShared(false)
                .build();
        Long caregiverId = caregiverService.addCaregiver(insurance, request);
        caregiverService.requestEndDate(caregiverId, LocalDate.of(2024, 10, 1));

        request.setStartDate(LocalDate.of(2024, 9, 9));
        assertThrows(CaregiverDuplicateException.class, ()->caregiverService.addCaregiver(insurance, request));

        request.setStartDate(LocalDate.of(2024, 10, 2));
        caregiverService.addCaregiver(insurance, request);
    }

    @Test
    @DisplayName("간병인의 보험 해지일에 바로 동일한 간병인의 보험을 시작할 수 있음")
    void createCaregiver_onEndDate_success() {
        CaregiverCreateRequest request = CaregiverCreateRequest.builder()
                .startDate(LocalDate.of(2024, 9, 10))
                .name("이름")
                .birthday(LocalDate.of(2000, 1, 1))
                .genderCode(1)
                .isShared(false)
                .build();
        Long caregiverId = caregiverService.addCaregiver(insurance, request);
        caregiverService.requestEndDate(caregiverId, LocalDate.of(2024, 10, 1));

        request.setStartDate(LocalDate.of(2024, 10, 1));
        caregiverService.addCaregiver(insurance, request);
    }

    @Test
    @DisplayName("오늘 이전으로는 간병인 보험을 등록할 수 없음")
    void createCaregiver_beforeToday_throwsException() {
        CaregiverCreateRequest request = CaregiverCreateRequest.builder()
                .startDate(LocalDate.of(2024, 8, 31))
                .name("이름")
                .birthday(LocalDate.of(2000, 1, 1))
                .genderCode(1)
                .isShared(false)
                .build();

        assertThrows(CaregiverStartDateBeforeNowException.class, ()->caregiverService.addCaregiver(insurance, request));
    }

    @Test
    @DisplayName("개인 간병과 공동 간병 그룹끼리는 중복 체크를 하지 않아야함")
    void createCaregiver_differentGroupType_noDuplicateCheck() {
        CaregiverCreateRequest request = createRequest(LocalDate.of(2024, 9, 1));
        request.setIsShared(false);
        caregiverService.addCaregiver(insurance, request);

        request.setIsShared(true);
        caregiverService.addCaregiver(insurance, request);

        request.setIsShared(false);
        assertThrows(CaregiverDuplicateException.class, ()->caregiverService.addCaregiver(
                insurance,
                request
        ));
    }

    @Test
    @DisplayName("이름, 생년월일, 성별이 다른 경우 중복으로 취급하지 않음")
    void createCaregiver_differentField() {
        CaregiverCreateRequest request = createRequest(LocalDate.of(2024, 9, 1));
        request.setGenderCode(0);
        request.setName("이름");
        request.setBirthday(LocalDate.of(2024, 10, 1));
        caregiverService.addCaregiver(insurance, request);

        request.setName("이름2");
        caregiverService.addCaregiver(insurance, request);

        request.setBirthday(LocalDate.of(2024, 10, 2));
        caregiverService.addCaregiver(insurance, request);

        request.setGenderCode(1);
        caregiverService.addCaregiver(insurance, request);
    }

    @Test
    @DisplayName("간병인 등록시 보험료가 계산되는지 확인")
    void createCaregiver_insuranceAmount() {
        Long caregiverId = caregiverService.addCaregiver(
                insurance, createRequest(LocalDate.of(2025, 5, 1)));
        assertNotNull(caregiverId);

        CaregiverResponse caregiver = caregiverService.getCaregiver(insurance, caregiverId);
        assertEquals(caregiver.getInsuranceAmount(), 85);
        assertNull(caregiver.getRefundAmount());
    }
}
