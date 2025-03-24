package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.BAD_REQUEST)
public class CaregiverStartDateInPastException extends KnownException {
    public CaregiverStartDateInPastException() {
        super("보험 시작일은 과거가 될 수 없습니다.");
    }
}
