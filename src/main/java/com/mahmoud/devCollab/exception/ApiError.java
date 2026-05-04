package com.mahmoud.devCollab.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ApiError {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;

    public static ApiError unauthorized(Integer status, String message) {
        return new ApiError(status, message, LocalDateTime.now());
    }

    public static ApiError notFound() {
        return new ApiError(404, "User not found.", LocalDateTime.now());
    }
}
