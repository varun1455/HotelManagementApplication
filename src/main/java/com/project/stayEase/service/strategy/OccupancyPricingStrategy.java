package com.project.stayEase.service.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy {

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext) {
        BigDecimal price = pricingStrategy.calculatePrice(inventory, pricingContext);
        double occupancy_rate = (double) inventory.getBookedCount()/inventory.getTotalCount();
        if(occupancy_rate>=0.8){
            price = price.multiply(BigDecimal.valueOf(1.5));

        }
        return price;
    }
}
