package com.allo.exception;

public class ReservationExpiredException extends RuntimeException {

    public ReservationExpiredException(String message) {
        super(message);
    }
}