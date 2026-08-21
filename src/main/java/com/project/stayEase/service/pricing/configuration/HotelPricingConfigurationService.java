package com.project.stayEase.service.pricing.configuration;


import com.project.stayEase.configuration.properties.DefaultPricingProperties;
import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.hotel.request.HotelPricingConfigurationDto;
import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.User;
import com.project.stayEase.repository.HotelPricingConfigurationRepository;
import com.project.stayEase.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelPricingConfigurationService {

    private  final HotelPricingConfigurationRepository hotelPricingConfigurationRepository;
    private final SecurityUtils securityUtils;
    private final PricingConfigurationUpdateService pricingConfigurationUpdateService;
    private final DefaultPricingProperties defaultPricingProperties;

    public HotelPricingConfiguration get(User owner) {
        return hotelPricingConfigurationRepository.findByOwner(owner)
                .orElseThrow(()->new ResourceNotFoundException("Hotel Price Configuration not exist for current admin"));
    }


    public void initializeDefaultHotelPriceConfigurationForHotelManager(User owner){
        HotelPricingConfiguration config = new HotelPricingConfiguration();
        config.setOwner(owner);
        config.setSurgeFactor(defaultPricingProperties.getSurgeFactor());

        config.setUrgencyDaysThreshold(defaultPricingProperties.getUrgencyDaysThreshold());

        config.setUrgencyFactor(defaultPricingProperties.getUrgencyFactor());

        config.setOccupancyThreshold(defaultPricingProperties.getOccupancyThreshold());

        config.setOccupancyFactor(defaultPricingProperties.getOccupancyFactor());

        hotelPricingConfigurationRepository.save(config);
    }

    public void updateHotelPricingConfiguration(HotelPricingConfigurationDto dto){
        User currentUser = securityUtils.getCurrentuser();

        HotelPricingConfiguration hotelPricingConfiguration = get(currentUser);


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

        pricingConfigurationUpdateService.refreshPrices(currentUser, hotelPricingConfiguration);
    }

}
