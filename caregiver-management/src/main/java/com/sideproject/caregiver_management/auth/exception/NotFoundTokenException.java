package com.sideproject.caregiver_management.auth.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class NotFoundTokenException extends KnownException {
    public NotFoundTokenException() {
        super("토큰을 찾을 수 없습니다.");
    }
}
