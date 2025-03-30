package com.sideproject.caregiver_management.caregiver.controller;

import com.sideproject.caregiver_management.auth.service.AuthorizationService;
import com.sideproject.caregiver_management.caregiver.dto.*;
import com.sideproject.caregiver_management.caregiver.service.CaregiverService;
import com.sideproject.caregiver_management.common.dto.ListResponse;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.insurance.service.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaregiverControllerTest {

    @InjectMocks
    private CaregiverController caregiverController;

    @Mock
    private InsuranceService insuranceService;

    @Mock
    private CaregiverService caregiverService;

    @Mock
    private AuthorizationService authorizationService;

    private final Long userId = 1L;
    private final Insurance insurance = new Insurance(null);

    @BeforeEach
    void setUp() {
        when(insuranceService.getInsuranceByUserId(userId)).thenReturn(insurance);
    }
    @Test
    @DisplayName("간병인 목록 조회 컨트롤러 검증")
    void shouldReturnCaregiverList() {
        // given
        CaregiverSearchCondition searchCondition = new CaregiverSearchCondition();
        when(caregiverService.getCaregivers(insurance, searchCondition)).thenReturn(
                ListResponse.<CaregiverResponse>builder()
                        .count(10)
                        .data(List.of(CaregiverResponse.builder()
                                .id(100L)
                                .build()))
                        .build()
        );

        // when
        var response = caregiverController.getCaregivers(userId, searchCondition);

        // then
        verify(authorizationService).validateAccessToUser(userId); // 권한을 검사하는 로직이 포함되어있는지 확인
        verify(caregiverService).getCaregivers(insurance, searchCondition); // 서비스를 호출하여 결과를 얻는지 검증

        assertEquals(10, response.getCount()); // 예상된 서비스의 결과가 반환되는지 확인
        assertEquals(100L, response.getData().getFirst().getId());
    }

    @Test
    @DisplayName("간병인 등록 컨트롤러 검증")
    void shouldAddCaregiver() {
        // given
        CaregiverCreateRequest request = new CaregiverCreateRequest();
        when(caregiverService.addCaregiver(insurance, request)).thenReturn(100L);

        // when
        CaregiverCreateResponse response = caregiverController.addCaregiver(userId, request);

        // then
        verify(authorizationService).validateAccessToUser(userId); // 권한을 검사하는 로직이 포함되어있는지 확인
        verify(caregiverService).addCaregiver(insurance, request); // 서비스를 호출하여 결과를 얻는지 검증
        assertEquals(100L, response.getId()); // 결과 확인
    }

    @Test
    @DisplayName("간병인 예상 보험금 조회 컨트롤러 검증")
    void shouldReturnEstimatedInsuranceAmount() {
        // given
        CaregiverEstimateRequest request = new CaregiverEstimateRequest();
        when(caregiverService.getCaregiverEstimate(insurance, request)).thenReturn(
                CaregiverEstimateResponse.builder()
                        .insuranceAmount(100L)
                        .build()
        );

        // when
        CaregiverEstimateResponse response = caregiverController.getEstimation(userId, request);

        // then
        verify(authorizationService).validateAccessToUser(userId); // 권한을 검사하는 로직이 포함되어있는지 확인
        verify(caregiverService).getCaregiverEstimate(insurance, request); // 서비스를 호출하여 결과를 얻는지 검증
        assertEquals(100L, response.getInsuranceAmount()); // 결과 확인
    }
}