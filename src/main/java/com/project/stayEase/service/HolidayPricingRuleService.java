package com.project.stayEase.service;

import com.project.stayEase.dto.UpdatePriceFactorDto;

public interface HolidayPricingRuleService {

    void updatePricingFactorOFHolidayType(Long id, UpdatePriceFactorDto priceFactorDto);

}
