package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class NotFoundCaregiverException extends KnownException {
    public NotFoundCaregiverException() {
        super("간병인 정보를 찾을 수 없습니다.");
    }
}
