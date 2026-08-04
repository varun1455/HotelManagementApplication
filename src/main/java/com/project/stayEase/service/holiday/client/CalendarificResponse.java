package com.project.stayEase.service.holiday.client;

import com.project.stayEase.service.holiday.client.dto.Meta;
import com.project.stayEase.service.holiday.client.dto.Response;

public record CalendarificResponse(
    Meta meta,
    Response response
){}
