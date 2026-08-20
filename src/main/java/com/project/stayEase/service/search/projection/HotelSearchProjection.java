package com.project.stayEase.service.search.projection;

import com.project.stayEase.entity.Hotel;

import java.math.BigDecimal;

public record HotelSearchProjection(

        Hotel hotel,
        BigDecimal startingFrom

) {}
