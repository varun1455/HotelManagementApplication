package com.project.stayEase.service.holiday.services;


import com.project.stayEase.entity.Holiday;
import com.project.stayEase.entity.enums.HolidayType;
import com.project.stayEase.repository.HolidayRepository;
import com.project.stayEase.dto.holiday.response.CalendarificResponse;
import com.project.stayEase.service.holiday.client.CalendarificClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicHolidaysService {


    private final HolidayRepository holidayRepository;
    private final CalendarificClient calendarificClient;

    public CalendarificResponse syncHolidays(int year) {

        CalendarificResponse response = calendarificClient.fetchHolidays(year);
        saveHolidays(response);
        return response;
    }

    public void saveHolidays(CalendarificResponse calendarificResponse) {

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
