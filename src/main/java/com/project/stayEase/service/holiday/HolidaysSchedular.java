package com.project.stayEase.service.holiday;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HolidaysSchedular {

    private final PublicHolidaysService publicHolidaysService;

    @Scheduled(cron = "0 0 1 1 1 *")     ///runs only once in a year
    public void updateYearlyHolidays(){
        publicHolidaysService.syncHolidays(LocalDate.now().getYear());
    }
}
