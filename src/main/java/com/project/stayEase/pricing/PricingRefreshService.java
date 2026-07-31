package com.project.stayEase.pricing;


import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.User;
import com.project.stayEase.repository.HotelRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingRefreshService {

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingContextFactory pricingContextFactory;
    private final InventoryDynamicPricingService inventoryDynamicPricingService;
    private final HotelMinPricingUpdateService hotelMinPricingUpdateService;


    public void refreshPrices(User currentUser, HotelPricingConfiguration hotelPricingConfiguration) {


        List<Hotel> hotels = hotelRepository.findByOwner(currentUser);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = inventoryRepository.findLastDynamicPriceDate();

        PricingContext context =
                pricingContextFactory.findHolidaysAndBuildContext(hotelPricingConfiguration, startDate, endDate);

        for (Hotel hotel : hotels) {
            List<Inventory> inventories = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);
            inventoryDynamicPricingService.calculateAndUpdateDynamicPrices(inventories, context);
            hotelMinPricingUpdateService.updateHotelMinPrice(hotel, inventories);
        }
    }
}
