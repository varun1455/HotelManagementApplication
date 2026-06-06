package com.project.stayEase.dto;

import com.project.stayEase.entity.HotelContactInfo;
import lombok.Data;

@Data
public class HotelSummyDtoForBooking {
    private long id;
    private String name;
    private String city;
    private HotelContactInfo contactInfo;
}
