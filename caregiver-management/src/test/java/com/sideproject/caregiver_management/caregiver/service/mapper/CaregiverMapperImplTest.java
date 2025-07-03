package com.sideproject.caregiver_management.caregiver.service.mapper;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverResponse;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.service.CaregiverStatusService;
import com.sideproject.caregiver_management.caregiver.service.contract_period_calculator.CaregiverContractPeriodCalculator;
import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaregiverMapperImplTest {

    @Mock
    private CaregiverContractPeriodCalculator contractPeriodCalculator;

    @Mock
    private CaregiverStatusService statusService;

    private CaregiverMapperImpl caregiverMapper;

    @BeforeEach
    void setUp() {
        caregiverMapper = new CaregiverMapperImpl(contractPeriodCalculator, statusService);
    }

    @Test
    @DisplayName("Caregiver 엔티티를 CaregiverResponse DTO로 정상 변환")
    void toDTO_SingleCaregiver_Success() {
        // given
        Insurance insurance = new Insurance(User.builder().build());
        insurance.updateInsurance(InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .personalInsuranceFee(100000L)
                .sharedInsuranceFee(200000L)
                .policyNumber("1112111")
                .build());

        Caregiver caregiver = Caregiver.builder()
                .id(1L)
                .insurance(insurance)
                .name("테스트 이름")
                .isShared(false)
                .birthday(LocalDate.of(1990, 5, 15))
                .genderCode(1)
                .startDate(LocalDate.of(2024, 6, 1))
                .endDate(LocalDate.of(2024, 11, 30))
                .memo("테스트 메모")
                .insuranceAmount(100000L)
                .refundAmount(50000L)
                .createdAt(LocalDateTime.of(2024, 5, 1, 10, 0))
                .isApproved(true)
                .build();

        Long expectedContractDays = 183L;
        Long expectedEffectiveDays = 150L;
        String expectedStatus = "근무";

        when(contractPeriodCalculator.getContractDays(caregiver)).thenReturn(expectedContractDays);
        when(contractPeriodCalculator.getEffectiveDays(caregiver)).thenReturn(Optional.of(expectedEffectiveDays));
        when(statusService.getReadableStatus(caregiver)).thenReturn(expectedStatus);

        // when
        CaregiverResponse result = caregiverMapper.toDTO(caregiver);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("테스트 이름", result.getName());
        assertEquals(false, result.getIsShared());
        assertEquals(LocalDate.of(1990, 5, 15), result.getBirthday());
        assertEquals(1, result.getGenderCode());
        assertEquals(LocalDate.of(2024, 6, 1), result.getStartDate());
        assertEquals(LocalDate.of(2024, 11, 30), result.getEndDate());
        assertEquals("테스트 메모", result.getMemo());
        assertEquals(100000L, result.getInsuranceAmount());
        assertEquals(50000L, result.getRefundAmount());
        assertEquals(expectedContractDays, result.getContractDays());
        assertEquals(expectedEffectiveDays, result.getEffectiveDays());
        assertEquals(LocalDateTime.of(2024, 5, 1, 10, 0), result.getCreatedAt());
        assertEquals(true, result.getIsApproved());
        assertEquals(expectedStatus, result.getStatus());

        // verify
        verify(contractPeriodCalculator).getContractDays(caregiver);
        verify(contractPeriodCalculator).getEffectiveDays(caregiver);
        verify(statusService).getReadableStatus(caregiver);
    }

    @Test
    @DisplayName("effectiveDays가 Optional.empty()인 경우 null로 변환")
    void toDTO_EffectiveDaysEmpty_ShouldReturnNull() {
        // given
        Insurance insurance = new Insurance(User.builder().build());
        insurance.updateInsurance(InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .personalInsuranceFee(100000L)
                .sharedInsuranceFee(200000L)
                .policyNumber("11121112")
                .build());

        Caregiver caregiver = Caregiver.builder()
                .id(1L)
                .insurance(insurance)
                .name("테스트 이름")
                .isShared(true)
                .birthday(LocalDate.of(1985, 3, 20))
                .genderCode(0)
                .startDate(LocalDate.of(2024, 7, 1))
                .endDate(null)
                .memo(null)
                .insuranceAmount(200000L)
                .refundAmount(null)
                .createdAt(LocalDateTime.of(2024, 6, 15, 14, 30))
                .isApproved(false)
                .build();

        Long expectedContractDays = 90L;
        String expectedStatus = "근무예정";

        when(contractPeriodCalculator.getContractDays(caregiver)).thenReturn(expectedContractDays);
        when(contractPeriodCalculator.getEffectiveDays(caregiver)).thenReturn(Optional.empty());
        when(statusService.getReadableStatus(caregiver)).thenReturn(expectedStatus);

        // when
        CaregiverResponse result = caregiverMapper.toDTO(caregiver);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getEffectiveDays()); // Optional.empty()는 null로 변환

        // verify
        verify(contractPeriodCalculator).getContractDays(caregiver);
        verify(contractPeriodCalculator).getEffectiveDays(caregiver);
        verify(statusService).getReadableStatus(caregiver);
    }

    @Test
    @DisplayName("Caregiver 리스트를 CaregiverResponse 리스트로 정상 변환")
    void toDTO_CaregiverList_Success() {
        // given
        Insurance insurance = new Insurance(User.builder().build());
        insurance.updateInsurance(InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .personalInsuranceFee(100000L)
                .sharedInsuranceFee(200000L)
                .policyNumber("11121112")
                .build());

        Caregiver caregiver1 = Caregiver.builder()
                .id(1L)
                .insurance(insurance)
                .name("테스트 이름1")
                .isShared(false)
                .birthday(LocalDate.of(1990, 1, 1))
                .genderCode(1)
                .startDate(LocalDate.of(2024, 6, 1))
                .endDate(LocalDate.of(2024, 11, 30))
                .memo("메모1")
                .insuranceAmount(100000L)
                .refundAmount(50000L)
                .createdAt(LocalDateTime.of(2024, 5, 1, 10, 0))
                .isApproved(true)
                .build();

        Caregiver caregiver2 = Caregiver.builder()
                .id(2L)
                .insurance(insurance)
                .name("테스트 이름2")
                .isShared(true)
                .birthday(LocalDate.of(1985, 2, 2))
                .genderCode(0)
                .startDate(LocalDate.of(2024, 7, 1))
                .endDate(null)
                .memo("메모2")
                .insuranceAmount(150000L)
                .refundAmount(null)
                .createdAt(LocalDateTime.of(2024, 6, 1, 11, 0))
                .isApproved(false)
                .build();

        List<Caregiver> caregivers = Arrays.asList(caregiver1, caregiver2);

        // when
        when(contractPeriodCalculator.getContractDays(caregiver1)).thenReturn(183L);
        when(contractPeriodCalculator.getEffectiveDays(caregiver1)).thenReturn(Optional.of(150L));
        when(statusService.getReadableStatus(caregiver1)).thenReturn("근무");

        when(contractPeriodCalculator.getContractDays(caregiver2)).thenReturn(90L);
        when(contractPeriodCalculator.getEffectiveDays(caregiver2)).thenReturn(Optional.empty());
        when(statusService.getReadableStatus(caregiver2)).thenReturn("근무예정");
        List<CaregiverResponse> result = caregiverMapper.toDTO(caregivers);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());

        // 첫 번째 간병인 검증
        CaregiverResponse response1 = result.get(0);
        assertEquals(1L, response1.getId());
        assertEquals("테스트 이름1", response1.getName());
        assertEquals(false, response1.getIsShared());
        assertEquals(183L, response1.getContractDays());
        assertEquals(150L, response1.getEffectiveDays());
        assertEquals("근무", response1.getStatus());

        // 두 번째 간병인 검증
        CaregiverResponse response2 = result.get(1);
        assertEquals(2L, response2.getId());
        assertEquals("테스트 이름2", response2.getName());
        assertEquals(true, response2.getIsShared());
        assertEquals(90L, response2.getContractDays());
        assertNull(response2.getEffectiveDays());
        assertEquals("근무예정", response2.getStatus());

        // verify
        verify(contractPeriodCalculator, times(2)).getContractDays(any(Caregiver.class));
        verify(contractPeriodCalculator, times(2)).getEffectiveDays(any(Caregiver.class));
        verify(statusService, times(2)).getReadableStatus(any(Caregiver.class));
    }

    @Test
    @DisplayName("null 리스트 입력 시 빈 리스트 반환")
    void toDTO_NullList_ShouldReturnEmptyList() {
        // when
        List<CaregiverResponse> result = caregiverMapper.toDTO((List<Caregiver>) null);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("빈 리스트 입력 시 빈 리스트 반환")
    void toDTO_EmptyList_ShouldReturnEmptyList() {
        // given
        List<Caregiver> emptyList = Arrays.asList();

        // when
        List<CaregiverResponse> result = caregiverMapper.toDTO(emptyList);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }
}
