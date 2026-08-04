package com.project.stayEase.service.holiday.client.dto;

import java.util.List;

public record HolidayDto(

        String name,
        HolidayDate date,
        List<String> type
) {}
