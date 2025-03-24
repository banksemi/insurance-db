package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.exception.KnownException;

public class CaregiverStartDateBeforeNowException extends KnownException {
    public CaregiverStartDateBeforeNowException() {
        super("보험 시작일은 오늘 이전으로 설정할 수 없습니다.");
    }
}
