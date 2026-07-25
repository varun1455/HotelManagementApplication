package com.project.stayEase.service.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext) {
       BigDecimal price = pricingStrategy.calculatePrice(inventory, pricingContext);
       HolidayType holidayType = pricingContext.getHolidays().get(inventory.getDate());
        if (holidayType == null) {
            return price;
        }
        BigDecimal factor = pricingContext.getHolidayFactors().getOrDefault(holidayType, BigDecimal.ONE);
       return price.multiply(factor);
    }
}
