package com.mahmoud.devCollab.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String s) {
        super(s);
    }

    public InternalServerErrorException() {
        super();
    }
}
