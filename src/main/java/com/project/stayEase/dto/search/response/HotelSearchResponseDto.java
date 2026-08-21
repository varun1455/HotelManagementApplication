package com.project.stayEase.dto.search.response;

import com.project.stayEase.entity.HotelContactInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HotelSearchResponseDto {
    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private HotelContactInfo contactInfo;
    private List<RoomSearchResponseDto> rooms;
    private BigDecimal startingFrom;


}
