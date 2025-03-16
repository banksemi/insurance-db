package com.sideproject.caregiver_management.auth.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.UNAUTHORIZED)
public class NeedAuthenticationException extends KnownException {
    public NeedAuthenticationException() {
        super("로그인이 필요합니다.");
    }
    public NeedAuthenticationException(String message) {
        super(message);
    }
}
