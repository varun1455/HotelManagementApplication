package com.project.stayEase.service;


import com.project.stayEase.entity.*;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.repository.*;
import com.project.stayEase.service.Factory.PricingContextFactory;
import com.project.stayEase.service.strategy.PricingService;
import com.project.stayEase.util.PricingContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PricingUpdateService {

        private final HotelRepository hotelRepository;
        private final HotelMinPriceRepository hotelMinPriceRepository;
        private final InventoryRepository inventoryRepository;
        private final PricingService pricingService;
        private final HolidayRepository holidayRepository;
        private final PricingContextFactory pricingContextFactory;
        private final HotelPricingConfigurationService hotelPricingConfigurationService;


        @Scheduled(cron = "0 * * * * *")
        public void updateDynamicPricingSchedular(){
            updatePrices();
        }


        public void updatePrices(){
            int page = 0;
            int batchSize = 100;
            while(true){
                Page<Hotel> hotelsPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
                if(hotelsPage.isEmpty()){
                    break;
                }

                hotelsPage.getContent().forEach(this::updateHotelPrices);

                page++;

            }
        }


        public void updateDynamicPriceOnHolidayPricingFactorUpdate(HolidayPricingRule holidayPricingRule){

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
                        hotelPricingConfigurationService.getOrCreate(hotel.getOwner());

                PricingContext context =
                        pricingContextFactory.buildHolidayPricingContext(holidayMap,configuration);

                updateInventoryPrices(inventories, context);

                updateHotelMinPrice(hotel, inventories);
            });
        }
        private void updateHotelPrices(Hotel hotel) {

            LocalDate today = LocalDate.now();

            LocalDate lastCalculated = inventoryRepository.findLastDynamicPriceDateByHotel(hotel.getId());

            LocalDate startDate;
            LocalDate endDate;

            if (lastCalculated == null) {
                startDate = today;
                endDate = today.plusDays(59);
            } else {
                startDate = lastCalculated.plusDays(1);
                endDate = startDate;
            }

            List<Inventory> inventories = inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);

            if (inventories.isEmpty()) {
                return;
            }

            HotelPricingConfiguration configuration =
                    hotelPricingConfigurationService.getOrCreate(
                            hotel.getOwner()
            );


            PricingContext context =
                    pricingContextFactory.findHolidaysAndBuildContext(
                            configuration,
                            startDate,
                            endDate
                    );


            updateInventoryPrices(inventories, context);
            updateHotelMinPrice(hotel, inventories);
        }

        public void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList){

            Map<LocalDate, BigDecimal> dailyMinPrice = inventoryList.stream()
                    .collect(Collectors.
                            groupingBy(Inventory::getDate,
                                    Collectors.collectingAndThen(
                                            Collectors.minBy(
                                                    Comparator.comparing(
                                                            Inventory::getDynamicPrice
                                                    )
                                             ),inventory->inventory.get().getDynamicPrice()
                                     )
                            )


                    );

            List<HotelMinPrice>hotelPrices= new ArrayList<>();
            dailyMinPrice.forEach((date, price)->{
                    HotelMinPrice hotelPrice =hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                            .orElse(new HotelMinPrice(hotel, date));
                    hotelPrice.setPrice(price);
                    hotelPrices.add(hotelPrice);

            });

            hotelMinPriceRepository.saveAll(hotelPrices);
        }


        public void updateInventoryPrices(List<Inventory> inventoryList,PricingContext pricingContext){

            inventoryList.forEach(inventory ->{
                        BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory,pricingContext);
                        inventory.setDynamicPrice(dynamicPrice);
                    }
            );

            inventoryRepository.saveAll(inventoryList);
        }
}

