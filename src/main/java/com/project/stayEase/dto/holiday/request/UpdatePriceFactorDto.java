package com.project.stayEase.dto.holiday.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request to update a holiday pricing multiplier")
public class UpdatePriceFactorDto {

    @Schema(
            description = """
                    Multiplier applied to the calculated room price
                    when the inventory date falls on the corresponding
                    holiday type.

                    Example: 1.20 increases the calculated price by 20%.
                    """,
            example = "1.20",
            minimum = "0"
    )
    private BigDecimal priceFactor;
}
