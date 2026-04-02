package com.project.stayEase.dto;

import com.project.stayEase.entity.HotelContactInfo;
import lombok.Data;

@Data
public class HotelResponseDto {

    private long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContactInfo contactInfo;
    private boolean active;
}
