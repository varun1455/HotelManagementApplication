package com.project.stayEase.customExceptions;

public class GuestCapacityExceededException extends RuntimeException {
    public GuestCapacityExceededException(String message) {
        super(message);
    }
}
