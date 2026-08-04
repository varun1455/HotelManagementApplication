package com.project.stayEase.service.pricing.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.service.pricing.context.PricingContext;

import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy{

    @Override
    public BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext) {
        return inventory.getRoom().getBasePrice();
    }
}
