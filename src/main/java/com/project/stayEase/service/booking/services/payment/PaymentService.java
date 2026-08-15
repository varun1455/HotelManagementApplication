package com.project.stayEase.service.booking.services.payment;

import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void createPendingPayment(
            Booking booking,
            String checkoutSessionId) {

        Payment payment = Payment.builder()
                .checkoutSessionId(checkoutSessionId)
                .amount(booking.getAmount()) 
                .paymentStatus(PaymentStatus.PENDING)
                .booking(booking)
                .build();

         paymentRepository.save(payment);
    }
}
