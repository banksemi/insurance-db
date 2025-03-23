package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class CaregiverEndDateAfterContractEndException extends KnownException {
    public CaregiverEndDateAfterContractEndException() {
        super("만료일은 보험 만료일 이후로 설정할 수 없습니다.");
    }
}
