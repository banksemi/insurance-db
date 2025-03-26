package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverDateUpdate;
import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import com.sideproject.caregiver_management.caregiver.service.amount.BasicCaregiverInsuranceAmountCalculator;
import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BasicCaregiverInsuranceAmountCalculatorTest {
    BasicCaregiverInsuranceAmountCalculator calculator = new BasicCaregiverInsuranceAmountCalculator();
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
    @DisplayName("보험 최대 범위로 설정시 간병인 보험료는 보험 기준 금액과 동일")
    void fullInsurancePeriod_calculatesCorrectAmountAndNoRefund() {
        // given
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2022, 6, 1)
        ));

        // when
        caregiver.calculateAmounts(calculator);

        // then
        assertEquals(1000L, caregiver.getInsuranceAmount()); // 보험료와 일치
        assertNull(caregiver.getRefundAmount()); // 종료일이 없기 때문에 환불액은 결정되지 않음
    }

    @Test
    @DisplayName("마지막 날까지 사용시 환불액은 0원으로 처리되어야함")
    void lastDayUsage_resultsInZeroRefund() {
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.of(
                LocalDate.of(2022, 6, 1),
                LocalDate.of(2023, 6, 1)
        ));

        caregiver.calculateAmounts(calculator);

        assertEquals(1000L, caregiver.getInsuranceAmount());
        assertEquals(0L, caregiver.getRefundAmount());
    }

    @Test
    @DisplayName("공동 간병인은 공동 간병인 보험으로 보험료가 계산되어야함")
    void sharedCaregiver() {
        Caregiver caregiver = new Caregiver(insurance, true);
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2022, 6, 1)
        ));

        caregiver.calculateAmounts(calculator);

        assertEquals(10000L, caregiver.getInsuranceAmount());
        assertNull(caregiver.getRefundAmount()); // 종료일이 없기 때문에 환불액은 결정되지 않음
    }

    @Test
    @DisplayName("당일 해지시 보험료는 청구되지 않아야함")
    void sameDayStartAndEnd_resultsInZeroInsuranceFee() {
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.of(
                LocalDate.of(2022, 7 ,1),
                LocalDate.of(2022,7,1)
        ));

        caregiver.calculateAmounts(calculator);

        assertEquals(0L, caregiver.getInsuranceAmount());
        assertEquals(0L, caregiver.getRefundAmount());
    }

    @Test
    @DisplayName("중간에 가입한 간병인의 보험료 계산 로직 확인")
    void midTermJoin_calculatesProratedInsuranceFee() {
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2022, 8, 1)
        ));

        caregiver.calculateAmounts(calculator);

        assertEquals(833, caregiver.getInsuranceAmount());
        assertNull(caregiver.getRefundAmount());
    }

    @Test
    @DisplayName("보험 환불액 계산 로직 확인")
    void refundCalculation_basedOnUnusedPeriod() {
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.of(
                LocalDate.of(2022, 8, 1),
                LocalDate.of(2022, 9, 1)
        ));

        caregiver.calculateAmounts(calculator);

        assertEquals(833, caregiver.getInsuranceAmount()); // 간병인 보험 금액은 병원 보험의 마지막날까지를 전제로 계산되어야함
        assertEquals(748, caregiver.getRefundAmount()); // 사용하지 않은 금액에 대해서는 환불
    }

    @Test
    @DisplayName("보험 환불액 계산시 반올림된 보험료 기준으로 계산되어야함")
    void refundAmount_usesRoundedInsuranceAmountAsBase() {
        insurance.updateInsurance(InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2023, 6, 1))
                .endDate(LocalDate.of(2024, 6, 1))
                .build());
        Caregiver caregiver = new Caregiver(insurance, false);
        caregiver.setDate(CaregiverDateUpdate.of(
                LocalDate.of(2023, 8, 1),
                LocalDate.of(2023, 9, 1)
        ));
        // 윤년 계산으로 반올림 유도
        caregiver.calculateAmounts(calculator);

        // 간병인 보험 금액은 병원 보험의 마지막날까지를 전제로 계산되어야함
        assertEquals(833, caregiver.getInsuranceAmount());

        // 사용하지 않은 금액에 대해서는 환불 (참고: 반올림 처리된 833원 기준으로 환불액을 측정하므로 749원이 아닌 748원으로 입력되어야함)
        assertEquals(748, caregiver.getRefundAmount());
    }


    @Test
    @DisplayName("윤년이 있으면 366일로 보험료가 계산되어야함")
    void leapYear_calculatesInsuranceUsing366Days() {
        insurance.updateInsurance(InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2023, 6, 1))
                .endDate(LocalDate.of(2024, 6, 1))
                        .personalInsuranceFee(100000L)
                .build());

        Caregiver caregiver = new Caregiver(insurance, false);
        CaregiverDateUpdate dateUpdate = new CaregiverDateUpdate();
        dateUpdate.setStartDate(LocalDate.of(2023, 8, 1));
        dateUpdate.setEndDate(LocalDate.of(2023, 9, 1));
        caregiver.setDate(dateUpdate);

        caregiver.calculateAmounts(calculator);

        assertEquals(83333, caregiver.getInsuranceAmount());
        assertEquals(74863, caregiver.getRefundAmount());
    }
}