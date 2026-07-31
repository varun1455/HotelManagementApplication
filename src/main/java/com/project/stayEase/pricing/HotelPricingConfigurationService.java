package com.project.stayEase.pricing;


import com.project.stayEase.dto.HotelPricingConfigurationDto;
import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.User;
import com.project.stayEase.repository.HotelPricingConfigurationRepository;
import com.project.stayEase.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HotelPricingConfigurationService {

    private  final HotelPricingConfigurationRepository hotelPricingConfigurationRepository;
    private final SecurityUtils securityUtils;
    private final PricingRefreshService pricingRefreshService;

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

    public void updateHotelPricingConfiguration(HotelPricingConfigurationDto dto){
        User currentUser = securityUtils.getCurrentuser();

        HotelPricingConfiguration hotelPricingConfiguration = getOrCreate(currentUser);


        if (dto.getSurgeFactor() != null) {
            hotelPricingConfiguration.setSurgeFactor(dto.getSurgeFactor());
        }

        if (dto.getUrgencyDaysThreshold() != null) {
            hotelPricingConfiguration.setUrgencyDaysThreshold(dto.getUrgencyDaysThreshold());
        }

        if (dto.getUrgencyFactor() != null) {
            hotelPricingConfiguration.setUrgencyFactor(dto.getUrgencyFactor());
        }

        if (dto.getOccupancyThreshold() != null) {
            hotelPricingConfiguration.setOccupancyThreshold(dto.getOccupancyThreshold());
        }

        if (dto.getOccupancyFactor() != null) {
            hotelPricingConfiguration.setOccupancyFactor(dto.getOccupancyFactor());
        }
        hotelPricingConfigurationRepository.save(hotelPricingConfiguration);

        pricingRefreshService.refreshPrices(currentUser, hotelPricingConfiguration);
    }

}
