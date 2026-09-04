package com.project.stayEase.repository;


import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByCheckoutSessionId(String sessionId);

    Optional<Payment> findFirstByBookingIdAndPaymentStatusOrderByCreatedAtDesc(Long bookingId, PaymentStatus paymentStatus);

    List<Payment> findByPaymentStatusAndCreatedAtBefore(
            PaymentStatus status,
            LocalDateTime cutoffTime
    );
    Payment findByBookingIdAndPaymentStatus(Long bookingId, PaymentStatus paymentStatus);

    long countByBookingId(Long bookingId);
}
