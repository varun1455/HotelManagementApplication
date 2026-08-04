package com.project.stayEase.service.pricing.strategy;


import com.project.stayEase.entity.Inventory;
import com.project.stayEase.service.pricing.context.PricingContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PricingService {

    private final PricingStrategy pricingStrategy;

    public PricingService() {
        PricingStrategy strategy = new BasePricingStrategy();
        strategy = new SurgePricingStrategy(strategy);
        strategy = new OccupancyPricingStrategy(strategy);
        strategy = new UrgencyPricingStrategy(strategy);
        strategy = new HolidayPricingStrategy(strategy);

        this.pricingStrategy = strategy;
    }


    public BigDecimal calculateDynamicPricing(Inventory inventory, PricingContext pricingContext ){
        return pricingStrategy.calculatePrice(inventory, pricingContext);
    }

    public BigDecimal totalPriceOfRoomFromCheckinToCheckoutDate(List<Inventory> inventoryList){
        return inventoryList.stream()
                .map(inventory ->
                        inventory.getDynamicPrice() != null
                                ? inventory.getDynamicPrice()
                                : inventory.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}


