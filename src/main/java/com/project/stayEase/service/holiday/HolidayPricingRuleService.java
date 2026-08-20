package com.project.stayEase.service.holiday;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.service.holiday.client.dto.UpdatePriceFactorDto;
import com.project.stayEase.entity.HolidayPricingRule;
import com.project.stayEase.service.pricing.update.HolidayPricingRefreshService;
import com.project.stayEase.repository.HolidayPricingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayPricingRuleService {

    private final HolidayPricingRuleRepository holidayPricingRuleRepository;
    private final HolidayPricingRefreshService holidayPricingRefreshService;

    public void updatePricingFactorOFHolidayType(Long id, UpdatePriceFactorDto priceFactorDto) {
        HolidayPricingRule holidayPricingRule = holidayPricingRuleRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Holiday Type does not exist"));
        holidayPricingRule.setPriceFactor(priceFactorDto.getPriceFactor());
        holidayPricingRuleRepository.save(holidayPricingRule);

        holidayPricingRefreshService.updateDynamicPriceOnHolidayPricingFactor(holidayPricingRule);

    }
}
