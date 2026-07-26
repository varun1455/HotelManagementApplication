package com.project.stayEase.service;


import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.User;
import com.project.stayEase.repository.HotelPricingConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HotelPricingConfigurationService {

    private  final HotelPricingConfigurationRepository hotelPricingConfigurationRepository;

    public HotelPricingConfiguration getOrCreate(User owner) {
        return hotelPricingConfigurationRepository.findByOwner(owner)
                .orElseGet(() -> initializeDefaultHotelPriceConfigurationForHotelManager(owner));
    }


    public HotelPricingConfiguration initializeDefaultHotelPriceConfigurationForHotelManager(User owner){
        HotelPricingConfiguration config = new HotelPricingConfiguration();
        config.setOwner(owner);
        config.setSurgeFactor(BigDecimal.ONE);
        config.setUrgencyDaysThreshold(7);
        config.setUrgencyFactor(BigDecimal.valueOf(1.20));
        config.setOccupancyThreshold(80);
        config.setOccupancyFactor(BigDecimal.valueOf(1.15));

        return hotelPricingConfigurationRepository.save(config);
    }
}
