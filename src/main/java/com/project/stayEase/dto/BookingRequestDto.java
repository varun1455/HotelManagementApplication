package com.project.stayEase.dto;

import com.project.stayEase.customAnnotations.ValidDateRange;
import com.project.stayEase.entity.Guest;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Room;
import com.project.stayEase.entity.User;
import com.project.stayEase.entity.enums.BookingStatus;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

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
