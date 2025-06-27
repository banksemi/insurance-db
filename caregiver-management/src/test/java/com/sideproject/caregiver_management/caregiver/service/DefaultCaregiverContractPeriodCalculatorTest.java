package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverDateUpdate;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.service.contract_period_calculator.DefaultCaregiverContractPeriodCalculatorImpl;
import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCaregiverContractPeriodCalculatorTest {
    DefaultCaregiverContractPeriodCalculatorImpl calculator = new DefaultCaregiverContractPeriodCalculatorImpl();
    Insurance insurance;

    @BeforeEach
    void setUp() {
        insurance = new Insurance(User.builder().build());
        insurance.updateInsurance(InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2022, 6, 1))
                .endDate(LocalDate.of(2023, 6, 1))
                .personalInsuranceFee(1000L)
                .sharedInsuranceFee(10000L)
                .build());
    }

    @Test
    @DisplayName("계약일수 계산 - 보험 시작일부터 보험 종료일까지")
    void getContractDays_calculatesCorrectly() {
        // given
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2022, 6, 1)
        ));

        // when
        Long contractDays = calculator.getContractDays(caregiver);

        // then
        assertEquals(365, contractDays);
    }

    @Test
    @DisplayName("계약일수 계산 - 중간에 시작한 경우")
    void getContractDays_midTermJoin() {
        // given
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2022, 8, 1)
        ));

        // when
        Long contractDays = calculator.getContractDays(caregiver);

        // then
        assertEquals(304, contractDays); // 2022-08-01 ~ 2023-06-01
    }

    @Test
    @DisplayName("실제 사용일수 계산 - 종료일이 없는 경우 실제 사용 일수는 Empty 값이 반환되어야함.")
    void getEffectiveDays_noEndDate() {
        // given
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2022, 8, 1)
        ));

        // when
        Optional<Long> effectiveDays = calculator.getEffectiveDays(caregiver);

        // then
        assertEquals(Optional.empty(), effectiveDays);
    }

    @Test
    @DisplayName("실제 사용일수 계산 - 종료일이 있는 경우")
    void getEffectiveDays_withEndDate() {
        // given
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.of(
                LocalDate.of(2022, 8, 1),
                LocalDate.of(2022, 9, 1)
        ));

        // when
        Optional<Long> effectiveDays = calculator.getEffectiveDays(caregiver);

        // then
        assertEquals(Optional.of(31L), effectiveDays); // 2022-08-01 ~ 2022-09-01
    }

    @Test
    @DisplayName("실제 사용일수 계산 - 당일 해지 (0일)")
    void getEffectiveDays_sameDayStartAndEnd() {
        // given
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.of(
                LocalDate.of(2022, 7, 1),
                LocalDate.of(2022, 7, 1)
        ));

        // when
        Optional<Long> effectiveDays = calculator.getEffectiveDays(caregiver);

        // then
        assertEquals(Optional.of(0L), effectiveDays);
    }

    @Test
    @DisplayName("윤년이 있는 경우 계약일수 계산 (366일)")
    void getContractDays_leapYear() {
        // given
        insurance.updateInsurance(InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2023, 6, 1))
                .endDate(LocalDate.of(2024, 6, 1))
                .personalInsuranceFee(100000L)
                .build());

        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2023, 6, 1)
        ));

        // when
        Long contractDays = calculator.getContractDays(caregiver);

        // then
        assertEquals(366, contractDays); // 2023-06-01 ~ 2024-06-01 (윤년 포함)
    }

    @Test
    @DisplayName("보험 계약일 검사")
    void insuranceDate() {
        insurance.updateInsurance(InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2024, 6, 1))
                .endDate(LocalDate.of(2025, 6, 1))
                .personalInsuranceFee(100000L)
                .build());

        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2024, 6, 11)
        ));
        Long contractDays = calculator.getContractDays(caregiver);
        Optional<Long> effectiveInsuranceDays = calculator.getEffectiveDays(caregiver);
        caregiver.setDate(CaregiverDateUpdate.ofEndDate(
                LocalDate.of(2024, 6, 21)
        ));
        Optional<Long> effectiveInsuranceDaysWithEndDate = calculator.getEffectiveDays(caregiver);

        assertEquals(355, contractDays);
        assertEquals(Optional.empty(), effectiveInsuranceDays);
        assertEquals(Optional.of(10L), effectiveInsuranceDaysWithEndDate);
    }
}
