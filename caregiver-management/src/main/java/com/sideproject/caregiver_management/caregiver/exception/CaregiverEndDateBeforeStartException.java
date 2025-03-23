package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class CaregiverEndDateBeforeStartException extends KnownException {
    public CaregiverEndDateBeforeStartException() {
        super("만료일은 시작일보다 이전으로 설정할 수 없습니다.");
    }
}
