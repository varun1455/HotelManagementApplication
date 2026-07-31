package com.project.stayEase.controller;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.BookingRequestDto;
import com.project.stayEase.dto.BookingResponseDto;
import com.project.stayEase.dto.GuestRequestDto;
import com.project.stayEase.dto.PaymentSessionDto;
import com.project.stayEase.service.BookingService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<BookingResponseDto>> initializeBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto){
        BookingResponseDto bookingResponse =
                bookingService.initializeBooking(bookingRequestDto);

        log.info("Initiate Booking response {}", bookingResponse);
        return new ResponseEntity<>(ApiResponse.successResponse(bookingResponse), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<ApiResponse<BookingResponseDto>> addGuestsToBooking(@RequestBody List<GuestRequestDto> guestRequestDto,@PathVariable Long bookingId){

        return new ResponseEntity<>(ApiResponse.successResponse(bookingService.addGuestsToBooking(guestRequestDto, bookingId)), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<ApiResponse<PaymentSessionDto>> initiatePayment(@PathVariable Long bookingId){

        return new ResponseEntity<>(ApiResponse.successResponse(bookingService.initiatePayment(bookingId)), HttpStatus.OK);
    }

    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){

        bookingService.cancelMyBooking(bookingId);

        return ResponseEntity.noContent().build();
    }
}

