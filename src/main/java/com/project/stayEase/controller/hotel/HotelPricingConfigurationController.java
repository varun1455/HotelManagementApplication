package com.project.stayEase.controller.hotel;

import com.project.stayEase.dto.hotelMappers.HotelPricingConfigurationDto;
import com.project.stayEase.service.pricing.configuration.HotelPricingConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HotelPricingConfigurationController {

    private final HotelPricingConfigurationService hotelPricingConfigurationService;


    @PatchMapping("/priceConfig")
    public ResponseEntity<Void> updateHotelPricingConfiguration(@RequestBody HotelPricingConfigurationDto dto){

        hotelPricingConfigurationService.updateHotelPricingConfiguration(dto);
        return ResponseEntity.noContent().build();
    }








}
