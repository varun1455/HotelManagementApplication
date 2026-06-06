package com.project.stayEase.service;


import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.HotelMinPrice;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.repository.HotelMinPriceRepository;
import com.project.stayEase.repository.HotelRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.service.strategy.PricingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PricingUpdateService {

        private final HotelRepository hotelRepository;
        private final HotelMinPriceRepository hotelMinPriceRepository;
        private final InventoryRepository inventoryRepository;
        private final PricingService pricingService;


        @Scheduled(cron = "0 0 * * * *")
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

        private void updateHotelPrices(Hotel hotel){
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusMonths(6);

            List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);

            updateInventoryPrices(inventoryList);

            updateHotelMinPrice(hotel, inventoryList, startDate, endDate);


        }

        private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate){

            Map<LocalDate, BigDecimal> dailyMinPrice = inventoryList.stream()
                    .collect(Collectors.
                            groupingBy(Inventory::getDate,
                                    Collectors.collectingAndThen(
                                            Collectors.minBy(
                                                    Comparator.comparing(
                                                            Inventory::getPrice
                                                    )
                                             ),inventory->inventory.get().getPrice()
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

        private void updateInventoryPrices(List<Inventory> inventoryList){

            inventoryList.forEach(inventory ->{
                        BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
                        inventory.setPrice(dynamicPrice);
                    }
                    );

            inventoryRepository.saveAll(inventoryList);
        }
}

