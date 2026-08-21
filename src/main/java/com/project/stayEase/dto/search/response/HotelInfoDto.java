package com.project.stayEase.dto.search.response;

import com.project.stayEase.dto.hotel.response.HotelResponseDto;
import com.project.stayEase.dto.room.response.RoomResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class HotelInfoDto {
    private HotelResponseDto hotelResponseDto;
    private List<RoomResponseDto> rooms;
}
