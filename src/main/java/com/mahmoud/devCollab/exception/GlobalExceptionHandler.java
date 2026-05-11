package com.mahmoud.devCollab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException() {
        return ResponseEntity.badRequest().body(
            ApiError.badRequest("Invalid request body.")
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(
        BadCredentialsException exception
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiError.unauthorized(exception.getMessage())
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiError.notFound("User not found.")
        );
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ApiError> handleUnauthorizedUserException(
        UnauthorizedUserException exception
    ) {
        if (exception.getMessage() != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiError.unauthorized(exception.getMessage())
            );
        }

        return ResponseEntity.status(401).build();
    }
}
