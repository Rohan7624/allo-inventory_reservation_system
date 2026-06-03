package com.allo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            InsufficientStockException.class)
    public ResponseEntity<?> handleStock(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(
            ReservationExpiredException.class)
    public ResponseEntity<?> handleExpired(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(ex.getMessage());
    }
}