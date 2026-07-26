package com.project.stayEase.service;

import com.project.stayEase.dto.*;
import com.stripe.model.Event;

import java.util.List;

public interface BookingService {

    public BookingResponseDto initializeBooking(BookingRequestDto bookingRequestDto);

    public BookingResponseDto addGuestsToBooking(List<GuestRequestDto> guestRequestDto, Long bookingId);

    public PaymentSessionDto initiatePayment(Long bookingId);

    public void capturePaymentEvent(Event event);

    void cancelMyBooking(Long bookingId);

    List<BookingsPerHotelDto> getAllBookingsByHotelId(Long hotelId);

}
