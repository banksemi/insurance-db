package com.sideproject.caregiver_management.caregiver.entity;

import com.sideproject.caregiver_management.caregiver.dto.CaregiverCreateRequest;
import com.sideproject.caregiver_management.caregiver.dto.CaregiverDateUpdate;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverEndDateAfterContractEndException;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverEndDateBeforeStartException;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverStartDateAfterContractEndException;
import com.sideproject.caregiver_management.caregiver.exception.CaregiverStartDateBeforeContractStartException;
import com.sideproject.caregiver_management.insurance.dto.InsuranceUpdateRequest;
import com.sideproject.caregiver_management.insurance.entity.Insurance;
import com.sideproject.caregiver_management.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CaregiverDateValidationTest {
    Insurance insurance = new Insurance(null);
    @BeforeEach
    void setUp() {
        insurance = new Insurance(User.builder().build());
        insurance.updateInsurance(                InsuranceUpdateRequest.builder()
                .startDate(LocalDate.of(2024, 6, 1))
                .endDate(LocalDate.of(2025, 6, 1))
                .policyNumber("123")
                .personalInsuranceFee(1000L)
                .sharedInsuranceFee(1000L)
                .build());
    }

    @Test
    @DisplayName("간병일을 보험 날짜에 정확히 일치시킬 수 있어야함 (최대 범위)")
    void canCreateCaregiverWithFullInsuranceDateRange() {
        Caregiver caregiver = new Caregiver(insurance, false);

        caregiver.setDate(CaregiverDateUpdate.of(
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2025, 6, 1)
        ));

        assertEquals(LocalDate.of(2024, 6, 1), caregiver.getStartDate());
        assertEquals(LocalDate.of(2025, 6, 1), caregiver.getEndDate());
    }

    @Test
    @DisplayName("같은 날 즉시 해지하는 것은 가능해야함")
    void canTerminateCaregiverOnSameDay() {
        Caregiver caregiver = new Caregiver(insurance, false);

        caregiver.setDate(CaregiverDateUpdate.of(
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2024, 7, 1)
        ));

        assertEquals(LocalDate.of(2024, 7, 1), caregiver.getStartDate());
        assertEquals(LocalDate.of(2024, 7, 1), caregiver.getEndDate());
    }

    @Test
    @DisplayName("간병 시작일은 보험 시작일 이전으로 설정할 수 없음")
    void caregiverStartDateCannotBeBeforeInsuranceStartDate() {
        Caregiver caregiver = new Caregiver(insurance, false);

        assertThrows(CaregiverStartDateBeforeContractStartException.class, () ->
                caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                        LocalDate.of(2024, 5, 31)
                ))
        );
    }
    @Test
    @DisplayName("간병 시작일은 보험 만료일 하루 전까지만 설정 가능")
    void caregiverStartDateCanBeUpToOneDayBeforeInsuranceEndDate() {
        Caregiver caregiver = new Caregiver(insurance, false);

        // 보험 만료일(2025-06-01)의 하루 전(2025-05-31)
        caregiver.setDate(CaregiverDateUpdate.ofStartDate(
                LocalDate.of(2025, 5, 31)
        ));

        assertEquals(LocalDate.of(2025, 5, 31), caregiver.getStartDate());
        assertNull(caregiver.getEndDate());
    }
    @Test
    @DisplayName("간병 시작일은 보험 만료일보다 크거나 같을 수 없음")
    void caregiverStartDateCannotBeOnOrAfterInsuranceEndDate() {
        Caregiver caregiver = new Caregiver(insurance, false);

        assertThrows(
                CaregiverStartDateAfterContractEndException.class,
                () -> {
                    CaregiverDateUpdate dateUpdate = new CaregiverDateUpdate();
                    dateUpdate.setStartDate(LocalDate.of(2025, 6, 1));
                    caregiver.setDate(dateUpdate);
                }
        );
    }

    @Test
    @DisplayName("간병 종료일은 간병 시작일보다 이전으로 설정할 수 없음")
    void caregiverEndDateCannotBeBeforeStartDate() {
        Caregiver caregiver = new Caregiver(insurance, false);

        assertThrows(
                CaregiverEndDateBeforeStartException.class,
                () -> {
                    CaregiverDateUpdate dateUpdate = new CaregiverDateUpdate();
                    dateUpdate.setStartDate(LocalDate.of(2024, 6, 2));
                    dateUpdate.setEndDate(LocalDate.of(2024, 6, 1));
                    caregiver.setDate(dateUpdate);
                }
        );
    }

    @Test
    @DisplayName("간병 종료일은 보험 만료일 이후로 설정할 수 없음")
    void caregiverEndDateCannotBeAfterInsuranceEndDate() {
        Caregiver caregiver = new Caregiver(insurance, false);

        assertThrows(CaregiverEndDateAfterContractEndException.class, () ->
                caregiver.setDate(CaregiverDateUpdate.of(
                        LocalDate.of(2024, 6, 2),
                        LocalDate.of(2025, 6, 2)
                ))
        );
    }
}