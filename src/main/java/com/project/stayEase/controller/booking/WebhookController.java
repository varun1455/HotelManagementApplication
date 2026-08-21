package com.project.stayEase.controller.booking;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.service.booking.domain.request.PaymentVerificationRequest;
import com.project.stayEase.service.booking.facade.BookingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Payment Webhooks",
        description = "Webhook endpoints used by payment providers to notify StayEase about payment events"
)
public class WebhookController {

    private final BookingFacade bookingFacade;

    @Operation(
            summary = "Handle Stripe payment webhook",
            description = """
                    Receives payment events from Stripe.

                    The Stripe-Signature header is used to verify that
                    the webhook originated from Stripe before processing
                    the payment event.
                    """
    )@ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Webhook processed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid webhook payload or signature"
            )
    })
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
