package com.project.stayEase.dto.search.request;

import com.project.stayEase.customAnnotations.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@ValidDateRange(
        start = "startDate",
        end = "endDate"
)
@Schema(
        description = "Hotel search criteria"
)
public class HotelSearchRequestDto {

    @Schema(
            description = "City where the hotel should be located",
            example = "Delhi"
    )
    private String city;

    @Schema(
            description = "Check-in date",
            example = "2026-09-01"
    )
    private LocalDate startDate;

    @Schema(
            description = "Check-out date",
            example = "2026-09-05"
    )
    private LocalDate endDate;

    @Schema(
            description = "Number of rooms required",
            example = "2",
            minimum = "1"
    )
    private Integer roomsCount;

    @Schema(
            description = "Page number (zero-based)",
            example = "0",
            defaultValue = "0",
            minimum = "0"
    )
    private Integer page = 0;

    @Schema(
            description = "Number of hotels returned per page",
            example = "10",
            defaultValue = "10",
            minimum = "1"
    )
    private Integer size = 10;
}
