package com.project.stayEase.service.strategy;

import com.project.stayEase.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
