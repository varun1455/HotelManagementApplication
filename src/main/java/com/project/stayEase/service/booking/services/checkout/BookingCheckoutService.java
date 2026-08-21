package com.project.stayEase.service.booking.services.checkout;

import com.project.stayEase.customExceptions.InvalidBookingStateException;
import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.checkout.PaymentSessionDto;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.repository.BookingRepository;
import com.project.stayEase.security.utils.SecurityUtils;
import com.project.stayEase.service.booking.domain.response.PaymentSession;
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

    @Value("${frontend.url}")
    private String frontendUrl;


    public PaymentSessionDto initiatePayment(Long bookingId) {

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, securityUtils.getCurrentUserId())
                .orElseThrow(()->new ResourceNotFoundException("Booking not found or access denied"));

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

        if(booking.getBookingStatus() == BookingStatus.CONFIRMED || booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingStateException(
                    "Booking is already confirmed or cancelled"
            );
        }

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
}
