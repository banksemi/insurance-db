package com.sideproject.caregiver_management.user.exception;

import com.sideproject.caregiver_management.user.HTTPStatusAnnotation;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(value = HttpStatus.UNAUTHORIZED)
public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
