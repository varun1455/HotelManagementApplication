package com.project.stayEase.pricing;

import com.project.stayEase.entity.Holiday;
import com.project.stayEase.entity.HolidayPricingRule;
import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.repository.HolidayPricingRuleRepository;
import com.project.stayEase.repository.HolidayRepository;
import com.project.stayEase.util.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PricingContextFactory {

    private final HolidayRepository holidayRepository;
    private final HolidayPricingRuleRepository holidayPricingRuleRepository;

    public PricingContext buildHolidayPricingContext(Map<LocalDate, HolidayType> holidays,HotelPricingConfiguration configuration) {

        Map<HolidayType, BigDecimal> holidayFactors =
                holidayPricingRuleRepository.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                HolidayPricingRule::getHolidayType,
                                HolidayPricingRule::getPriceFactor
                        ));

        PricingContext context = new PricingContext();
        context.setHolidays(holidays);
        context.setPricingConfiguration(configuration);
        context.setHolidayFactors(holidayFactors);

        return context;
    }

    public PricingContext findHolidaysAndBuildContext(HotelPricingConfiguration configuration, LocalDate startDate, LocalDate endDate){
        Map<LocalDate, HolidayType> holidays =
                holidayRepository.findByDateBetween(startDate, endDate)
                        .stream()
                        .collect(Collectors.toMap(
                                Holiday::getDate,
                                Holiday::getType
                        ));

       return buildHolidayPricingContext(holidays, configuration);


    }
}
