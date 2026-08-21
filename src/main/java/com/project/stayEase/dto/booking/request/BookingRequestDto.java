package com.project.stayEase.dto.booking.request;

import com.project.stayEase.customAnnotations.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@ValidDateRange(
        start = "checkInDate",
        end = "checkOutDate"
)
@Schema(description = "Request to initialize a hotel booking")
public class BookingRequestDto {

    @Schema(
            description = "ID of the hotel to book",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long hotelId;

    @Schema(
            description = "ID of the room to book",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long roomId;

    @Schema(
            description = "Check-in date",
            example = "2026-09-10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate checkInDate;

    @Schema(
            description = "Check-out date",
            example = "2026-09-15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate checkOutDate;

    @Schema(
            description = "Number of rooms to reserve",
            example = "2",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer roomsCount;
}
