package com.project.stayEase.customExceptions;

public class PaymentRetryLimitExceededException extends RuntimeException {
    public PaymentRetryLimitExceededException(String message) {
        super(message);
    }
}
