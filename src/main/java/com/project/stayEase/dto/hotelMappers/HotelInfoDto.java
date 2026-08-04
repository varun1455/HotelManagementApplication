package com.project.stayEase.dto.hotelMappers;

import com.project.stayEase.dto.roomMappers.RoomResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class HotelInfoDto {
    private HotelResponseDto hotelResponseDto;
    private List<RoomResponseDto> rooms;
}
