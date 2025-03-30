package com.sideproject.caregiver_management.caregiver.service;

import com.sideproject.caregiver_management.caregiver.entity.Caregiver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class CaregiverStatusServiceTest {
    private final Clock clock = Clock.fixed(
            ZonedDateTime.of(2024, 9, 1, 1, 0, 0, 0, ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
    );
    private final CaregiverStatusService stateService = new CaregiverStatusServiceImpl(clock);

    @Test
    @DisplayName("미래의 시작일이 지정된 경우 상태는 근무예정이 되어야함 (해지일 없음)")
    void shouldBeScheduled() {
        Caregiver caregiver = Caregiver.builder().startDate(LocalDate.of(2024, 9, 2)).build();

        String readableState = stateService.getReadableStatus(caregiver);

        assertEquals("근무예정", readableState);
    }

    @Test
    @DisplayName("날짜는 0시 기준이므로 보험 시작일이 오늘과 같으면 '근무'로 표시되어야함 (해지일 없음)")
    void shouldBeWorkingToday() {
        Caregiver caregiver = Caregiver.builder().startDate(LocalDate.of(2024, 9, 1)).build();

        String readableState = stateService.getReadableStatus(caregiver);

        assertEquals("근무", readableState);
    }

    @Test
    @DisplayName("보험 시작일이 과거이면 '근무'로 표시되어야함 (해지일 없음)")
    void shouldBeWorkingBeforeToday() {
        Caregiver caregiver = Caregiver.builder().startDate(LocalDate.of(2024, 8, 31)).build();

        String readableState = stateService.getReadableStatus(caregiver);

        assertEquals("근무", readableState);
    }

    @Test
    @DisplayName("보험 시작일과 종료일이 같으면 '삭제됨'으로 처리되어야함")
    void shouldBeDeleted() {
        Caregiver caregiver = Caregiver.builder()
                .startDate(LocalDate.of(2024, 8, 31))
                .endDate(LocalDate.of(2024, 8, 31))
                .build();
        Caregiver caregiver2 = Caregiver.builder()
                .startDate(LocalDate.of(2024, 9, 1))
                .endDate(LocalDate.of(2024, 9, 1))
                .build();
        Caregiver caregiver3 = Caregiver.builder()
                .startDate(LocalDate.of(2024, 9, 2))
                .endDate(LocalDate.of(2024, 9, 2))
                .build();

        String readableState = stateService.getReadableStatus(caregiver);
        String readableState2 = stateService.getReadableStatus(caregiver2);
        String readableState3 = stateService.getReadableStatus(caregiver3);

        assertEquals("삭제됨", readableState);
        assertEquals("삭제됨", readableState2);
        assertEquals("삭제됨", readableState3);
    }

    @Test
    @DisplayName("해지일이 지정되어있더라도 근무를 시작하지 않았으면 근무 예정으로 표시되어야함")
    void shouldBeScheduledWithEndDate() {
        Caregiver caregiver = Caregiver.builder()
                .startDate(LocalDate.of(2024, 9, 2))
                .endDate(LocalDate.of(2024, 9, 3))
                .build();

        String readableState = stateService.getReadableStatus(caregiver);

        assertEquals("근무예정", readableState);
    }

    @Test
    @DisplayName("해지일이 지정되어있더라도 근무중인경우 '근무'로 표시되어야함")
    void shouldBeWorkingWithEndDate() {
        Caregiver caregiver = Caregiver.builder()
                .startDate(LocalDate.of(2024, 8, 31))
                .endDate(LocalDate.of(2024, 9, 3))
                .build();
        Caregiver caregiver2 = Caregiver.builder()
                .startDate(LocalDate.of(2024, 9, 1))
                .endDate(LocalDate.of(2024, 9, 3))
                .build();

        String readableState = stateService.getReadableStatus(caregiver);
        String readableState2 = stateService.getReadableStatus(caregiver2);

        assertEquals("근무", readableState);
        assertEquals("근무", readableState2);
    }

    @Test
    @DisplayName("해지일이 오늘과 같으면 '퇴사'로 표시되어야함")
    void shouldBeLeaveToday() {
        // NOTE: 시작일과 종료일은 0시를 기준으로 계산되므로, 두 날짜가 같으면 0시간 근무와 동일함
        Caregiver caregiver = Caregiver.builder()
                .startDate(LocalDate.of(2024, 8, 22))
                .endDate(LocalDate.of(2024, 9, 1))
                .build();

        String readableState = stateService.getReadableStatus(caregiver);

        assertEquals("퇴사", readableState);
    }

    @Test
    @DisplayName("해지일이 과거이면 '퇴사'로 표시되어야함")
    void shouldBeLeaveBeforeToday() {
        Caregiver caregiver = Caregiver.builder()
                .startDate(LocalDate.of(2024, 8, 22))
                .endDate(LocalDate.of(2024, 8, 31))
                .build();

        String readableState = stateService.getReadableStatus(caregiver);

        assertEquals("퇴사", readableState);
    }
}