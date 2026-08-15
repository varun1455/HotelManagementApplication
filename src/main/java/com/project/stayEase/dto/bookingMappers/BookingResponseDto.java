package com.project.stayEase.dto.bookingMappers;

import com.project.stayEase.dto.bookingMappers.guestMappers.GuestResponseDto;
import com.project.stayEase.dto.bookingMappers.summaryMappers.HotelSummyDtoForBooking;
import com.project.stayEase.dto.bookingMappers.summaryMappers.RoomSummaryDtoForBooking;
import com.project.stayEase.dto.UserResponseDto;
import com.project.stayEase.entity.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
public class BookingResponseDto {

    private Long id;
    private HotelSummyDtoForBooking hotel;
    private RoomSummaryDtoForBooking room;
    private Integer roomsCount;
    private UserResponseDto user;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private BookingStatus bookingStatus;
    private BigDecimal amount;
    private Set<GuestResponseDto> guests = new HashSet<>();
}
