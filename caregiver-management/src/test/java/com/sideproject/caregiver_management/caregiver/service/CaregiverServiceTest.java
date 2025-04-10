package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverResponse;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverSearchCondition;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverSortType;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverForbiddenException;
import com.sideproject.caregiver_management.common.dto.ListResponse;
import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.service.InsuranceService;
import com.sideproject.caregiver_management.user.dto.UserCreateRequest;
import com.sideproject.caregiver_management.user.service.TenantService;
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
        ListResponse<CaregiverResponse> result = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(CaregiverSortType.ID)
                        .build()
        );

        // then
        assertEquals(0, result.getCount());
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
        ListResponse<CaregiverResponse> sortedCaregivers = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(SORT_BY_ID)
                        .build()
        );

        // then
        assertEquals(3, sortedCaregivers.getCount());
        assertEquals(List.of(id1, id2, id3), sortedCaregivers.getData().stream().map(CaregiverResponse::getId).toList());
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
        ListResponse<CaregiverResponse> filteredCaregivers = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(SORT_BY_ID)
                        .isShared(true)
                        .build()
        );

        // then
        assertEquals(2, filteredCaregivers.getCount());
        assertEquals(List.of(id1, id3), filteredCaregivers.getData().stream().map(CaregiverResponse::getId).toList());
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
        ListResponse<CaregiverResponse> filteredCaregivers = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(SORT_BY_ID)
                        .isShared(false)
                        .build()
        );

        // then
        assertEquals(1, filteredCaregivers.getCount());
        assertEquals(List.of(id2), filteredCaregivers.getData().stream().map(CaregiverResponse::getId).toList());
    }

    @Test
    @DisplayName("간병인 목록 정렬 테스트 (이름순 정렬)")
    void getCaregivers_SortByName() {
        // given
        Insurance insurance = createInsurance();
        Long id1 = createCaregiver(insurance, "이름1", LocalDate.of(2024, 9, 3), true);
        Long id2 = createCaregiver(insurance, "이름3", LocalDate.of(2024, 9, 2), false);
        Long id3 = createCaregiver(insurance, "이름2", LocalDate.of(2024, 9, 2), true);
        Long id4 = createCaregiver(insurance, "이름4", LocalDate.of(2024, 9, 4), false);

        // when
        ListResponse<CaregiverResponse> sortedCaregivers = caregiverService.getCaregivers(
                insurance,
                CaregiverSearchCondition.builder()
                        .sortBy(CaregiverSortType.NAME)
                        .build()
        );

        // then
        assertEquals(4, sortedCaregivers.getCount());
        assertEquals(List.of(id1, id3, id2, id4), sortedCaregivers.getData().stream().map(CaregiverResponse::getId).toList());
    }

    @Test
    @DisplayName("간병인 메모 수정이 정상적으로 완료되어야함")
    void updateMemoSuccess() {
        // given
        Insurance insurance = createInsurance();
        Long id1 = createCaregiver(insurance, "이름1", LocalDate.of(2024, 9, 1), true);

        // when
        caregiverService.updateMemo(insurance, id1, "memo");

        // then
        CaregiverResponse caregiverResponse = caregiverService.getCaregiver(insurance, id1);
        assertEquals("memo", caregiverResponse.getMemo());
    }

    @Test
    @DisplayName("다른 보험에 속한 간병인 메모 수정은 CaregiverForbiddenException 가 반환되어야함")
    void updateMemoFailBecauseNotBelongToInsurance() {
        // given
        Insurance insurance = createInsurance();
        Insurance dummyInsurance = new Insurance(null);
        Long id1 = createCaregiver(insurance, "이름1", LocalDate.of(2024, 9, 1), true);

        // when, then
        assertThrows(
                CaregiverForbiddenException.class,
                ()->caregiverService.updateMemo(dummyInsurance, id1, "memo")
        );
    }

    @Test
    @DisplayName("간병인의 만료일을 지정하기 위해서는 기존 등록 데이터에 대한 승인을 받은 상태여야함.")
    void requestEndDateWithNotApproved() {
        Insurance insurance = createInsurance();
        Long id1 = createCaregiver(insurance, "이름1", LocalDate.of(2024, 9, 1), true);

        assertThrows(
                CaregiverForbiddenException.class,
                ()->caregiverService.requestEndDate(id1, LocalDate.of(2024, 9, 1), true)
        );
    }
}