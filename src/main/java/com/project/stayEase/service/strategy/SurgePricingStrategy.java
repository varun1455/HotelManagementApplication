package com.project.stayEase.service.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class SurgePricingStrategy implements PricingStrategy{

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext) {
        BigDecimal price = pricingStrategy.calculatePrice(inventory, pricingContext);
        return price.multiply(pricingContext.getPricingConfiguration().getSurgeFactor());
    }
}
