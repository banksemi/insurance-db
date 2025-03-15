package com.sideproject.caregiver_management.user.exception;

import com.sideproject.caregiver_management.user.dto.APIExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class APIExceptionHandler {
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

    @ExceptionHandler(NotFoundTenantException.class)
    public ResponseEntity<APIExceptionResponse> handleNotFoundTenantException(NotFoundTenantException ex) {
        List<Map<String, Object>> errors = List.of(Map.of("message", ex.getMessage()));
        return buildErrorResponse(HttpStatus.NOT_FOUND, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIExceptionResponse> handleGlobalException(Exception ex) {
        List<Map<String, Object>> errors = List.of(Map.of("message", ex.getMessage()));
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors);
    }
}
