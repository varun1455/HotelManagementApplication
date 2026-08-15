package com.project.stayEase.controller.bookingAndPayment;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.bookingMappers.BookingRequestDto;
import com.project.stayEase.dto.bookingMappers.BookingResponseDto;
import com.project.stayEase.dto.bookingMappers.guestMappers.GuestRequestDto;
import com.project.stayEase.dto.bookingMappers.paymentMapper.PaymentSessionDto;
import com.project.stayEase.service.booking.facade.BookingFacade;
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

    private final BookingFacade bookingFacade;

    @PostMapping("/init")
    public ResponseEntity<ApiResponse<BookingResponseDto>> initializeBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto){
        return new ResponseEntity<>(ApiResponse.successResponse(bookingFacade.createBooking(bookingRequestDto)), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<ApiResponse<BookingResponseDto>> addGuestsToBooking(@RequestBody List<GuestRequestDto> guestRequestDto,@PathVariable Long bookingId){
        return new ResponseEntity<>(ApiResponse.successResponse(bookingFacade.addGuests(bookingId, guestRequestDto)), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<ApiResponse<PaymentSessionDto>> initiatePayment(@PathVariable Long bookingId){
        return new ResponseEntity<>(ApiResponse.successResponse(bookingFacade.initiatePayment(bookingId)), HttpStatus.OK);
    }

    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){
        bookingFacade.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}

