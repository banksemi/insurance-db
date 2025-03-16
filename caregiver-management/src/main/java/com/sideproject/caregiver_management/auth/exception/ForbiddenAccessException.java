package com.sideproject.caregiver_management.auth.exception;

import com.sideproject.caregiver_management.common.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.FORBIDDEN)
public class ForbiddenAccessException extends KnownException {
    public ForbiddenAccessException() {
        super("권한이 없습니다.");
    }
}
