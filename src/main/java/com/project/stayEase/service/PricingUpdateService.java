package com.project.stayEase.service;


import com.project.stayEase.entity.*;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.repository.*;
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
        private final HolidayPricingRuleRepository holidayPricingRuleRepository;


        @Scheduled(cron = "0 * * * * *")
        public void updateDynamicPricingSchedular(){
            updatePrices();
        }

    private PricingContext buildPricingContext(Map<LocalDate, HolidayType> holidays) {

        Map<HolidayType, BigDecimal> holidayFactors =
                holidayPricingRuleRepository.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                HolidayPricingRule::getHolidayType,
                                HolidayPricingRule::getPriceFactor
                        ));

        PricingContext context = new PricingContext();
        context.setHolidays(holidays);
        context.setHolidayFactors(holidayFactors);

        return context;
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

            List<LocalDate> dates = holidayRepository.findDatesByHolidayTypeAndDateBetween(
                    holidayPricingRule.getHolidayType(), today, lastCalculated
            );


            List<Inventory> inventoryList = inventoryRepository.findByDates(dates);

            Map<LocalDate, HolidayType> holidays = holidayRepository.findByDateIn(dates).stream()
                    .collect(Collectors.toMap(
                            Holiday::getDate,
                            Holiday::getType
                    )
            );

            PricingContext context = buildPricingContext(holidays);

            updateInventoryPrices(inventoryList, context);
            Map<Hotel, List<Inventory>> inventoriesByHotel = inventoryList.stream()
                    .collect(Collectors.groupingBy(
                            Inventory::getHotel
                    )
            );

            inventoriesByHotel.forEach(this::updateHotelMinPrice);

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

            Map<LocalDate, HolidayType> holidayList = holidayRepository.findByDateBetween(startDate, endDate).stream()
                    .collect(Collectors.toMap(
                            Holiday::getDate,
                            Holiday::getType

                    ));

            PricingContext context = buildPricingContext(holidayList);

            List<Inventory> inventories = inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);

            if (inventories.isEmpty()) {
                return;
            }

            updateInventoryPrices(inventories, context);
            updateHotelMinPrice(hotel, inventories);
        }

        private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList){

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


        private void updateInventoryPrices(List<Inventory> inventoryList,PricingContext pricingContext){

            inventoryList.forEach(inventory ->{
                        BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory,pricingContext);
                        inventory.setDynamicPrice(dynamicPrice);
                    }
            );

            inventoryRepository.saveAll(inventoryList);
        }
}

