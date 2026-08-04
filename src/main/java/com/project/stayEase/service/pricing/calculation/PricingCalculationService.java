package com.project.stayEase.service.pricing.calculation;

import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.service.pricing.context.PricingContext;
import com.project.stayEase.service.pricing.update.HotelMinPricingUpdateService;
import com.project.stayEase.service.pricing.update.InventoryDynamicPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingCalculationService {

    private final HotelMinPricingUpdateService hotelMinPricingUpdateService;
    private final InventoryDynamicPricingService inventoryDynamicPricingService;

    public void recalculatePricing(
            Hotel hotel,
            List<Inventory> inventories,
            PricingContext context){

        inventoryDynamicPricingService.calculateAndUpdateDynamicPrices(inventories, context);
        hotelMinPricingUpdateService.updateHotelMinPrice(hotel, inventories);

    }
}
