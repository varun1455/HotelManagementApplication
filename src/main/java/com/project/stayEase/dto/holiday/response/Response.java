package com.project.stayEase.dto.holiday.response;

import java.util.List;

public record Response(
        List<HolidayDto> holidays
) {}
