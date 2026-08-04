package com.project.stayEase.service.pricing.strategy;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.service.pricing.context.PricingContext;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory, PricingContext pricingContext);
}
