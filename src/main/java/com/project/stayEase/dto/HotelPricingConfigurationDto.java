package com.project.stayEase.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HotelPricingConfigurationDto {
    private BigDecimal surgeFactor;

   @Min(1)
   @Max(15)
    private Integer urgencyDaysThreshold;


    @DecimalMin("1.00")
    @DecimalMax("4.00")
    private BigDecimal urgencyFactor;

    @Min(50)
    @Max(100)
    private Integer occupancyThreshold;

    @DecimalMin("1.00")
    @DecimalMax("4.00")
    private BigDecimal occupancyFactor;

}
