package com.project.stayEase.service.pricing.context;

import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.enums.HolidayType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;


@Getter
@Setter
public class PricingContext {
    private Map<LocalDate, HolidayType> holidays;
    private Map<HolidayType, BigDecimal> holidayFactors;
    private HotelPricingConfiguration pricingConfiguration;

}
