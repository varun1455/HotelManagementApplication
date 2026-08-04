package com.project.stayEase.service.pricing.update;

import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.HotelMinPrice;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.repository.HotelMinPriceRepository;
import lombok.RequiredArgsConstructor;
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
public class HotelMinPricingUpdateService {

    private final HotelMinPriceRepository hotelMinPriceRepository;

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
}
