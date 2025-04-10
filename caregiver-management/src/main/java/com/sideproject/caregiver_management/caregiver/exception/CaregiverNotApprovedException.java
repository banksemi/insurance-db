package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.FORBIDDEN)
public class CaregiverNotApprovedException extends KnownException {
    public CaregiverNotApprovedException() {
        super("관리자의 승인이 완료된 항목만 변경 가능합니다.");
    }
}
