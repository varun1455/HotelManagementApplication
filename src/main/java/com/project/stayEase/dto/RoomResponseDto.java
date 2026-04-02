package com.project.stayEase.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomResponseDto {

    private Long id;
    private Long roomTypeId;
    private Long bedTypeId;
    private BigDecimal basePrice;
    private String[] photos;
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;
}
