package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverSearchCondition;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverSortType;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CaregiverServiceTest {
    @MockitoBean
    private Clock clock;

    @Autowired
    private CaregiverService caregiverService;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private TenantService tenantService;

    Insurance createInsurance() {
        LocalDate fakeToday = LocalDate.of(2024, 9, 1);
        Clock fixedClock = Clock.fixed(
                fakeToday.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());

        Long tenantId = tenantService.createTenant("Tenent");
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

        return insuranceService.getInsuranceById(insuranceId);
    }

    private Long createCaregiver(Insurance insurance, String name, LocalDate startDate, Boolean isShared) {
        return caregiverService.addCaregiver(insurance, CaregiverCreateRequest.builder()
                .isShared(isShared)
                .genderCode(0)
                .birthday(LocalDate.of(2000, 1, 1))
                .name(name)
                .startDate(startDate)
                .build());
    }

    @Test
    @DisplayName("간병인이 없을 때에는 빈 배열이 반환되어야함")
    void getCaregivers_Empty() {
        // given
        Insurance insurance = createInsurance();

        // when
        List<Caregiver> result = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(CaregiverSortType.ID)
                        .build()
        );

        // then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("정렬 순서 테스트 (ID 등록순)")
    void getCaregivers_SortingID() {
        // given
        Insurance insurance = createInsurance();
        Long id1 = createCaregiver(insurance, "이름1", LocalDate.of(2024, 9, 1), false);
        Long id2 = createCaregiver(insurance, "이름3", LocalDate.of(2024, 9, 3), false);
        Long id3 = createCaregiver(insurance, "이름2", LocalDate.of(2024, 9, 2), false);
        final CaregiverSortType SORT_BY_ID = CaregiverSortType.ID;

        // when
        List<Caregiver> sortedCaregivers = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(SORT_BY_ID)
                        .build()
        );

        // then
        assertEquals(3, sortedCaregivers.size());
        assertEquals(List.of(id1, id2, id3), sortedCaregivers.stream().map(Caregiver::getId).toList());
    }

    @Test
    @DisplayName("공동 간병인만 보기")
    void getCaregivers_FilterShared() {
        // given
        Insurance insurance = createInsurance();
        Long id1 = createCaregiver(insurance, "이름1", LocalDate.of(2024, 9, 1), true);
        Long id2 = createCaregiver(insurance, "이름3", LocalDate.of(2024, 9, 3), false);
        Long id3 = createCaregiver(insurance, "이름2", LocalDate.of(2024, 9, 2), true);
        final CaregiverSortType SORT_BY_ID = CaregiverSortType.ID;

        // when
        List<Caregiver> filteredCaregivers = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(SORT_BY_ID)
                        .isShared(true)
                        .build()
        );

        // then
        assertEquals(2, filteredCaregivers.size());
        assertEquals(List.of(id1, id3), filteredCaregivers.stream().map(Caregiver::getId).toList());
    }

    @Test
    @DisplayName("개인 간병인만 보기")
    void getCaregivers_FilterPersonal() {
        // given
        Insurance insurance = createInsurance();
        Long id1 = createCaregiver(insurance, "이름1", LocalDate.of(2024, 9, 1), true);
        Long id2 = createCaregiver(insurance, "이름3", LocalDate.of(2024, 9, 3), false);
        Long id3 = createCaregiver(insurance, "이름2", LocalDate.of(2024, 9, 2), true);
        final CaregiverSortType SORT_BY_ID = CaregiverSortType.ID;

        // when
        List<Caregiver> filteredCaregivers = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(SORT_BY_ID)
                        .isShared(false)
                        .build()
        );

        // then
        assertEquals(1, filteredCaregivers.size());
        assertEquals(List.of(id2), filteredCaregivers.stream().map(Caregiver::getId).toList());
    }
}