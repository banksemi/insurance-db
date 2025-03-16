package com.sideproject.caregiver_management.user.exception;

import com.sideproject.caregiver_management.common.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class NotFoundTenantException extends KnownException {
    public NotFoundTenantException(String message) {
        super(message);
    }
}
