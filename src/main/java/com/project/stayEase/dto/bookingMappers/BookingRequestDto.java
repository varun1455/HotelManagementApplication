package com.project.stayEase.dto.bookingMappers;

import com.project.stayEase.customAnnotations.ValidDateRange;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@ValidDateRange(
        start = "checkInDate",
        end = "checkOutDate"
)
public class BookingRequestDto {

    private Long hotelId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer roomsCount;
}
