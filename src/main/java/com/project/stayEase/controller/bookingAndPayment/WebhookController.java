package com.project.stayEase.controller.bookingAndPayment;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.service.booking.domain.request.PaymentVerificationRequest;
import com.project.stayEase.service.booking.facade.BookingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final BookingFacade bookingFacade;

    @PostMapping("/payments")
    public ResponseEntity<ApiResponse<ApiResponse<Void>>> paymentWebhookHandler(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
            PaymentVerificationRequest paymentVerificationRequest = PaymentVerificationRequest.builder()
                            .providerPayload(payload)
                                    .providerSignature(sigHeader)
                                            .build();
           bookingFacade.handlePaymentWebhook(paymentVerificationRequest);
            return ResponseEntity.ok(ApiResponse.successResponse(null));
    }

}
