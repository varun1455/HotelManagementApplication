package com.project.stayEase.util;

import com.project.stayEase.entity.HolidayPricingRule;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.repository.HolidayPricingRuleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class HolidayPricingSeeder {

    private final HolidayPricingRuleRepository holidayPricingRuleRepository;

    @PostConstruct
    public void seed() {

        if (holidayPricingRuleRepository.count() > 0) {
            return;
        }

        Arrays.stream(HolidayType.values())
            .forEach(type -> {

                HolidayPricingRule rule = new HolidayPricingRule();

                rule.setHolidayType(type);
                rule.setPriceFactor(BigDecimal.ONE);

                holidayPricingRuleRepository.save(rule);
            }
        );
    }

}
