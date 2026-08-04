package com.project.stayEase.service.pricing.update;

import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.User;
import com.project.stayEase.service.pricing.calculation.PricingCalculationService;
import com.project.stayEase.service.pricing.context.PricingContextBuilder;
import com.project.stayEase.repository.HotelPricingConfigurationRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.service.pricing.context.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PricingWindowUpdateService {

    private final InventoryRepository inventoryRepository;
    private final HotelPricingConfigurationRepository configurationRepository;
    private final PricingContextBuilder pricingContextBuilder;
    private final PricingCalculationService pricingCalculationService;

    public void updateRollingWindowPricing(Hotel hotel){

        LocalDate today = LocalDate.now();
        LocalDate targetEndDate = today.plusDays(59);

        LocalDate lastCalculated = inventoryRepository.findLastDynamicPriceDateByHotel(hotel.getId());

        if (lastCalculated == null) {
            refreshPricing(hotel, today, targetEndDate);
            return;
        }

        if (!lastCalculated.isBefore(targetEndDate)) {
            return;
        }

        refreshPricing(hotel, lastCalculated.plusDays(1), targetEndDate);


    }

    public void updateUrgencyWindowPricing(Hotel hotel){
        int urgencyDaysThreshold = getConfiguration(hotel.getOwner()).getUrgencyDaysThreshold()-1;
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(urgencyDaysThreshold);

        refreshPricing(hotel, today, endDate);


    }

    private void refreshPricing(
            Hotel hotel,
            LocalDate startDate,
            LocalDate endDate) {


        List<Inventory> inventories = inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);

        if (inventories.isEmpty()) {
            return;
        }

        HotelPricingConfiguration configuration = getConfiguration(hotel.getOwner());


        PricingContext context =
                pricingContextBuilder.findHolidaysAndBuildContext(
                        configuration,
                        startDate,
                        endDate
                );

        pricingCalculationService.recalculatePricing(hotel, inventories, context);

    }

    public HotelPricingConfiguration getConfiguration(User owner) {

        return configurationRepository.findByOwner(owner)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Pricing configuration not found for " + owner.getId()));
    }


}
