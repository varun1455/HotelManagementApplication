package com.project.stayEase.controller.holiday;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.service.holiday.PublicHolidaysService;
import com.project.stayEase.service.holiday.client.CalendarificResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/holidays")
@RequiredArgsConstructor
public class PublicHolidaysController {

    private final PublicHolidaysService publicHolidaysService;

    @GetMapping("/{year}")
    public ResponseEntity<ApiResponse<CalendarificResponse>> syncHolidays(@PathVariable int year){
        CalendarificResponse response = publicHolidaysService.syncHolidays(year);
        return new ResponseEntity<>(ApiResponse.successResponse(response), HttpStatusCode.valueOf(response.meta().code()));

    }

}
