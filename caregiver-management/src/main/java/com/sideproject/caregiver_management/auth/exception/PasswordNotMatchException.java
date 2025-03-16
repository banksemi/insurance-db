package com.sideproject.caregiver_management.auth.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(value = HttpStatus.UNAUTHORIZED)
public class PasswordNotMatchException extends KnownException {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
