package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.FORBIDDEN)
public class CaregiverForbiddenException extends KnownException {
    public CaregiverForbiddenException() {
        super("해당 간병인 정보에 접근할 수 없습니다.");
    }
}
