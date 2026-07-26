package com.project.stayEase.controller;

import com.project.stayEase.dto.HotelPricingConfigurationDto;
import com.project.stayEase.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HotelPricingConfigurationController {


    private final InventoryService inventoryService;


    @PatchMapping("/priceConfig")
    public ResponseEntity<Void> updateHotelPricingConfiguration(@RequestBody HotelPricingConfigurationDto dto){

        inventoryService.updatePricingConfiguration(dto);
        return ResponseEntity.noContent().build();
    }








}
