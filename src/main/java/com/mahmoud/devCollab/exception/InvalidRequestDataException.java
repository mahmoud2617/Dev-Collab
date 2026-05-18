package com.mahmoud.devCollab.exception;

public class InvalidRequestDataException extends RuntimeException {
    public InvalidRequestDataException() {
    }

    public InvalidRequestDataException(String message) {
        super(message);
    }
}
