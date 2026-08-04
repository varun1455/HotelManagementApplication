package com.project.stayEase.service.pricing.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.service.pricing.context.PricingContext;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy {

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext) {

        BigDecimal price = pricingStrategy.calculatePrice(inventory, pricingContext);

        double occupancy_rate = (double) (inventory.getBookedCount()/inventory.getTotalCount())*100;

        if(occupancy_rate>=pricingContext.getPricingConfiguration().getOccupancyThreshold()){
            price = price.multiply(pricingContext.getPricingConfiguration().getOccupancyFactor());

        }
        return price;
    }
}
