package com.project.stayEase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSearchResponseDto {
    private long id;
    private RoomTypeResponseDto type;
    private BedTypeResponseDto bedType;
    private String[] photos;
    private String[] amenities;
    private Integer availableNumberOfRooms;
    private BigDecimal totalPrice;
    private Integer capacity;
}
