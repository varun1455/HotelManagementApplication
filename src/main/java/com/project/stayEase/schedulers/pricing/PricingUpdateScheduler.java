package com.project.stayEase.schedulers.pricing;

import com.project.stayEase.service.pricing.coordinator.PricingUpdateCoordinator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingUpdateScheduler {

    private final PricingUpdateCoordinator pricingUpdateCoordinator;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateDynamicPricing(){
        pricingUpdateCoordinator.updatePrices();
    }

}
