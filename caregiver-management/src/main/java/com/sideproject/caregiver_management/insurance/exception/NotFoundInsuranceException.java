package com.sideproject.caregiver_management.insurance.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class NotFoundInsuranceException extends KnownException {
    public NotFoundInsuranceException(String message) {
        super(message);
    }
}
