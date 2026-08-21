package com.project.stayEase.service.booking.facade;

import com.project.stayEase.dto.booking.request.BookingRequestDto;
import com.project.stayEase.dto.booking.response.BookingResponseDto;
import com.project.stayEase.dto.guests.request.GuestRequestDto;
import com.project.stayEase.dto.checkout.PaymentSessionDto;
import com.project.stayEase.service.booking.domain.request.PaymentVerificationRequest;
import com.project.stayEase.service.booking.services.cancellation.BookingCancellationService;
import com.project.stayEase.service.booking.services.checkout.BookingCheckoutService;
import com.project.stayEase.service.booking.services.creation.BookingCreationService;
import com.project.stayEase.service.booking.services.guests.BookingGuestService;
import com.project.stayEase.service.booking.services.provider.PaymentProvider;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingFacade {

    private final BookingCreationService bookingCreationService;
    private final BookingGuestService bookingGuestService;
    private final BookingCheckoutService bookingCheckoutService;
    private final BookingCancellationService bookingCancellationService;
    private final PaymentProvider paymentProvider;


    public BookingResponseDto createBooking(BookingRequestDto request) {
        return bookingCreationService.initializeBooking(request);
    }

    public BookingResponseDto addGuests(Long bookingId, List<GuestRequestDto> guests) {
        return bookingGuestService.addGuestsToBooking(guests, bookingId);
    }

    public PaymentSessionDto initiatePayment(Long bookingId){
        return bookingCheckoutService.initiatePayment(bookingId);
    }

    public void handlePaymentWebhook(PaymentVerificationRequest request){
        paymentProvider.processWebhook(request);
    }

    public void cancelBooking(Long bookingId){
        bookingCancellationService.cancelBooking(bookingId);
    }



}
