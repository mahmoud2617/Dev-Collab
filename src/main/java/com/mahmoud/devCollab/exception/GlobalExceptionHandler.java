package com.mahmoud.devCollab.exception;

import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(
        DataIntegrityViolationException exception
    ) {
        String message = "Data integrity violation: " + exception.getMostSpecificCause().getMessage();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError.conflict(message)
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

    @ExceptionHandler(InvalidRequestDataException.class)
    public ResponseEntity<ApiError> handleInvalidRequestDataException(
        InvalidRequestDataException exception
    ) {
        String message = exception.getMessage() != null
            ? exception.getMessage()
            : "Invalid request data.";

        return ResponseEntity.badRequest().body(
            ApiError.badRequest(message)
        );
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ApiError> handleEmailNotVerifiedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ApiError.forbidden("Email Not verified.")
        );
    }
}
