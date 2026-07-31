package com.project.stayEase.dto;

import com.project.stayEase.customAnnotations.ValidDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
@ValidDateRange(
        start = "startDate",
        end = "endDate"
)
public class HotelSearchRequestDto {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomsCount;

    private Integer page = 0;
    private Integer size = 10;
}
