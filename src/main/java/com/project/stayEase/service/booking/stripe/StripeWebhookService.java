package com.project.stayEase.service.booking.stripe;

import com.project.stayEase.customExceptions.InvalidWebhookException;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.service.booking.domain.request.PaymentVerificationRequest;
import com.project.stayEase.service.booking.domain.response.PaymentResult;
import com.project.stayEase.service.booking.services.payment.PaymentConfirmationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StripeWebhookService {


    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    private final PaymentConfirmationService paymentConfirmationService;

    public PaymentResult verifyPayment(PaymentVerificationRequest request){

        Event event;
        try{
            event = Webhook.constructEvent(request.getProviderPayload(), request.getProviderSignature(), stripeWebhookSecret);
        } catch (SignatureVerificationException e) {
            throw new InvalidWebhookException("Invalid Stripe webhook signature", e);
        }

        if (event.getType().equals("checkout.session.completed")) {
            return handleCheckoutCompleted(event);
        } else {
            log.info(
                    "Unhandled Stripe event: {}",
                    event.getType()
            );
        }
        return null;
    }

    private PaymentResult handleCheckoutCompleted(Event event) {

        Session session = extractSession(event);

        if (session == null) {
            log.warn("Unable to deserialize checkout session");
            return PaymentResult.builder()
                    .successful(false)
                    .status(PaymentStatus.FAILED)
                    .failureReason("Unable to deserialize Stripe checkout session")
                    .build();
        }

        return paymentConfirmationService.confirmPayment(session);
    }
    private Session extractSession(Event event) {

        return (Session) event
                .getDataObjectDeserializer()
                .getObject()
                .orElse(null);
    }


}
