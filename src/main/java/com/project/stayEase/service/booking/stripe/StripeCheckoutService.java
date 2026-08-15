package com.project.stayEase.service.booking.stripe;

import com.project.stayEase.customExceptions.PaymentProviderException;
import com.project.stayEase.service.booking.domain.request.PaymentRequest;
import com.project.stayEase.service.booking.domain.response.PaymentSession;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeCheckoutService {

    public PaymentSession createCheckoutSession(
            PaymentRequest request,String customerId ) {

        try {
            SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customerId)
                    .setSuccessUrl(request.getSuccessUrl())
                    .setCancelUrl(request.getFailureUrl())
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("inr")
                                    .setUnitAmount(request.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(request.getProductName())
                                                    .setDescription(request.getProductDescription())
                                                    .build()
                                    )
                                    .build())
                            .build())
                    .build();

            Session checkoutSession = Session.create(sessionCreateParams);
            return PaymentSession.builder()
                    .checkoutUrl(checkoutSession.getUrl())
                    .sessionId(checkoutSession.getId())
                    .paymentReference(checkoutSession.getPaymentIntent())
                    .provider("STRIPE")
                    .build();
        } catch (StripeException e) {

            throw new PaymentProviderException("Failed to create Stripe checkout session", e);
        }
    }

}
