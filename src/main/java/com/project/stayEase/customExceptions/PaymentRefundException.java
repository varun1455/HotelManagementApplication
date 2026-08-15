package com.project.stayEase.customExceptions;

import com.stripe.exception.StripeException;

public class PaymentRefundException extends RuntimeException {
    public PaymentRefundException(String message, StripeException e) {
        super(message);
    }
}
