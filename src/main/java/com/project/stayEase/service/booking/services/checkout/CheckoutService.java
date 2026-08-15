package com.project.stayEase.service.booking.services.checkout;

import com.project.stayEase.entity.Booking;
import com.project.stayEase.service.booking.domain.request.PaymentRequest;
import com.project.stayEase.service.booking.domain.response.PaymentSession;
import com.project.stayEase.service.booking.services.provider.PaymentProvider;
import com.project.stayEase.service.booking.services.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final PaymentService paymentService;
    private final PaymentProvider paymentProvider;


    public PaymentSession createPaymentSession(Booking booking, String successUrl, String failureUrl){

        PaymentRequest request = PaymentRequest.builder()
                .bookingId(booking.getId())
                .amount(booking.getAmount())
                .currency("INR")
                .successUrl(successUrl)
                .failureUrl(failureUrl)
                .productName(
                booking.getHotel().getName()
                        + " - "
                        + booking.getRoom().getType().getName()
                        + " - "
                        + booking.getRoom().getBedType().getName()
        )
                .productDescription(
                        "Check-in: " + booking.getCheckInDate()
                                + ", Check-out: " + booking.getCheckOutDate()
                )
                .quantity(booking.getRoomsCount().longValue())
                .build();

        PaymentSession session = paymentProvider.createCheckoutSession(request, booking.getUser());
        paymentService.createPendingPayment(
                booking,
                session.getSessionId()
        );

        return session;
    }


}
