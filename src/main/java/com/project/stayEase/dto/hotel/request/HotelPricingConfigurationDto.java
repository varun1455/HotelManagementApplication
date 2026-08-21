package com.project.stayEase.dto.hotel.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(
        description = "Configuration used by the hotel's dynamic pricing engine"
)
public class HotelPricingConfigurationDto {
    @Schema(
            description = "Multiplier applied during surge pricing",
            example = "1.10",
            minimum = "1.0"
    )
    private BigDecimal surgeFactor;

    @Schema(
            description = "Number of days before check-in at which urgency pricing starts",
            example = "7",
            minimum = "1",
            maximum = "15"
    )
    private Integer urgencyDaysThreshold;


    @Schema(
            description = "Multiplier applied during urgency pricing",
            example = "1.20",
            minimum = "1.0"
    )
    private BigDecimal urgencyFactor;

    @Schema(
            description = "Occupancy percentage at which occupancy pricing is triggered",
            example = "80",
            minimum = "0",
            maximum = "100"
    )
    private Integer occupancyThreshold;

    @Schema(
            description = "Multiplier applied when occupancy threshold is reached",
            example = "1.15",
            minimum = "1.0",
            maximum = "4.00"
    )
    private BigDecimal occupancyFactor;

}
