package com.project.stayEase.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingSchedular {

    private final PricingUpdateCoordinator pricingUpdateCoordinator;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateDynamicPricing(){
        pricingUpdateCoordinator.updatePrices();
    }

}
