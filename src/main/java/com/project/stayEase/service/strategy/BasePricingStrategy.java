package com.project.stayEase.service.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.util.PricingContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy{

    @Override
    public BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext) {
        return inventory.getRoom().getBasePrice();
    }
}
