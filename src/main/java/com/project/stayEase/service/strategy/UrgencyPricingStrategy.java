package com.project.stayEase.service.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy{

    private final PricingStrategy pricingStrategy;


    @Override
    public BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext) {
        BigDecimal price = pricingStrategy.calculatePrice(inventory, pricingContext);
        LocalDate today = LocalDate.now();

        int urgencyDays = pricingContext.getPricingConfiguration()
                .getUrgencyDaysThreshold();

        LocalDate urgencyCutoff = today.plusDays(urgencyDays);

        if(inventory.getDate().isBefore(urgencyCutoff)){
            price = price.multiply(BigDecimal.valueOf(1.8));
        }
        return price;
    }
}
