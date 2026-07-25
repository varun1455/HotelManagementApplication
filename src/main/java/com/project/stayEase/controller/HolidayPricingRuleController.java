package com.project.stayEase.controller;

import com.project.stayEase.dto.UpdatePriceFactorDto;
import com.project.stayEase.service.HolidayPricingRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system/pricingrule")
public class HolidayPricingRuleController {

    private final HolidayPricingRuleService holidayPricingRuleService;

    @PatchMapping("/holidayType/{id}")
    public ResponseEntity<Void> updatePricingFactorForHolidayType(@PathVariable Long id, @RequestBody UpdatePriceFactorDto priceFactorDto){
            holidayPricingRuleService.updatePricingFactorOFHolidayType(id, priceFactorDto);
        return ResponseEntity.noContent().build();

    }
}
