package com.project.stayEase.dto.bookingMappers;

import com.project.stayEase.dto.bookingMappers.guestMappers.GuestResponseDto;
import com.project.stayEase.dto.UserResponseDto;
import com.project.stayEase.entity.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class QueryBookingsDto {
    private Long id;
    private Integer roomsCount;
    private UserResponseDto user;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private BookingStatus bookingStatus;
    private BigDecimal totalPrice;
    private Set<GuestResponseDto> guests = new HashSet<>();
}
