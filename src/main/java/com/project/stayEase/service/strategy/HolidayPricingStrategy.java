package com.project.stayEase.service.strategy;

import com.project.stayEase.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
       BigDecimal price = pricingStrategy.calculatePrice(inventory);
       boolean isTodayHoliday = true;
       if(isTodayHoliday){
           price = price.multiply(BigDecimal.valueOf(1.25));
       }
       return price;
    }
}
