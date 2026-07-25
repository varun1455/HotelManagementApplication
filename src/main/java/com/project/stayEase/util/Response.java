package com.project.stayEase.util;

import java.util.List;

public record Response(
        List<HolidayDto> holidays
) {}
