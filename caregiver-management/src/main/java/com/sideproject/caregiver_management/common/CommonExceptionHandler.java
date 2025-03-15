package com.sideproject.caregiver_management.common;

import com.sideproject.caregiver_management.user.dto.APIExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CommonExceptionHandler {
    private ResponseEntity<APIExceptionResponse> buildErrorResponse(HttpStatus status, List<Map<String, Object>> errors) {
        APIExceptionResponse response = APIExceptionResponse.builder()
                .status(status.value())
                .errors(errors)
                .build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> fieldErrors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("field", error.getField());
            errorDetails.put("message", error.getDefaultMessage());
            fieldErrors.add(errorDetails);
        });
        return buildErrorResponse(HttpStatus.BAD_REQUEST, fieldErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIExceptionResponse> handleGlobalException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Annotation 으로 HTTP 코드를 조회할 수 있을까?
        HTTPStatusAnnotation annotation = ex.getClass().getAnnotation(HTTPStatusAnnotation.class);
        List<Map<String, Object>> errors = new ArrayList<>();
        if (annotation != null) {
            status = annotation.value();
            errors.add(Map.of("message", ex.getMessage()));
        } else {
            errors.add(Map.of("message", "Internal Server Error"));
        }
        return buildErrorResponse(status, errors);
    }
}
