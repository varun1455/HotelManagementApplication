package com.project.stayEase.controller.booking;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.booking.request.BookingRequestDto;
import com.project.stayEase.dto.booking.response.BookingResponseDto;
import com.project.stayEase.dto.guests.request.GuestRequestDto;
import com.project.stayEase.dto.checkout.PaymentSessionDto;
import com.project.stayEase.service.booking.facade.BookingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Hotel Booking",
        description = "Create, manage, pay for and cancel hotel bookings"
)
@SecurityRequirement(name = "bearerAuth")
public class HotelBookingController {

    private final BookingFacade bookingFacade;

    @Operation(
            summary = "Initialize a booking",
            description = """
                    Creates a new booking for the authenticated user.

                    The booking is created using the selected hotel, room,
                    check-in/check-out dates and number of rooms.
                    """
    )
    @PostMapping("/init")
    public ResponseEntity<ApiResponse<BookingResponseDto>> initializeBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto){
        return new ResponseEntity<>(ApiResponse.successResponse(bookingFacade.createBooking(bookingRequestDto)), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Add guests to a booking",
            description = "Adds guest information to an existing booking."
    )
    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<ApiResponse<BookingResponseDto>> addGuestsToBooking(@RequestBody List<GuestRequestDto> guestRequestDto,@PathVariable Long bookingId){
        return new ResponseEntity<>(ApiResponse.successResponse(bookingFacade.addGuests(bookingId, guestRequestDto)), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Initiate payment for a booking",
            description = """
                    Creates a payment session for the specified booking.

                    The returned payment session can be used by the client
                    to complete the payment with the configured payment provider.
                    """
    )
    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<ApiResponse<PaymentSessionDto>> initiatePayment(@PathVariable Long bookingId){
        return new ResponseEntity<>(ApiResponse.successResponse(bookingFacade.initiatePayment(bookingId)), HttpStatus.OK);
    }

    @Operation(
            summary = "Cancel a booking",
            description = """
                    Cancels the specified booking.

                    Depending on the booking and payment state,
                    inventory is released and a refund may be initiated.
                    """
    )
    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){
        bookingFacade.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}

