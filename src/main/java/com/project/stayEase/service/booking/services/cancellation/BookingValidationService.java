package com.project.stayEase.service.booking.services.cancellation;

import com.project.stayEase.customExceptions.InvalidBookingStateException;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.enums.BookingStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BookingValidationService {

    public void validateCancellation(Booking booking){
        if(booking.getBookingStatus() != BookingStatus.CONFIRMED){
            throw new InvalidBookingStateException("You cannot cancel this booking");
        }
        if (booking.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingStateException(
                    "You cannot cancel a booking after the checkIn date"
            );
        }

    }
}
