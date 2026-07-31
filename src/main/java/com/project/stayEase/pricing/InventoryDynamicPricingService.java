package com.project.stayEase.pricing;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.service.strategy.PricingService;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryDynamicPricingService {

    private final PricingService pricingService;
    private final InventoryRepository inventoryRepository;

    public void calculateAndUpdateDynamicPrices(List<Inventory> inventoryList, PricingContext pricingContext){

        inventoryList.forEach(inventory ->{
                    BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory,pricingContext);
                    inventory.setDynamicPrice(dynamicPrice);
                }
        );


        inventoryRepository.saveAll(inventoryList);
    }
}
