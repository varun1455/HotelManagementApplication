package com.project.stayEase.entity;

import com.project.stayEase.entity.enums.HolidayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class HolidayPricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private HolidayType holidayType;

    @Column(nullable = false)
    private BigDecimal priceFactor;
}
