package com.project.stayEase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class HotelInfoDto {
    private HotelResponseDto hotelResponseDto;
    private List<RoomResponseDto> rooms;
}
