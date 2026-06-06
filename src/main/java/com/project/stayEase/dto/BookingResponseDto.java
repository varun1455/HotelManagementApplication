package com.project.stayEase.dto;

import com.project.stayEase.entity.User;
import com.project.stayEase.entity.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Data
public class BookingResponseDto {

    private Long id;
    private HotelSummyDtoForBooking hotel;
    private RoomSummaryDtoForBooking room;
    private Integer roomsCount;
    private User user;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private BookingStatus bookingStatus;
    private Set<GuestResponseDto> guests;
}
