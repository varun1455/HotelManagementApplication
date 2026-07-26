package com.project.stayEase.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
public class HotelPricingConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User owner;

    @Column(nullable = false)
    private BigDecimal surgeFactor;

    @Column(nullable = false)
    @Min(value = 1, message = "Urgency days threshold must be at least 1")
    @Max(value = 15, message = "Urgency days threshold cannot exceed 15")
    private Integer urgencyDaysThreshold;

    @Column(nullable = false)
    @DecimalMin(value = "1.00", message = "Factor must be at least 1.00")
    @DecimalMax(value = "4.00", message = "Factor cannot exceed 4.00")
    private BigDecimal urgencyFactor;

    @Column(nullable = false)
    @Min(value = 50, message = "Occupancy threshold cannot be less than 50")
    @Max(value = 100, message = "Occupancy threshold cannot exceed 100")
    private Integer occupancyThreshold;

    @Column(nullable = false)
    @DecimalMin(value = "1.00", message = "Factor must be at least 1.00")
    @DecimalMax(value = "4.00", message = "Factor cannot exceed 4.00")
    private BigDecimal occupancyFactor;

}
