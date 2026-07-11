package com.project.stayEase.repository;


import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByCheckoutSessionId(String sessionId);

    Optional<Payment> findFirstByBookingIdAndPaymentStatusOrderByCreatedAtDesc(Long bookingId, PaymentStatus paymentStatus);
}
