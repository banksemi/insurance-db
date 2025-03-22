package com.sideproject.caregiver_management.user.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.NOT_FOUND)
public class NotFoundUserException extends KnownException {
    public NotFoundUserException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
