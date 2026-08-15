package com.project.stayEase.customExceptions;

import com.stripe.exception.StripeException;

public class PaymentProviderException extends RuntimeException {
    public PaymentProviderException(String message, StripeException e) {
        super(message);
    }
}
