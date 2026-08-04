package com.project.stayEase.service.pricing.update;

import com.project.stayEase.entity.*;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.service.pricing.calculation.PricingCalculationService;
import com.project.stayEase.service.pricing.context.PricingContextBuilder;
import com.project.stayEase.repository.HolidayRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.service.pricing.context.PricingContext;
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
    private final PricingContextBuilder pricingContextBuilder;
    private final PricingWindowUpdateService pricingWindowUpdateService;
    private final PricingCalculationService pricingCalculationService;

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
                        pricingWindowUpdateService.getConfiguration(hotel.getOwner());

                PricingContext context =
                        pricingContextBuilder.buildHolidayPricingContext(holidayMap,configuration);

                pricingCalculationService.recalculatePricing(hotel, inventories, context);

            });
        }

}
