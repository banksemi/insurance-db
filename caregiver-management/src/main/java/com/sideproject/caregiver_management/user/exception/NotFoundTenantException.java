package com.sideproject.caregiver_management.user.exception;

import com.sideproject.caregiver_management.common.HTTPStatusAnnotation;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class NotFoundTenantException extends RuntimeException {
    public NotFoundTenantException(String message) {
        super(message);
    }
}
