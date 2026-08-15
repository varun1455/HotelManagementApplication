package com.project.stayEase.service.booking.services.payment;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.PaymentRepository;
import com.project.stayEase.service.booking.domain.response.PaymentResult;
import com.project.stayEase.service.booking.services.inventory.InventoryBookingService;
import com.stripe.model.checkout.Session;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentConfirmationService {


    private final PaymentRepository paymentRepository;
    private final InventoryBookingService inventoryBookingService;

    @Transactional
    public PaymentResult confirmPayment(Session session){
        String sessionId = session.getId();

        log.info("Processing Stripe checkout session: {}", sessionId);

        Payment payment = paymentRepository.findByCheckoutSessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with session id " + sessionId));

        // Idempotency
        if (payment.getPaymentStatus() == PaymentStatus.APPROVED) {
            log.info("Payment already processed: {}", sessionId);
            return PaymentResult.builder()
                    .successful(true)
                    .providerPaymentId(payment.getPaymentIntentId())
                    .amount(payment.getAmount())
                    .status(PaymentStatus.APPROVED)
                    .build();
        }

        if (!"paid".equals(session.getPaymentStatus())) {
            log.warn("Checkout completed but payment not paid. " + "SessionId={}, status={}", sessionId, session.getPaymentStatus());
            return PaymentResult.builder()
                    .successful(false)
                    .status(PaymentStatus.FAILED)
                    .failureReason("Stripe payment status is not paid")
                    .build();
        }

        payment.setPaymentIntentId(session.getPaymentIntent());

        payment.setPaymentStatus(PaymentStatus.APPROVED);

        inventoryBookingService.confirm(payment.getBooking());

        log.info("Payment successfully confirmed. " + "paymentId={}, bookingId={}", payment.getId(), payment.getBooking().getId());

        return PaymentResult.builder()
                        .providerPaymentId(session.getPaymentIntent())
                                .amount(payment.getAmount())
                                        .status(PaymentStatus.APPROVED)
                                                .successful(true)
                                                        .failureReason(null)
                                                                .build();


    }
}
