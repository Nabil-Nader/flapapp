package com.flap.app.exception;

public class JwtFilterException extends RuntimeException {

    public JwtFilterException(String message, Throwable cause) {
        super(message, cause);
    }
}