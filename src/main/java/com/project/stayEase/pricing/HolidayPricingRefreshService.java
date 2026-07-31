package com.project.stayEase.pricing;

import com.project.stayEase.entity.*;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.repository.HolidayRepository;
import com.project.stayEase.repository.HotelRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HolidayPricingRefreshService {

    private final InventoryRepository inventoryRepository;
    private final HolidayRepository holidayRepository;
    private final PricingContextFactory pricingContextFactory;
    private final HotelMinPricingUpdateService hotelMinPricingUpdateService;
    private final InventoryDynamicPricingService inventoryDynamicPricingService;
    private final PricingUpdateService pricingUpdateService;

    public void updateDynamicPriceOnHolidayPricingFactor(HolidayPricingRule holidayPricingRule){

            LocalDate today = LocalDate.now();
            LocalDate lastCalculated = inventoryRepository.findLastDynamicPriceDate();

            List<Holiday> holidays =
                    holidayRepository.findByTypeAndDateBetween(
                            holidayPricingRule.getHolidayType(),
                            today,
                            lastCalculated
                    );

            List<LocalDate> dates = holidays.stream()
                    .map(Holiday::getDate)
                    .toList();

            List<Inventory> inventoryList = inventoryRepository.findByDates(dates);

            Map<Hotel, List<Inventory>> inventoriesByHotel = inventoryList.stream()
                    .collect(Collectors.groupingBy(
                                    Inventory::getHotel
                            )
            );

            Map<LocalDate, HolidayType> holidayMap = holidays.stream()
                    .collect(Collectors.toMap(
                            Holiday::getDate,
                            Holiday::getType
                    ));



            inventoriesByHotel.forEach((hotel, inventories) -> {

                HotelPricingConfiguration configuration =
                        pricingUpdateService.getConfiguration(hotel.getOwner());

                PricingContext context =
                        pricingContextFactory.buildHolidayPricingContext(holidayMap,configuration);

                inventoryDynamicPricingService.calculateAndUpdateDynamicPrices(inventoryList, context);
                hotelMinPricingUpdateService.updateHotelMinPrice(hotel, inventoryList);

            });
        }

}
