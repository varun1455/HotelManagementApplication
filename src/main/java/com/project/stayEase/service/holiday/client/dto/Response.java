package com.project.stayEase.service.holiday.client.dto;

import java.util.List;

public record Response(
        List<HolidayDto> holidays
) {}
