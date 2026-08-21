package com.project.stayEase.dto.holiday.response;

import java.util.List;

public record HolidayDto(

        String name,
        HolidayDate date,
        List<String> type
) {}
