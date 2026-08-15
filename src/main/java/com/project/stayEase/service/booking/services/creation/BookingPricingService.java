package com.project.stayEase.service.booking.services.creation;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.service.pricing.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingPricingService {

    private final PricingService pricingService;

    public BigDecimal calculateFinalAmount(List<Inventory> inventoryList, Integer roomsCount){
        BigDecimal totalPrice = pricingService.totalPriceOfRoomFromCheckinToCheckoutDate(inventoryList);
        return totalPrice.multiply(BigDecimal.valueOf(roomsCount));
    }
}
