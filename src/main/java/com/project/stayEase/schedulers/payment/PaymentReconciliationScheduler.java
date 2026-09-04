package com.project.stayEase.schedulers.payment;

import com.project.stayEase.service.booking.services.payment.PaymentReconciliationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentReconciliationScheduler {

    private final PaymentReconciliationService paymentReconciliationService;

    @Scheduled(fixedDelay = 60000)
    public void reconcilePendingPayments(){
        paymentReconciliationService.reconcilePendingPayments();
    }

}
