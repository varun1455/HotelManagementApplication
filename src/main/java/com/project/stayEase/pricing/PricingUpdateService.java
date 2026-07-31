package com.project.stayEase.pricing;

import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.User;
import com.project.stayEase.repository.HotelPricingConfigurationRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PricingUpdateService {

    private final InventoryRepository inventoryRepository;
    private final HotelPricingConfigurationRepository configurationRepository;
    private final PricingContextFactory pricingContextFactory;
    private final InventoryDynamicPricingService inventoryDynamicPricingService;
    private final HotelMinPricingUpdateService hotelMinPricingUpdateService;



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
                pricingContextFactory.findHolidaysAndBuildContext(
                        configuration,
                        startDate,
                        endDate
                );


        inventoryDynamicPricingService.calculateAndUpdateDynamicPrices(inventories, context);
        hotelMinPricingUpdateService.updateHotelMinPrice(hotel, inventories);

    }

    public HotelPricingConfiguration getConfiguration(User owner) {

        return configurationRepository.findByOwner(owner)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Pricing configuration not found for " + owner.getId()));
    }


}
