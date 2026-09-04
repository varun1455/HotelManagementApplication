package com.project.stayEase.service.booking.services.payment;

import com.project.stayEase.customExceptions.InvalidBookingStateException;
import com.project.stayEase.customExceptions.PaymentRetryLimitExceededException;
import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentReconciliationService {

    private final PaymentRepository paymentRepository;
    private final PaymentConfirmationService paymentConfirmationService;

    public void reconcilePendingPayments() {

        LocalDateTime cutoffTime =
                LocalDateTime.now().minusMinutes(1);

        List<Payment> payments =
                paymentRepository.findByPaymentStatusAndCreatedAtBefore(
                        PaymentStatus.PENDING,
                        cutoffTime
                );

        if (payments.isEmpty()) {
            log.debug("No pending payments require reconciliation");
            return;
        }

        for(Payment payment : payments){

            try{
                reconcilePayment(payment);
            }catch(StripeException e){
                log.error(
                        "event=PAYMENT_RECONCILIATION_FAILED paymentId={} bookingId={} errorType={}",
                        payment.getId(),
                        payment.getBooking().getId(),
                        e.getClass().getSimpleName(),
                        e
                );
            }
        }
    }

    private void reconcilePayment(Payment payment) throws StripeException{
        String sessionId = payment.getCheckoutSessionId();

        if (sessionId == null || sessionId.isBlank()) {

            log.error("event=PAYMENT_RECONCILIATION_SKIPPED paymentId={} reason=MISSING_SESSION_ID", payment.getId());
            return;
        }

        Session session = Session.retrieve(payment.getCheckoutSessionId());
        String paymentStatus = session.getPaymentStatus();
        String sessionStatus = session.getStatus();

        if ("paid".equals(paymentStatus)) {

            paymentConfirmationService.confirmPayment(session);

            log.info(
                    "event=PAYMENT_RECONCILED paymentId={} bookingId={} result=APPROVED",
                    payment.getId(),
                    payment.getBooking().getId()
            );

            return;
        }
        if ("open".equals(sessionStatus) &&
                "unpaid".equals(paymentStatus)) {

            log.info(
                    "event=PAYMENT_RECONCILIATION_PENDING paymentId={} bookingId={} result=SESSION_STILL_OPEN",
                    payment.getId(),
                    payment.getBooking().getId()
            );

            return;
        }


        if ("expired".equals(sessionStatus)) {

            handleExpiredPayment(payment);

            log.info(
                    "event=PAYMENT_RECONCILED paymentId={} bookingId={} result=EXPIRED",
                    payment.getId(),
                    payment.getBooking().getId()
            );

            return;
        }



    }

    private void handleExpiredPayment(Payment payment) {

        long attemptCount = paymentRepository.countByBookingId(payment.getBooking().getId());

        if(attemptCount>3){
            throw new PaymentRetryLimitExceededException("Payment Retry limit has been exceeded, Please create your booking again");
        }
        if(payment.getBooking().getBookingStatus()==BookingStatus.EXPIRED){
            throw new InvalidBookingStateException("Booking has been Expired");
        }
        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.getBooking().setBookingStatus(BookingStatus.GUESTS_ADDED);


        paymentRepository.save(payment);
    }
}
