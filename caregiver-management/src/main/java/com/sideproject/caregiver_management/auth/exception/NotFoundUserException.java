package com.sideproject.caregiver_management.auth.exception;

import com.sideproject.caregiver_management.common.HTTPStatusAnnotation;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
