package com.glowapex.exception;

public class JwtAuthenticationException extends RuntimeException {
    private final int statusCode;

    public JwtAuthenticationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
