package com.project.stayEase.service.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy{

    private final PricingStrategy pricingStrategy;


    @Override
    public BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext) {
        BigDecimal price = pricingStrategy.calculatePrice(inventory, pricingContext);
        LocalDate today = LocalDate.now();

        if(inventory.getDate().isBefore(today.plusDays(7))){
            price = price.multiply(BigDecimal.valueOf(1.8));
        }
        return price;
    }
}
