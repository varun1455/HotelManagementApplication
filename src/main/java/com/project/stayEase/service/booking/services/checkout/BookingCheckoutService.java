package com.project.stayEase.service.booking.services.checkout;

import com.project.stayEase.customExceptions.*;
import com.project.stayEase.dto.checkout.PaymentSessionDto;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.BookingRepository;
import com.project.stayEase.repository.PaymentRepository;
import com.project.stayEase.security.utils.SecurityUtils;
import com.project.stayEase.service.booking.domain.response.PaymentSession;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingCheckoutService {

    private final BookingRepository bookingRepository;
    private final SecurityUtils securityUtils;
    private final CheckoutService checkoutService;
    private final PaymentRepository paymentRepository;

    @Value("${frontend.url}")
    private String frontendUrl;


    public PaymentSessionDto initiatePayment(Long bookingId) {

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, securityUtils.getCurrentUserId())
                .orElseThrow(()->new ResourceNotFoundException("Booking not found or access denied"));


        if(booking.getBookingStatus() == BookingStatus.CONFIRMED ) {
            throw new InvalidBookingStateException(
                    "Booking is already confirmed"
            );
        }


        if(booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingStateException(
                    "Booking is already cancelled"
            );
        }

        if (booking.getBookingStatus() == BookingStatus.PAYMENT_PENDING){
            return handleExistingPaymentAttempt(booking);
        }


        validateBookingForCheckout(booking);

        PaymentSession session = checkoutService.createPaymentSession(booking,frontendUrl + "payments/success", frontendUrl + "payments/failure");

        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        booking.setPaymentInitiatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return PaymentSessionDto.builder()
                .sessionUrl(session.getCheckoutUrl())
                .build();
    }

    private void validateBookingForCheckout(Booking booking) {

        if (booking.getBookingStatus() != BookingStatus.GUESTS_ADDED) {
            throw new InvalidBookingStateException(
                    "Booking is not ready for checkout"
            );
        }

        // Have to decide whether this method needed or not
        if (booking.getReservedUntil().isBefore(LocalDateTime.now())) {
            throw new InvalidBookingStateException(
                    "Booking reservation has expired"
            );
        }
    }

    public PaymentSessionDto handleExistingPaymentAttempt(Booking booking)  {

            Payment payment = paymentRepository.findByBookingIdAndPaymentStatus(booking.getId(), PaymentStatus.PENDING);

            try{
                Session session = Session.retrieve(payment.getCheckoutSessionId());
                String sessionStatus = session.getStatus();
                String paymentStatus = session.getPaymentStatus();

                if ("open".equals(sessionStatus) &&
                        "unpaid".equals(paymentStatus)) {

                    if(booking.getBookingStatus() == BookingStatus.EXPIRED){
                        throw new InvalidBookingStateException(
                                "Booking reservation has expired"
                        );
                    } else{
                        return PaymentSessionDto.builder()
                                .sessionUrl(session.getUrl())
                                .build();
                    }


                }
                if ("expired".equals(sessionStatus)){

                    long attemptCount = paymentRepository.countByBookingId(booking.getId());
                    if(attemptCount>3){
                        throw new PaymentRetryLimitExceededException("Payment Retry limit has been exceeded, Please create your booking again");
                    }

                    PaymentSession newPaymentSession = checkoutService.createPaymentSession(booking,frontendUrl + "payments/success", frontendUrl + "payments/failure");
                    return PaymentSessionDto.builder()
                            .sessionUrl(newPaymentSession.getCheckoutUrl())
                            .build();
                }
                if("paid".equals(paymentStatus)){
                   throw  new PaymentAlreadyCompletedException("Payment already Paid wait for confirmation");
                }

            } catch (StripeException e) {
                throw new PaymentProviderException("Unable to retrieve Stripe checkout session", e);
            }
            return null;
    }
}
