package com.project.stayEase.service;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.UpdatePriceFactorDto;
import com.project.stayEase.entity.HolidayPricingRule;
import com.project.stayEase.pricing.HolidayPricingRefreshService;
import com.project.stayEase.pricing.PricingUpdateService;
import com.project.stayEase.repository.HolidayPricingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayPricingRuleServiceImpl implements HolidayPricingRuleService{

    private final HolidayPricingRuleRepository holidayPricingRuleRepository;
    private final HolidayPricingRefreshService holidayPricingRefreshService;


    @Override
    public void updatePricingFactorOFHolidayType(Long id, UpdatePriceFactorDto priceFactorDto) {
        HolidayPricingRule holidayPricingRule = holidayPricingRuleRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Holiday Type does not exist"));
        holidayPricingRule.setPriceFactor(priceFactorDto.getPriceFactor());
        holidayPricingRuleRepository.save(holidayPricingRule);

        holidayPricingRefreshService.updateDynamicPriceOnHolidayPricingFactor(holidayPricingRule);

    }
}
