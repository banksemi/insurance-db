package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.BAD_REQUEST)
public class CaregiverStartDateAfterContractEndException extends KnownException {
    public CaregiverStartDateAfterContractEndException() {
        super("시작일은 보험의 만료일 이후로 설정할 수 없습니다.");
    }
}
