package com.sideproject.caregiver_management.auth.exception;

import com.sideproject.caregiver_management.common.HTTPStatusAnnotation;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(value = HttpStatus.UNAUTHORIZED)
public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
