package com.sideproject.caregiver_management.auth.exception;

import com.sideproject.caregiver_management.common.HTTPStatusAnnotation;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.UNAUTHORIZED)
public class NeedAuthenticationException extends RuntimeException {
    public NeedAuthenticationException() {
        super("로그인이 필요합니다.");
    }
}
