package com.project.stayEase.service.holiday;

import com.project.stayEase.service.holiday.client.CalendarificResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class CalendarificClient {

    private final RestClient restClient;

    @Value("${calendarific.api.key}")
    private String apiKey;

    @Value("${calendarific.host.url}")
    private String baseurl;


    public CalendarificResponse fetchHolidays(int year) {

        String url = baseurl +
                "/holidays?api_key=" + apiKey +
                "&country=IN" +
                "&year=" + year;

        CalendarificResponse response = restClient.get()
                .uri(url)
                .retrieve()
                .body(CalendarificResponse.class);

        return response;
    }





}
