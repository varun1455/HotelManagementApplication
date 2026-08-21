package com.project.stayEase.configuration.projection;

import com.project.stayEase.entity.Hotel;

import java.math.BigDecimal;

public record HotelSearchProjection(

        Hotel hotel,
        BigDecimal startingFrom

) {}
