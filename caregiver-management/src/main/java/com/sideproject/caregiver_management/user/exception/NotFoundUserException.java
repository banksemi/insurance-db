package com.sideproject.caregiver_management.user.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class NotFoundUserException extends KnownException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
