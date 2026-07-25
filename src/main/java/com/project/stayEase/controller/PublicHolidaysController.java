package com.project.stayEase.controller;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.service.PublicHolidaysService;
import com.project.stayEase.util.CalendarificResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/system/holidays")
@RequiredArgsConstructor
public class PublicHolidaysController {

    private final RestClient restClient;
    private final PublicHolidaysService publicHolidaysService;

    @Value("${calendarific.api.key}")
    private String apiKey;

    @Value("${calendarific.host.url}")
    private String baseurl;


    @GetMapping("/{year}")
    public ResponseEntity<ApiResponse<CalendarificResponse>> getPublicHolidays(@PathVariable int year){
        String url = baseurl +
                "/holidays?api_key=" + apiKey +
                "&country=IN" +
                "&year=" + year;

        System.out.println(url);

       CalendarificResponse response = restClient.get()
                .uri(url)
                .retrieve()
                .body(CalendarificResponse.class);
        publicHolidaysService.storeHolidaysToDB(response);

        assert response != null;
        return new ResponseEntity<>(ApiResponse.successResponse(response), HttpStatusCode.valueOf(response.meta().code()));

    }




}
