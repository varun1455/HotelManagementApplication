package com.project.stayEase.util;

import java.util.List;

public record HolidayDto(

        String name,
        HolidayDate date,
        List<String> type
) {}
