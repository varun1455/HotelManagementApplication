package com.project.stayEase.service;

import com.project.stayEase.dto.BookingRequestDto;
import com.project.stayEase.dto.BookingResponseDto;
import com.project.stayEase.dto.GuestRequestDto;

import java.util.List;

public interface BookingService {

    public BookingResponseDto initializeBooking(BookingRequestDto bookingRequestDto);

    public BookingResponseDto addGuestsToBooking(List<GuestRequestDto> guestRequestDto, Long bookingId);
}
