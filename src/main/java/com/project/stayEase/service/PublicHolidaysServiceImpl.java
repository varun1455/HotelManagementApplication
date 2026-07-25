package com.project.stayEase.service;


import com.project.stayEase.entity.Holiday;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.repository.HolidayRepository;
import com.project.stayEase.util.CalendarificResponse;
import com.project.stayEase.util.HolidayDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicHolidaysServiceImpl implements PublicHolidaysService {


    private final HolidayRepository holidayRepository;

    @Override
//    @Scheduled(cron = "0 0 1 1 * ") runs only once in a year
    public void storeHolidaysToDB(CalendarificResponse calendarificResponse) {

        /// Get all the holidays from the response and store it in the list
        List<Holiday> holidayList = calendarificResponse.response().holidays()
                .stream()
                .map(dto->{
                    Holiday holiday = new Holiday();
                    holiday.setName(dto.name());
                    holiday.setDate(dto.date().iso());
                    holiday.setType(HolidayType.fromApiTypes(dto.type()));

                    return holiday;
                }).toList();

        /// There may be multiple holidays on the same date in the list so store the holiday with higher precedence
        Map<LocalDate, Holiday> holidayTypeDateMap = holidayList.stream()
                        .collect(Collectors.toMap(
                                Holiday::getDate,
                                Function.identity(),
                                (h1, h2) -> h1.getType().getPrecedence() < h2.getType().getPrecedence()
                                                ? h1 : h2
                                )
                        );

        holidayRepository.saveAll(holidayTypeDateMap.values());
    }

}
