package com.project.stayEase.service.booking.services.creation;

import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Room;
import com.project.stayEase.entity.User;
import com.project.stayEase.entity.enums.BookingStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class BookingFactory {

    public Booking createReservedBooking(User user,
                                         Hotel hotel,
                                         Room room,
                                         LocalDate checkIn,
                                         LocalDate checkOut,
                                         Integer roomsCount,
                                         BigDecimal totalAmount){
        return Booking.builder()
                .hotel(hotel)
                .room(room)
                .user(user)
                .roomsCount(roomsCount)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .bookingStatus(BookingStatus.RESERVED)
                .amount(totalAmount)
                .reservedUntil(LocalDateTime.now().plusMinutes(10))
                .build();
    }

}
