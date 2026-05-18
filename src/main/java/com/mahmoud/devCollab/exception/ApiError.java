package com.mahmoud.devCollab.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;

    private ApiError(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static ApiError unauthorized(String message) {
        return new ApiError(401, message);
    }

    public static ApiError notFound(String message) {
        return new ApiError(404, message);
    }

    public static ApiError badRequest(String message) {
        return new ApiError(400, message);
    }

    public static ApiError forbidden(String message) {
        return new ApiError(403, message);
    }
}
