package com.sideproject.caregiver_management.caregiver.exception;

import com.sideproject.caregiver_management.common.dto.HTTPStatusAnnotation;
import com.sideproject.caregiver_management.common.exception.KnownException;
import org.springframework.http.HttpStatus;

@HTTPStatusAnnotation(HttpStatus.BAD_REQUEST)
public class CaregiverDuplicateException extends KnownException {
    public CaregiverDuplicateException() {
        super("이미 등록되어있습니다. 새로운 보험은 기존에 등록된 보험이 끝난 이후로만 설정할 수 있습니다.");
    }
}
