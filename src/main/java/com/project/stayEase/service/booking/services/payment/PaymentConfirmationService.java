package com.project.stayEase.service.booking.services.payment;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.PaymentRepository;
import com.project.stayEase.service.booking.domain.response.PaymentResult;
import com.project.stayEase.service.booking.services.inventory.InventoryConfirmBookingService;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentConfirmationService {


    private final PaymentRepository paymentRepository;
    private final InventoryConfirmBookingService inventoryConfirmBookingService;

    @Transactional
    public PaymentResult confirmPayment(Session session){
        String sessionId = session.getId();

        Payment payment = paymentRepository.findByCheckoutSessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with session id " + sessionId));

        // Idempotency
        if (payment.getPaymentStatus() == PaymentStatus.APPROVED) {
            return PaymentResult.builder()
                    .successful(true)
                    .providerPaymentId(payment.getPaymentIntentId())
                    .amount(payment.getAmount())
                    .status(PaymentStatus.APPROVED)
                    .build();
        }

        if (!"paid".equals(session.getPaymentStatus())) {
            return PaymentResult.builder()
                    .successful(false)
                    .status(PaymentStatus.FAILED)
                    .failureReason("Stripe payment status is not paid")
                    .build();
        }

        payment.setPaymentIntentId(session.getPaymentIntent());

        payment.setPaymentStatus(PaymentStatus.APPROVED);

        inventoryConfirmBookingService.confirm(payment.getBooking());

        return PaymentResult.builder()
                        .providerPaymentId(session.getPaymentIntent())
                                .amount(payment.getAmount())
                                        .status(PaymentStatus.APPROVED)
                                                .successful(true)
                                                        .failureReason(null)
                                                                .build();


    }
}
