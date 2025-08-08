package com.glowapex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleJwtAuthException(JwtAuthenticationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("status", ex.getStatusCode());
        body.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(body, HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleOtpExpired(OtpExpiredException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorCode", "OTP_EXPIRED");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpInvalidException.class)
    public ResponseEntity<Map<String, Object>> handleOtpInvalid(OtpInvalidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorCode", "OTP_INVALID");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}