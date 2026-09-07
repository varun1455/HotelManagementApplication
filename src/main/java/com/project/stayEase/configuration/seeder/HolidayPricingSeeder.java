package com.project.stayEase.configuration.seeder;

import com.project.stayEase.configuration.properties.SeedProperties;
import com.project.stayEase.entity.HolidayPricingRule;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.repository.HolidayPricingRuleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class HolidayPricingSeeder {

    private final HolidayPricingRuleRepository holidayPricingRuleRepository;
    private final SeedProperties seedProperties;


    @PostConstruct
    public void seed() {

        if (!seedProperties.isEnabled() || holidayPricingRuleRepository.count() > 0) {
            return;
        }

        for (HolidayType type : HolidayType.values()) {

            HolidayPricingRule rule = new HolidayPricingRule();

            rule.setHolidayType(type);
            rule.setPriceFactor(
                    seedProperties
                            .getHolidayPricing()
                            .getOrDefault(type, BigDecimal.ONE)
            );

            holidayPricingRuleRepository.save(rule);
        }
    }

}
