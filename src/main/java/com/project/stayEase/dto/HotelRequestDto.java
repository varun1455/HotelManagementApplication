package com.project.stayEase.dto;

import com.project.stayEase.entity.HotelContactInfo;

import lombok.Data;


@Data
public class HotelRequestDto {

    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContactInfo contactInfo;

}
