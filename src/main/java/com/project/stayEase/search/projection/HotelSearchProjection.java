package com.project.stayEase.search.projection;

import com.project.stayEase.entity.Hotel;

import java.math.BigDecimal;

public record HotelSearchProjection(

        // Long hotelId,
        Hotel hotel,
        BigDecimal startingFrom

) {}
