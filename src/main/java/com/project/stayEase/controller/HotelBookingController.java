package com.project.stayEase.controller;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.BookingRequestDto;
import com.project.stayEase.dto.BookingResponseDto;
import com.project.stayEase.dto.GuestRequestDto;
import com.project.stayEase.dto.GuestResponseDto;
import com.project.stayEase.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
@Slf4j
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<ApiResponse<BookingResponseDto>> initializeBooking(@RequestBody BookingRequestDto bookingRequestDto){
        log.info("Initiate Booking response {}", bookingService.initializeBooking(bookingRequestDto));
        return new ResponseEntity<>(ApiResponse.successResponse(bookingService.initializeBooking(bookingRequestDto)), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<ApiResponse<BookingResponseDto>> addGuestsToBooking(@RequestBody List<GuestRequestDto> guestRequestDto,@PathVariable Long bookingId){

        return new ResponseEntity<>(ApiResponse.successResponse(bookingService.addGuestsToBooking(guestRequestDto, bookingId)), HttpStatus.CREATED);
    }
}

