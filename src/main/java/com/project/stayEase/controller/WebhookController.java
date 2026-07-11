package com.project.stayEase.controller;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {



    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    @PostMapping("/payments")
    public ResponseEntity<ApiResponse<Void>> capturePayment(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
        try{
            log.info("Webhook Secret Loaded: {}", stripeWebhookSecret);
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
            bookingService.capturePaymentEvent(event);
            return ResponseEntity.noContent().build();
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }


}
