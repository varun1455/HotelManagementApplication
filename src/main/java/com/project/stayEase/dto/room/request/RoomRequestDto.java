package com.project.stayEase.dto.room.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.math.BigDecimal;


@Data
@Schema(description = "Request for creating or updating a room")
public class RoomRequestDto {

    @Schema(
            description = "Room type ID",
            example = "1"
    )
    private Long roomTypeId;

    @Schema(
            description = "Bed type ID",
            example = "2"
    )
    private Long bedTypeId;

    @Schema(
            description = "Base price of the room per night",
            example = "600.00"
    )
    private BigDecimal basePrice;

    @Schema(
            description = "URLs of room photos",
            example = "[\"https://example.com/room1.jpg\"]"
    )
    private String[] photos;

    @Schema(
            description = "Amenities available in the room",
            example = "[\"WiFi\", \"AC\", \"TV\"]"
    )
    private String[] amenities;

    @Schema(
            description = "Total number of rooms of this type",
            example = "10",
            minimum = "1"
    )
    private Integer totalCount;

    @Schema(
            description = "Maximum number of guests",
            example = "2",
            minimum = "1"
    )
    private Integer capacity;


}
